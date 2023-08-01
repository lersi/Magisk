#![feature(format_args_nl)]
#![allow(clippy::missing_safety_doc)]

use base::Utf8CStr;
use cert::read_certificate;
use daemon::{daemon_entry, find_apk_path, get_liorsmagicd, zygisk_entry, LiorsmagicD};
use logging::{android_logging, liorsmagic_logging, zygisk_logging};

mod cert;
#[path = "../include/consts.rs"]
mod consts;
mod daemon;
mod logging;

#[cxx::bridge]
pub mod ffi {
    extern "C++" {
        include!("resetprop/resetprop.hpp");
        unsafe fn get_prop_rs(name: *const c_char, persist: bool) -> String;
    }

    extern "Rust" {
        fn rust_test_entry();
        fn android_logging();
        fn liorsmagic_logging();
        fn zygisk_logging();
        fn find_apk_path(pkg: &[u8], data: &mut [u8]) -> usize;
        fn read_certificate(fd: i32, version: i32) -> Vec<u8>;
    }

    #[namespace = "rust"]
    extern "Rust" {
        fn daemon_entry();
        fn zygisk_entry();

        type LiorsmagicD;
        fn get_liorsmagicd() -> &'static LiorsmagicD;
        fn get_log_pipe(self: &LiorsmagicD) -> i32;
        fn close_log_pipe(self: &LiorsmagicD);
        fn setup_logfile(self: &LiorsmagicD);
    }
}

fn rust_test_entry() {}

pub fn get_prop(name: &Utf8CStr, persist: bool) -> String {
    unsafe { ffi::get_prop_rs(name.as_ptr(), persist) }
}
