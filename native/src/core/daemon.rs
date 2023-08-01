use std::cell::RefCell;
use std::fs::File;
use std::io;
use std::sync::{Mutex, OnceLock};

use base::{copy_str, cstr, Directory, ResultExt, Utf8CStr, WalkResult};

use crate::logging::{liorsmagic_logging, zygisk_logging};

// Global liorsmagicd singleton
pub static LIORSMAGICD: OnceLock<LiorsmagicD> = OnceLock::new();

#[derive(Default)]
pub struct LiorsmagicD {
    pub logd: Mutex<RefCell<Option<File>>>,
}

pub fn daemon_entry() {
    let liorsmagicd = LiorsmagicD::default();
    liorsmagicd.start_log_daemon();
    LIORSMAGICD.set(liorsmagicd).ok();
    liorsmagic_logging();
}

pub fn zygisk_entry() {
    let liorsmagicd = LiorsmagicD::default();
    LIORSMAGICD.set(liorsmagicd).ok();
    zygisk_logging();
}

pub fn get_liorsmagicd() -> &'static LiorsmagicD {
    LIORSMAGICD.get().unwrap()
}

impl LiorsmagicD {}

pub fn find_apk_path(pkg: &[u8], data: &mut [u8]) -> usize {
    use WalkResult::*;
    fn inner(pkg: &[u8], data: &mut [u8]) -> io::Result<usize> {
        let mut len = 0_usize;
        let pkg = match Utf8CStr::from_bytes(pkg) {
            Ok(pkg) => pkg,
            Err(e) => return Err(io::Error::new(io::ErrorKind::Other, e)),
        };
        Directory::open(cstr!("/data/app"))?.pre_order_walk(|e| {
            if !e.is_dir() {
                return Ok(Skip);
            }
            let d_name = e.d_name().to_bytes();
            if d_name.starts_with(pkg.as_bytes()) && d_name[pkg.len()] == b'-' {
                // Found the APK path, we can abort now
                len = e.path(data)?;
                return Ok(Abort);
            }
            if d_name.starts_with(b"~~") {
                return Ok(Continue);
            }
            Ok(Skip)
        })?;
        if len > 0 {
            len += copy_str(&mut data[len..], "/base.apk");
        }
        Ok(len)
    }
    inner(pkg, data).log().unwrap_or(0)
}
