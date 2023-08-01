use logging::setup_klog;
// Has to be pub so all symbols in that crate is included
pub use liorsmagicpolicy;

mod logging;

#[cxx::bridge]
pub mod ffi {
    #[namespace = "rust"]
    extern "Rust" {
        fn setup_klog();
    }
}
