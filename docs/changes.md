# Liorsmagic Changelog

### v26.1

- [App] Fix crashing when revoking root permissions
- [LiorsmagicInit] Always prefer `ext4` partitions over `f2fs` when selecting the pre-init partition
- [General] Restore module files' context/owner/group from mirror. This is a regression introduced in v26.0

### v26.0

- [General] Bump minimum supported Android version to Android 6.0
- [General] New magic mount backend. It supports loading modules into system with `overlayfs` files injected
- [Zygisk] Release new API version 4
- [Zygisk] Prevent crashing daemon in error
- [Zygisk] Rewrite zygote code injection with new loader library approach
- [Zygisk] Rewrite code unloading implementation
- [LiorsmagicBoot] Support amonet microloader devices
- [LiorsmagicBoot] Always use lz4_legacy compression on v4 boot images. This fixes boot image patching issues on Android U preview.
- [LiorsmagicInit] Support replacing existing \*.rc files in `overlay.d`
- [LiorsmagicInit] Rewrite sepolicy.rules mounting and loading implementation
- [App] Make stub patching 100% offline
- [App] Support patching `init_boot.img` for Samsung ODIN firmware
- [LiorsmagicPolicy] Fix minor bug in command line argument parsing
- [LiorsmagicPolicy] Update rules to support Android U

### v25.2

- [LiorsmagicInit] Fix a potential issue when stub cpio is used
- [LiorsmagicInit] Fix reboot to recovery when stub cpio is used
- [LiorsmagicInit] Fix sepolicy.rules symlink for rootfs devices
- [General] Better data encryption detection
- [General] Move the whole logging infrastructure into Rust

### v25.1

- [LiorsmagicBoot] Fix ramdisk backup being incorrectly skipped
- [LiorsmagicBoot] Add new feature to detect unsupported dtb and abort during installation
- [Zygisk] Change binary hijack paths
- [App] Fix incorrect recovery mode detection and installation
- [LiorsmagicInit] Fix config not properly exported in legacy SAR devices
- [General] Enforce the Liorsmagic app to always match or be newer than `liorsmagicd`

### v25.0

- [LiorsmagicInit] Update 2SI implementation, significantly increase device compatibility (e.g. Sony Xperia devices)
- [LiorsmagicInit] Introduce new `sepolicy` injection mechanism
- [LiorsmagicInit] Support Oculus Go
- [LiorsmagicInit] Support Android 13 GKIs (Pixel 6)
- [LiorsmagicBoot] Fix vbmeta extraction implementation
- [App] Fix stub app on older Android versions
- [App] [LiorsmagicSU] Properly support apps using `sharedUserId`
- [LiorsmagicSU] Fix a possible crash in `liorsmagicd`
- [LiorsmagicSU] Prune unused UIDs as soon as `system_server` restarts to prevent UID reuse attacks
- [LiorsmagicSU] Verify and enforce the installed Liorsmagic app's certificate to match the distributor's signature
- [LiorsmagicSU] [Zygisk] Proper package management and detection
- [Zygisk] Fix function hooking on devices running Android 12 with old kernels
- [Zygisk] Fix Zygisk's self code unloading implementation
- [DenyList] Fix DenyList on shared UID apps
- [BusyBox] Add workaround for devices running old kernels

### v24.3

- [General] Stop using `getrandom` syscall
- [Zygisk] Update API to v3, adding new fields to `AppSpecializeArgs`
- [App] Improve app repackaging installation workflow

### v24.2

- [LiorsmagicSU] Fix buffer overflow
- [LiorsmagicSU] Fix owner managed multiuser superuser settings
- [LiorsmagicSU] Fix command logging when using `su -c <cmd>`
- [LiorsmagicSU] Prevent su request indefinite blocking
- [LiorsmagicBoot] Support `lz4_legacy` archive with multiple magic
- [LiorsmagicBoot] Fix `lz4_lg` compression
- [DenyList] Allow targeting processes running as system UID
- [Zygisk] Workaround Samsung's "early zygote"
- [Zygisk] Improved Zygisk loading mechanism
- [Zygisk] Fix application UID tracking
- [Zygisk] Fix improper `umask` being set in zygote
- [App] Fix BusyBox execution test
- [App] Improve stub loading mechanism
- [App] Major app upgrade flow improvements
- [General] Improve commandline error handling and messaging

### v24.1

- [App] Stability improvements

### v24.0

- [General] LiorsmagicHide is removed from Liorsmagic
- [General] Support Android 12
- [General] Support devices that do not support 32-bit and only runs 64-bit code
- [General] Update BusyBox to 1.34.1
- [Zygisk] Introduce new feature: Zygisk
- [Zygisk] Introduce DenyList feature to revert Liorsmagic features in user selected processes
- [LiorsmagicBoot] Support patching 32-bit kernel zImages
- [LiorsmagicBoot] Support boot image header v4
- [LiorsmagicBoot] Support patching out `skip_initramfs` from dtb bootargs
- [LiorsmagicBoot] Add new env variable `PATCHVBMETAFLAG` to configure whether vbmeta flags should be patched
- [LiorsmagicInit] Support loading fstab from `/system/etc` (required for Pixel 6)
- [LiorsmagicInit] Support `/proc/bootconfig` for loading boot configurations
- [LiorsmagicInit] Better support for some Meizu devices
- [LiorsmagicInit] Better support for some OnePlus/Oppo/Realme devices
- [LiorsmagicInit] Support `init.real` on some Sony devices
- [LiorsmagicInit] Skip loading Liorsmagic when detecting DSU
- [LiorsmagicPolicy] Load `*_compat_cil_file` from system_ext
- [LiorsmagicSU] Use isolated devpts if the kernel supports it
- [LiorsmagicSU] Fix root shell if isolated mount namespace is set
- [resetprop] Deleted properties are now wiped from memory instead of just unlinking
- [App] Build a single APK for all ABIs
- [App] Switch to use standard bottom navigation bar
- [App] Downloading modules from the centralized Liorsmagic-Modules-Repo is removed
- [App] Support user configuration of boot image vbmeta patching
- [App] Restore the ability to install Liorsmagic on the other slot on some A/B devices
- [App] Allow modules to specify an update URL for in-app update + install

### v23.0

- [App] Update snet extension. This fixes SafetyNet API errors.
- [App] Fix a bug in the stub app that causes APK installation to fail
- [App] Hide annoying errors in logs when hidden as stub
- [App] Fix issues when patching ODIN tar files when the app is hidden
- [General] Remove all pre Android 5.0 support
- [General] Update BusyBox to use proper libc
- [General] Fix C++ undefined behaviors
- [General] Several `sepolicy.rule` copy/installation fixes
- [LiorsmagicPolicy] Remove unnecessary sepolicy rules
- [LiorsmagicHide] Update package and process name validation logic
- [LiorsmagicHide] Some changes that prevents zygote deadlock

### v22.1

- [App] Prevent multiple installation sessions running in parallel
- [App] Prevent OutOfMemory crashes when checking boot signature on PXA boot images
- [General] Proper cgroup migration implementation
- [General] Rewrite log writer from scratch, should resolve any crashes and deadlocks
- [General] Many scripts updates fixing regressions
- [LiorsmagicHide] Prevent possible deadlock when signal arrives
- [LiorsmagicHide] Partial match process names if necessary
- [LiorsmagicBoot] Preserve and patch AVB 2.0 structures/headers in boot images
- [LiorsmagicBoot] Properly strip out data encryption flags
- [LiorsmagicBoot] Prevent possible integer overflow
- [LiorsmagicInit] Fix `sepolicy.rule` mounting strategy
- [resetprop] Always delete existing `ro.` props before updating. This will fix bootloops that could be caused by modifying device fingerprint properties.

### v22.0

- [General] Liorsmagic and Liorsmagic Manager is now merged into the same package!
- [App] The term "Liorsmagic Manager" is no longer used elsewhere. We refer it as the Liorsmagic app.
- [App] Support hiding the Liorsmagic app with advanced technique (stub APK loading) on Android 5.0+ (it used to be 9.0+)
- [App] Disallow re-packaging the Liorsmagic app on devices lower than Android 5.0
- [App] Detect and warn about multiple invalid states and provide instructions on how to resolve it
- [LiorsmagicHide] Fix a bug when stopping LiorsmagicHide does not take effect
- [LiorsmagicBoot] Fix bug when unpacking `lz4_lg` compressed boot images
- [LiorsmagicInit] Support Galaxy S21 series
- [LiorsmagicSU] Fix incorrect APEX paths that caused `libsqlite.so` fail to load

### v21.4

- [LiorsmagicSU] Fix `su -c` behavior that broke many root apps
- [General] Properly handle read/write over sockets (the `broken pipe` issue)

### v21.3

- [LiorsmagicInit] Avoid mounting `f2fs` userdata as it may result in kernel crashes. This shall fix a lot of bootloops
- [LiorsmagicBoot] Fix a minor header checksum bug for `DHTB` header and ASUS `blob` image formats
- [LiorsmagicHide] Allowing hiding isolated processes if the mount namespace is separated

### v21.2

- [LiorsmagicInit] Detect 2SI after mounting `system_root` on legacy SAR devices
- [General] Make sure `post-fs-data` scripts cannot block more than 35 seconds
- [General] Fix the `liorsmagic --install-module` command
- [General] Trim Windows newline when reading files
- [General] Directly log to file to prevent `logcat` weirdness
- [LiorsmagicBoot] Fix header dump/load for header v3 images

### v21.1

- [LiorsmagicBoot] Support boot header v3 (Pixel 5 and 4a 5G)
- [LiorsmagicBoot] Distinguish `lz4_lg` and `lz4_legacy` (Pixel 5 and 4a 5G)
- [LiorsmagicBoot] Support vendor boot images (for dev, not relevant for Liorsmagic installation)
- [LiorsmagicInit] Support kernel cmdline `androidboot.fstab_suffix`
- [LiorsmagicInit] Support kernel initialized dm-verity on legacy SAR
- [General] Significantly broaden sepolicy.rule compatibility
- [General] Add Liorsmagic binaries to `PATH` when executing boot scripts
- [General] Update `--remove-modules` command implementation
- [General] Make Liorsmagic properly survive after factory reset on Android 11
- [LiorsmagicSU] Add APEX package `com.android.i18n` to `LD_LIBRARY_PATH` when linking `libsqlite.so`
- [LiorsmagicHide] Support hiding apps installed in secondary users (e.g. work profile)
- [LiorsmagicHide] Make zygote detection more robust

### v21.0

- [General] Support Android 11 🎉
- [General] Add Safe Mode detection. Disable all modules when the device is booting into Safe Mode.
- [General] Increase `post-fs-data` mode timeout from 10 seconds to 40 seconds
- [LiorsmagicInit] Rewritten 2SI support from scratch
- [LiorsmagicInit] Support when no `/sbin` folder exists (Android 11)
- [LiorsmagicInit] Dump fstab from device-tree to rootfs and force `init` to use it for 2SI devices
- [LiorsmagicInit] Strip out AVB for 2SI as it may cause bootloop
- [Modules] Rewritten module mounting logic from scratch
- [LiorsmagicSU] For Android 8.0+, a completely new policy setup is used. This reduces compromises in Android's sandbox, providing more policy isolation and better security for root users.
- [LiorsmagicSU] Isolated mount namespace will now first inherit from parent process, then isolate itself from the world
- [LiorsmagicSU] Update communication protocol with Liorsmagic Manager to work with the hardened SELinux setup
- [LiorsmagicPolicy] Optimize match all rules. This will significantly reduce policy binary size and save memory and improve general kernel performance.
- [LiorsmagicPolicy] Support declaring new types and attributes
- [LiorsmagicPolicy] Make policy statement closer to stock `*.te` format. Please check updated documentation or `liorsmagicpolicy --help` for more details.
- [LiorsmagicBoot] Support compressed `extra` blobs
- [LiorsmagicBoot] Pad boot images to original size with zeros
- [LiorsmagicHide] Manipulate additional vendor properties

### v20.4

- [LiorsmagicInit] Fix potential bootloop in A-only 2SI devices
- [LiorsmagicInit] Properly support Tegra partition naming
- [General] Load libsqlite.so dynamically, which removes the need to use wrapper scripts on Android 10+
- [General] Detect API level with a fallback method on some devices
- [General] Workaround possible bug in x86 kernel readlinkat system call
- [BusyBox] Enable SELinux features. Add chcon/runcon etc., and '-Z' option to many applets
- [BusyBox] Introduce standalone mode. More details in release notes
- [LiorsmagicHide] Disable LiorsmagicHide by default
- [LiorsmagicHide] Add more potential detectable system properties
- [LiorsmagicHide] Add workaround for Xiaomi devices bootloop when LiorsmagicHide is enabled on cross region ROMs
- [LiorsmagicBoot] Support patching special Motorolla DTB format
- [LiorsmagicPolicy] Support 'genfscon' sepolicy rules
- [Scripts] Support NAND based boot images (character nodes in /dev/block)
- [Scripts] Better addon.d (both v1 and v2) support
- [Scripts] Support Lineage Recovery for Android 10+

### v20.3

- [LiorsmagicBoot] Fix `lz4_legacy` decompression

### v20.2

- [LiorsmagicSU] Properly handle communication between daemon and application (root request prompt)
- [LiorsmagicInit] Fix logging in kmsg
- [LiorsmagicBoot] Support patching dtb/dtbo partition formats
- [General] Support pre-init sepolicy patch in modules
- [Scripts] Update liorsmagic stock image backup format

### v20.1

- [LiorsmagicSU] Support component name agnostic communication (for stub APK)
- [LiorsmagicBoot] Set proper `header_size` in boot image headers (fix vbmeta error on Samsung devices)
- [LiorsmagicHide] Scan zygote multiple times
- [LiorsmagicInit] Support recovery images without /sbin/recovery binary. This will fix some A/B devices unable to boot to recovery after flashing Liorsmagic
- [General] Move acct to prevent daemon being killed
- [General] Make sure "--remove-modules" will execute uninstall.sh after removal

### v20.0

- [LiorsmagicBoot] Support inject/modify `mnt_point` value in DTB fstab
- [LiorsmagicBoot] Support patching QCDT
- [LiorsmagicBoot] Support patching DTBH
- [LiorsmagicBoot] Support patching PXA-DT
- [LiorsmagicInit] [2SI] Support non A/B setup (Android 10)
- [LiorsmagicHide] Fix bug that reject process names with ":"
- [MagicMount] Fix a bug that cause /product mirror not created

### v19.4

- [LiorsmagicInit] [SAR] Boot system-as-root devices with system mounted as /
- [LiorsmagicInit] [2SI] Support 2-stage-init for A/B devices (Pixel 3 Android 10)
- [LiorsmagicInit] [initramfs] Delay sbin overlay creation to post-fs-data
- [LiorsmagicInit] [SARCompat] Old system-as-root implementation is deprecated, no more future changes
- [LiorsmagicInit] Add overlay.d support for root directory overlay for new system-as-root implementation
- [LiorsmagicSU] Unblock all signals in root shells (fix bash on Android)
- [MagicMount] Support replacing files in /product
- [LiorsmagicHide] Support Android 10's Zygote blastula pool
- [LiorsmagicHide] All random strings now also have random length
- [LiorsmagicBoot] Allow no recompression for ramdisk.cpio
- [LiorsmagicBoot] Support some weird Huawei boot images
- [General] Add new `--remove-modules` command to remove modules without root in ADB shell
- [General] Support Android 10 new APEX libraries (Project Mainline)

### v19.3

- [LiorsmagicHide] Hugely improve process monitor implementation, hopefully should no longer cause 100% CPU and daemon crashes
- [LiorsmagicInit] Wait for partitions to be ready for early mount, should fix bootloops on a handful of devices
- [LiorsmagicInit] Support EROFS used in EMUI 9.1
- [LiorsmagicSU] Properly implement mount namespace isolation
- [LiorsmagicBoot] Proper checksum calculation for header v2

### v19.2

- [General] Fix uninstaller
- [General] Fix bootloops on some devices with tmpfs mounting to /data
- [LiorsmagicInit] Add Kirin hi6250 support
- [LiorsmagicSU] Stop claiming device focus for su logging/notify if feasible.
  This fix issues with users locking Liorsmagic Manager with app lock, and prevent
  video apps get messed up when an app is requesting root in the background.

### v19.1

- [General] Support recovery based Liorsmagic
- [General] Support Android Q Beta 2
- [LiorsmagicInit] New sbin overlay setup process for better compatibility
- [LiorsmagicInit] Allow long pressing volume up to boot to recovery in recovery mode
- [MagicMount] Use proper system_root mirror
- [MagicMount] Use self created device nodes for mirrors
- [MagicMount] Do not allow adding new files/folders in partition root folder (e.g. /system or /vendor)

### v19.0

- [General] Remove usage of liorsmagic.img
- [General] Add 64 bit liorsmagic binary for native 64 bit support
- [General] Support A only system-as-root devices that released with Android 9.0
- [General] Support non EXT4 system and vendor partitions
- [LiorsmagicHide] Use Zygote ptracing for monitoring new processes
- [LiorsmagicHide] Targets are now per-application component
- [LiorsmagicInit] Support Android Q (no logical partition support yet!)
- [LiorsmagicPolicy] Support Android Q new split sepolicy setup
- [LiorsmagicInit] Move sbin overlay creation from main daemon post-fs-data to early-init
- [General] Service scripts now run in parallel
- [LiorsmagicInit] Directly inject liorsmagic services to init.rc
- [General] Use lzma2 compressed ramdisk in extreme conditions
- [MagicMount] Clone attributes from original file if exists
- [LiorsmagicSU] Use `ACTION_REBOOT` intent to workaround some OEM broadcast restrictions
- [General] Use `skip_mount` instead of `auto_mount`: from opt-in to opt-out

### v18.1

- [General] Support EMUI 9.0
- [General] Support Kirin 960 devices
- [General] Support down to Android 4.2
- [General] Major code base modernization under-the-hood

### v18.0

- [General] Migrate all code base to C++
- [General] Modify database natively instead of going through Liorsmagic Manager
- [General] Deprecate path /sbin/.core, please start using /sbin/.liorsmagic
- [General] Boot scripts are moved from `<liorsmagic_img>/.core/<stage>.d` to `/data/adb/<stage>.d`
- [General] Remove native systemless hosts (Liorsmagic Manager is updated with a built-in systemless hosts module)
- [General] Allow module post-fs-data.sh scripts to disable/remove modules
- [LiorsmagicHide] Use component names instead of process names as targets
- [LiorsmagicHide] Add procfs protection on SDK 24+ (Nougat)
- [LiorsmagicHide] Remove the folder /.backup to prevent detection
- [LiorsmagicHide] Hide list is now stored in database instead of raw textfile in images
- [LiorsmagicHide] Add "--status" option to CLI
- [LiorsmagicHide] Stop unmounting non-custom related mount points
- [LiorsmagicSU] Add `FLAG_INCLUDE_STOPPED_PACKAGES` in broadcasts to force wake Liorsmagic Manager
- [LiorsmagicSU] Fix a bug causing SIGWINCH not properly detected
- [LiorsmagicPolicy] Support new av rules: type_change, type_member
- [LiorsmagicPolicy] Remove all AUDITDENY rules after patching sepolicy to log all denies for debugging
- [LiorsmagicBoot] Properly support extra_cmdline in boot headers
- [LiorsmagicBoot] Try to repair broken v1 boot image headers
- [LiorsmagicBoot] Add new CPIO command: "exists"

### v17.3

- [LiorsmagicBoot] Support boot image header v1 (Pixel 3)
- [LiorsmagicSU] No more linked lists for caching `su_info`
- [LiorsmagicSU] Parse command-lines in client side and send only options to daemon
- [LiorsmagicSU] Early ACK to prevent client freezes and early denies
- [Daemon] Prevent bootloops in situations where /data is mounted twice
- [Daemon] Prevent logcat failures when /system/bin is magic mounting, could cause LiorsmagicHide to fail
- [Scripts] Switch hexpatch to remove Samsung Defex to a more general pattern
- [Scripts] Update data encryption detection for better custom recovery support

### v17.2

- [ResetProp] Update to AOSP upstream to support serialized system properties
- [LiorsmagicInit] Randomize Liorsmagic service names to prevent detection (e.g. FGO)
- [LiorsmagicSU] New communication scheme to communicate with Liorsmagic Manager

### v17.0/17.1

- [General] Bring back install to inactive slot for OTAs on A/B devices
- [Script] Remove system based root in addon.d
- [Script] Add proper addon.d-v2 for preserving Liorsmagic on custom ROMs on A/B devices
- [Script] Enable KEEPVERITY when the device is using system_root_image
- [Script] Add hexpatch to remove Samsung defex in new Oreo kernels
- [Daemon] Support non ext4 filesystems for mirrors (system/vendor)
- [LiorsmagicSU] Make pts sockets always run in dev_pts secontext, providing all terminal emulator root shell the same power as adb shells
- [LiorsmagicHide] Kill all processes with same UID of the target to workaround OOS embryo optimization
- [LiorsmagicInit] Move all sepolicy patches pre-init to prevent Pixel 2 (XL) boot service breakdown

### v16.7

- [Scripts] Fix boot image patching errors on Android P (workaround the strengthened seccomp)
- [LiorsmagicHide] Support hardlink based ns proc mnt (old kernel support)
- [Daemon] Fix permission of /dev/null after logcat commands, fix ADB on EMUI
- [Daemon] Log fatal errors only on debug builds
- [LiorsmagicInit] Detect early mount partname from fstab in device tree

### v16.6

- [General] Add wrapper script to overcome weird `LD_XXX` flags set in apps
- [General] Prevent bootloop when flashing Liorsmagic after full wipe on FBE devices
- [Scripts] Support patching DTB placed in extra sections in boot images (Samsung S9/S9+)
- [Scripts] Add support for addon.d-v2 (untested)
- [Scripts] Fix custom recovery console output in addon.d
- [Scripts] Fallback to parsing sysfs for detecting block devices
- [Daemon] Check whether a valid Liorsmagic Manager is installed on boot, if not, install stub APK embedded in liorsmagicinit
- [Daemon] Check whether Liorsmagic Manager is repackaged (hidden), and prevent malware from hijacking com.topjohnwu.liorsmagic
- [Daemon] Introduce new daemon: liorsmagiclogd, a dedicated daemon to handle all logcat related monitoring
- [Daemon] Replace old invincible mode with handshake between liorsmagicd and liorsmagiclogd, one will respawn the other if disconnected
- [Daemon] Support GSI adbd bind mounting
- [LiorsmagicInit] Support detecting block names in upper case (Samsung)
- [LiorsmagicBoot] Check DTB headers to prevent false detections within kernel binary
- [LiorsmagicHide] Compare mount namespace with PPID to make sure the namespace is actually separated, fix root loss
- [LiorsmagicSU] Simplify `su_info` caching system, should use less resources and computing power
- [LiorsmagicSU] Reduce the amount of broadcasting to Liorsmagic Manager
- [ImgTool] Separate all ext4 image related operations to a new applet called "imgtool"
- [ImgTool] Use precise free space calculation methods
- [ImgTool] Use our own set of loop devices hidden along side with sbin tmpfs overlay. This not only eliminates another possible detection method, but also fixes apps that mount OBB files as loop devices (huge thanks to dev of Pzizz for reporting this issue)

### v16.4

- [Daemon] Directly check logcat command instead of detecting logd, should fix logging and LiorsmagicHide on several Samsung devices
- [Daemon] Fix startup Liorsmagic Manager APK installation on Android P
- [LiorsmagicPolicy] Switch from AOSP u:r:su:s0 to u:r:liorsmagic:s0 to prevent conflicts
- [LiorsmagicPolicy] Remove unnecessary sepolicy rules to reduce security penalty
- [Daemon] Massive re-design /sbin tmpfs overlay and daemon start up
- [LiorsmagicInit] Remove `liorsmagicinit_daemon`, the actual liorsmagic daemon (liorsmagicd) shall handle everything itself
- [Daemon] Remove post-fs stage as it is very limited and also will not work on A/B devices; replaced with simple mount in post-fs-data, which will run ASAP even before the daemon is started
- [General] Remove all 64-bit binaries as there is no point in using them; all binaries are now 32-bit only.
  Some weirdly implemented root apps might break (e.g. Tasker, already reported to the developer), but it is not my fault :)
- [resetprop] Add Protobuf encode/decode to support manipulating persist properties on Android P
- [LiorsmagicHide] Include app sub-services as hiding targets. This might significantly increase the amount of apps that could be properly hidden

### v16.3

- [General] Remove symlinks used for backwards compatibility
- [LiorsmagicBoot] Fix a small size calculation bug

### v16.2

- [General] Force use system binaries in handling ext4 images (fix module installation on Android P)
- [LiorsmagicHide] Change property state to disable if logd is disabled

### v16.1

- [LiorsmagicBoot] Fix MTK boot image packaging
- [LiorsmagicBoot] Add more Nook/Acclaim headers support
- [LiorsmagicBoot] Support unpacking DTB with empty kernel image
- [LiorsmagicBoot] Update high compression mode detection logic
- [Daemon] Support new mke2fs tool on Android P
- [resetprop] Support Android P new property context files
- [LiorsmagicPolicy] Add new rules for Android P

### v16.0

- [LiorsmagicInit] Support non `skip_initramfs` devices with slot suffix (Huawei Treble)
- [LiorsmagicPolicy] Add rules for Liorsmagic Manager
- [Compiler] Workaround an NDK compiler bug that causes bootloops

### v15.4

- [LiorsmagicBoot] Support Samsung PXA, DHTB header images
- [LiorsmagicBoot] Support ASUS blob images
- [LiorsmagicBoot] Support Nook Green Loader images
- [LiorsmagicBoot] Support pure ramdisk images
- [LiorsmagicInit] Prevent OnePlus angela `sepolicy_debug` from loading
- [LiorsmagicInit] Obfuscate Liorsmagic socket entry to prevent detection and security
- [Daemon] Fix subfolders in /sbin shadowed by overlay
- [Daemon] Obfuscate binary names to prevent naive detections
- [Daemon] Check logd before force trying to start logcat in a loop

### v15.3

- [Daemon] Fix the bug that only one script would be executed in post-fs-data.d/service.d
- [Daemon] Add `MS_SILENT` flag when mounting, should fix some devices that cannot mount liorsmagic.img
- [LiorsmagicBoot] Fix potential segmentation fault when patching ramdisk, should fix some installation failures

### v15.2

- [LiorsmagicBoot] Fix dtb verity patches, should fix dm-verity bootloops on newer devices placing fstabs in dtb
- [LiorsmagicPolicy] Add new rules for proper Samsung support, should fix LiorsmagicHide
- [LiorsmagicInit] Support non `skip_initramfs` devices using split sepolicies (e.g. Zenfone 4 Oreo)
- [Daemon] Use specific logcat buffers, some devices does not support all log buffers
- [scripts] Update scripts to double check whether boot slot is available, some devices set a boot slot without A/B partitions

### v15.1

- [LiorsmagicBoot] Fix faulty code in ramdisk patches which causes bootloops in some config and fstab format combos

### v15.0

- [Daemon] Fix the bug that Liorsmagic cannot properly detect /data encryption state
- [Daemon] Add merging `/cache/liorsmagic.img` and `/data/adb/liorsmagic_merge.img` support
- [Daemon] Update to upstream libsepol to support cutting edge split policy custom ROM cil compilations

### v14.6 (1468)

- [General] Move all files into a safe location: /data/adb
- [Daemon] New invincible implementation: use `liorsmagicinit_daemon` to monitor sockets
- [Daemon] Rewrite logcat monitor to be more efficient
- [Daemon] Fix a bug where logcat monitor may spawn infinite logcat processes
- [LiorsmagicSU] Update su to work the same as proper Linux implementation:
  Initialize window size; all environment variables will be migrated (except HOME, SHELL, USER, LOGNAME, these will be set accordingly),
  "--preserve-environment" option will preserve all variables, including those four exceptions.
  Check the Linux su manpage for more info
- [LiorsmagicBoot] Massive refactor, rewrite all cpio operations and CLI
- [LiorsmagicInit][liorsmagicboot] Support ramdisk high compression mode

### v14.5 (1456)

- [Liorsmagicinit] Fix bootloop issues on several devices
- [misc] Build binaries with NDK r10e, should get rid of the nasty linker warning when executing liorsmagic

### v14.5 (1455)

- [Daemon] Moved internal path to /sbin/.core, new image mountpoint is /sbin/.core/img
- [LiorsmagicSU] Support switching package name, used when Liorsmagic Manager is hidden
- [LiorsmagicHide] Add temporary /liorsmagic removal
- [LiorsmagicHide] All changes above contributes to hiding from nasty apps like FGO and several banking apps
- [Liorsmagicinit] Use liorsmagicinit for all devices (dynamic initramfs)
- [Liorsmagicinit] Fix Xiaomi A1 support
- [Liorsmagicinit] Add Pixel 2 (XL) support
- [Liorsmagicboot] Add support to remove avb-verity in dtbo.img
- [Liorsmagicboot] Fix typo in handling MTK boot image headers
- [script] Along with updates in Liorsmagic Manager, add support to sign boot images (AVB 1.0)
- [script] Add dtbo.img backup and restore support
- [misc] Many small adjustments to properly support old platforms like Android 5.0

### v14.3 (1437)

- [LiorsmagicBoot] Fix Pixel C installation
- [LiorsmagicBoot] Handle special `lz4_legacy` format properly, should fix all LG devices
- [Daemon] New universal logcat monitor is added, support plug-and-play to worker threads
- [Daemon] Invincible mode: daemon will be restarted by init, everything should seamlessly through daemon restarts
- [Daemon] Add new restorecon action, will go through and fix all Liorsmagic files with selinux unlabeled to `system_file` context
- [Daemon] Add brute-force image resizing mode, should prevent the notorious Samsung crappy resize2fs from affecting the result
- [resetprop] Add new "-p" flag, used to toggle whether alter/access the actual persist storage for persist props

### v14.2

- [MagicMount] Clone attributes to tmpfs mountpoint, should fix massive module breakage

### v14.1

- [LiorsmagicInit] Introduce a new init binary to support `skip_initramfs` devices (Pixel family)
- [script] Fix typo in update-binary for x86 devices
- [script] Fix stock boot image backup not moved to proper location
- [script] Add functions to support A/B slot and `skip_initramfs` devices
- [script] Detect Meizu boot blocks
- [LiorsmagicBoot] Add decompress zImage support
- [LiorsmagicBoot] Support extracting dtb appended to zImage block
- [LiorsmagicBoot] Support patching fstab within dtb
- [Daemon/LiorsmagicSU] Proper file based encryption support
- [Daemon] Create core folders if not exist
- [resetprop] Fix a bug which delete props won't remove persist props not in memory
- [MagicMount] Remove usage of dummy folder, directly mount tmpfs and construct file structure skeleton in place

### v14.0

- [script] Simplify installation scripts
- [script] Fix a bug causing backing up and restoring stock boot images failure
- [script] Installation and uninstallation will migrate old or broken stock boot image backups to proper format
- [script] Fix an issue with selabel setting in `util_functions.sh` on Lollipop
- [rc script] Enable logd in post-fs to start logging as early as possible
- [LiorsmagicHide] liorsmagic.img mounted is no longer a requirement
  Devices with issues mounting liorsmagic.img can now run in proper core-only mode
- [LiorsmagicBoot] Add native function to extract stock SHA1 from ramdisk
- [b64xz] New tool to extract compressed and encoded binary dumps in shell script
- [busybox] Add busybox to Liorsmagic source, and embed multi-arch busybox binary into update-binary shell script
- [busybox] Busybox is added into PATH for all boot scripts (post-fs-data.d, service.d, and all module scripts)
- [LiorsmagicSU] Fully fix multiuser issues
- [Magic Mount] Fix a typo in cloning attributes
- [Daemon] Fix the daemon crashing when boot scripts opens a subshell
- [Daemon] Adjustments to prevent stock Samsung kernel restrictions on exec system calls for binaries started from /data
- [Daemon] Workaround on Samsung device with weird fork behaviors

### v13.3

- [LiorsmagicHide] Update to bypass Google CTS (2017.7.17)
- [resetprop] Properly support removing persist props
- [uninstaller] Remove Liorsmagic Manager and persist props

### v13.2

- [liorsmagicpolicy] Fix liorsmagicpolicy segfault on old Android versions, should fix tons of older devices that couldn't use v13.1
- [LiorsmagicHide] Set proper selinux context while re-linking /sbin to hide Liorsmagic, should potentially fix many issues
- [LiorsmagicBoot] Change lzma compression encoder flag from `LZMA_CHECK_CRC64` to `LZMA_CHECK_CRC32`, kernel only supports latter
- [General] Core-only mode now properly mounts systemless hosts and liorsmagichide

### v13.1

- [General] Merge LiorsmagicSU, liorsmagichide, resetprop, liorsmagicpolicy into one binary
- [General] Add Android O support (tested on DP3)
- [General] Dynamic link libselinux.so, libsqlite.so from system to greatly reduce binary size
- [General] Remove bundled busybox because it causes a lot of issues
- [General] Unlock all block devices for read-write support instead of emmc only (just figured not all devices uses emmc lol)
- [Scripts] Run all ext4 image operations through liorsmagic binary in flash scripts
- [Scripts] Updated scripts to use liorsmagic native commands to increase compatibility
- [Scripts] Add addon.d survival support
- [Scripts] Introduce `util_functions.sh`, used as a global shell script function source for all kinds of installation
- [LiorsmagicBoot] Moved boot patch logic into liorsmagicboot binary
- [LiorsmagicSU] Does not fork new process for each request, add new threads instead
- [LiorsmagicSU] Added multiuser support
- [LiorsmagicSU] Introduce new timeout queue mechanism, prevent performance hit with poorly written su apps
- [LiorsmagicSU] Multiple settings moved from prop detection to database
- [LiorsmagicSU] Add namespace mode option support
- [LiorsmagicSU] Add master-mount option
- [resetprop] Updated to latest AOSP upstream, support props from 5.0 to Android O
- [resetprop] Renamed all functions to prevent calling functions from external libc
- [liorsmagicpolicy] Updated libsepol from official SELinux repo
- [liorsmagicpolicy] Added xperm patching support (in order to make Android O work properly)
- [liorsmagicpolicy] Updated rules for Android O, and Liveboot support
- [LiorsmagicHide] Remove pseudo permissive mode, directly hide permissive status instead
- [LiorsmagicHide] Remove unreliable list file monitor, change to daemon request mode
- [LiorsmagicHide] LiorsmagicHide is now enabled by default
- [LiorsmagicHide] Update unmount policies, passes CTS in SafetyNet!
- [LiorsmagicHide] Add more props for hiding
- [LiorsmagicHide] Remove background liorsmagichide daemon, spawn short life process for unmounting purpose
- [Magic Mount] Ditched shell script based mounting, use proper C program to parse and mount files. Speed is SIGNIFICANTLY improved

### v12.0

- [General] Move most binaries into liorsmagic.img (Samsung cannot run su daemon in /data)
- [General] Move sepolicy live patch to `late_start` service
  This shall fix the long boot times, especially on Samsung devices
- [General] Add Samsung RKP hexpatch back, should now work on Samsung stock kernels
- [General] Fix installation with SuperSU
- [LiorsmagicHide] Support other logcat `am_proc_start` patterns
- [LiorsmagicHide] Change /sys/fs/selinux/enforce(policy) permissions if required
  Samsung devices cannot switch selinux states, if running on permissive custom kernel, the users will stuck at permissive
  If this scenario is detected, change permissions to hide the permissive state, leads to SafetyNet passes
- [LiorsmagicHide] Add built in prop rules to fake KNOX status
  Samsung apps requiring KNOX status to be 0x0 should now work (Samsung Pay not tested)
- [LiorsmagicHide] Remove all ro.build props, since they cause more issues than they benefit...
- [LiorsmagicBoot] Add lz4 legacy format support (most linux kernel using lz4 for compression is using this)
- [LiorsmagicBoot] Fix MTK kernels with MTK headers

### v11.5/11.6

- [Magic Mount] Fix mounting issues with devices that have separate /vendor partitions
- [LiorsmagicBoot] Whole new boot image patching tool, please check release note for more info
- [liorsmagicpolicy] Rename sepolicy-inject to liorsmagicpolicy
- [liorsmagicpolicy] Update a rule to allow chcon everything properly
- [LiorsmagicHide] Prevent multirom crashes
- [LiorsmagicHide] Add patches for ro.debuggable, ro.secure, ro.build.type, ro.build.tags, ro.build.selinux
- [LiorsmagicHide] Change /sys/fs/selinux/enforce, /sys/fs/selinux/policy permissions for Samsung compatibility
- [LiorsmagicSU] Fix read-only partition mounting issues
- [LiorsmagicSU] Disable -cn option, the option will do nothing, preserved for compatibility

### v11.1

- [sepolicy-inject] Add missing messages
- [liorsmagichide] Start LiorsmagicHide with scripts

### v11.0

- [Magic Mount] Support replacing symlinks.
  Symlinks cannot be a target of a bind mounted, so they are treated the same as new files
- [Magic Mount] Fix the issue when file/folder name contains spaces
- [BusyBox] Updated to v1.26.2. Should fix the black screen issues of FlashFire
- [resetprop] Support reading prop files that contains spaces in prop values
- [LiorsmagicSU] Adapt communication to Liorsmagic Manager; stripped out unused data transfer
- [LiorsmagicSU] Implement SuperUser access option (Disable, APP only, ADB Only, APP & ADB)
  phh Superuser app has this option but the feature isn't implemented within the su binary
- [LiorsmagicSU] Fixed all issues with su -c "commands" (run commands with root)
  This feature is supposed to only allow one single option, but apparently adb shell su -c "command" doesn't work this way, and plenty of root apps don't follow the rule. The su binary will now consider everything after -c as a part of the command.
- [LiorsmagicSU] Removed legacy context hack for TiBack, what it currently does is slowing down the invocation
- [LiorsmagicSU] Preserve the current working directory after invoking su
  Previously phh superuser will change the path to /data/data after obtaining root shell. It will now stay in the same directory where you called su
- [LiorsmagicSU] Daemon now also runs in u:r:su:s0 context
- [LiorsmagicSU] Removed an unnecessary fork, reduce running processes and speed up the invocation
- [LiorsmagicSU] Add -cn option to the binary
  Not sure if this is still relevant, and also not sure if implemented correctly, but hey it's here
- [sepolicy-inject] Complete re-write the command-line options, now nearly matches supolicy syntax
- [sepolicy-inject] Support all matching mode for nearly every action (makes pseudo enforced possible)
- [sepolicy-inject] Fixed an ancient bug that allocated memory isn't reset
- [uninstaller] Now works as a independent script that can be executed at boot
  Fully support recovery with no /data access, Liorsmagic uninstallation with Liorsmagic Manager
- [Addition] Busybox, LiorsmagicHide, hosts settings can now be applied instantly; no reboots required
- [Addition] Add post-fs-data.d and service.d
- [Addition] Add option to disable Liorsmagic (LiorsmagicSU will still be started)

### v10.2

- [Magic Mount] Remove apps/priv-app from whitelist, should fix all crashes
- [phh] Fix binary out-of-date issue
- [scripts] Fix root disappear issue when upgrading within Liorsmagic Manager

### v10

- [Magic Mount] Use a new way to mount system (vendor) mirrors
- [Magic Mount] Use universal way to deal with /vendor, handle both separate partition or not
- [Magic Mount] Adding **anything to any place** is now officially supported (including /system root and /vendor root)
- [Magic Mount] Use symlinks for mirroring back if possible, reduce bind mounts for adding files
- [Liorsmagic Hide] Check init namespace, zygote namespace to prevent Magic Mount breakage (a.k.a root loss)
- [Liorsmagic Hide] Send SIGSTOP to pause target process ASAP to prevent crashing if unmounting too late
- [Liorsmagic Hide] Hiding should work under any conditions, including adding libs and /system root etc.
- [phh] Root the device if no proper root detected
- [phh] Move `/sbin` to `/sbin_orig` and link back, fix Samsung no-suid issue
- [scripts] Improve SuperSU integration, now uses sukernel to patch ramdisk, support SuperSU built in ramdisk restore
- [template] Add PROPFILE option to load system.prop

### v9

- **[API Change] Remove the interface for post-fs modules**
- [resetprop] New tool "resetprop" is added to Liorsmagic to replace most post-fs modules' functionality
- [resetprop] Liorsmagic will now patch "ro.boot.verifiedbootstate", "ro.boot.flash.locked", "ro.boot.veritymode" to bypass Safety Net
- [Magic Mount] Move dummy skeleton / mirror / mountinfo filesystem tree to tmpfs
- [Magic Mount] Rewritten dummy cloning mechanism from scratch, will result in minimal bind mounts, minimal file traversal, eliminate all possible issues that might happen in extreme cases
- [Magic Mount] Adding new items to /system/bin, /system/vendor, /system/lib(64) is properly supported (devices with separate vendor partition is not supported yet)
- [Liorsmagic Hide] Rewritten from scratch, now run in daemon mode, proper list monitoring, proper mount detection, and maybe more.....
- [Boot Image] Add support for Motorola boot image dtb, it shall now unpack correctly
- [Uninstaller] Add removal of SuperSU custom patch script

### v8

- Add Liorsmagic Hide to bypass SafetyNet
- Improve SuperSU integration: no longer changes the SuperSU PATH
- Support rc script entry points not located in init.rc

### v7

- Fully open source
- Remove supolicy dependency, use my own sepolicy-injection
- Run everything in its own selinux domain, should fix all selinux issues
- Add Note 7 stock kernel hex patches
- Add support to install Liorsmagic in Liorsmagic Manager
- Add support for image merging for module flashing in Liorsmagic Manager
- Add root helpers for SuperSU auto module-ize and auto upgrading legacy phh superuser
- New paths to toggle busybox, and support all root solutions
- Remove root management API; both SuperSU and phh has their own superior solutions

### v6

- Fixed the algorithm for adding new files and dummy system
- Updated the module template with a default permission, since people tend to forget them :)

### v5

- Hotfix for older Android versions (detect policy before patching)
- Update uninstaller to NOT uninstall Liorsmagic Manager, since it cause problems

### v4

- Important: Uninstall v1 - v3 Liorsmagic before upgrading with the uninstaller in the OP!!
- Massive Rewrite Liorsmagic Interface API! All previous mods are NOT compatible! Please download the latest version of the mods you use (root/xposed)
- Mods are now installed independently in their own subfolder. This paves the way for future Liorsmagic Manager versions to manage mods, **just like how Xposed Modules are handled**
- Support small boot partition devices (Huawei devices)
- Use minimal sepolicy patch in boot image for smaller ramdisk size. Live patch policies after bootup
- Include updated open source sepolicy injection tool (source code available), support nearly all SuperSU supolicy tool's functionality

### v3

- Fix bootimg-extract for Exynos Samsung devices (thanks to @phhusson), should fix all Samsung device issues
- Add supolicy back to patch sepolicy (stock Samsung do not accept permissive domain)
- Update sepolicy-injection to patch su domain for Samsung devices to use phh's root
- Update root disable method, using more aggressive approach
- Use lazy unmount to unmount root from system, should fix some issues with custom roms
- Use the highest possible compression rate for ramdisk, hope to fix some devices with no boot partition space
- Detect boot partition space insufficient, will abort installer instead of breaking your device

### v2

- Fix verity patch. It should now work on all devices (might fix some of the unable-to-boot issues)
- All scripts will now run in selinux permissive mode for maximum compatibility (this will **NOT** turn your device to permissive)
- Add Nougat Developer Preview 5 support
- Add systemless host support for AdBlock Apps (enabled by default)
- Add support for new root disable method
- Remove sepolicy patches that uses SuperSU's supolicy tool; it is now using a minimal set of modifications
- Removed Liorsmagic Manager in Liorsmagic patch, it is now included in Liorsmagic phh's superuser only

### v1

- Initial release
