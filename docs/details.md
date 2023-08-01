# Internal Details

## File Structure

### Paths in "Liorsmagic tmpfs directory"

Liorsmagic will mount a `tmpfs` directory to store some temporary data. For devices with the `/liorsbin` folder, it will be chosen as it will also act as an overlay to inject binaries into `PATH`. From Android 11 onwards, the `/liorsbin` folder might not exist, so Liorsmagic will randomly create a folder under `/dev` and use it as the base folder.

```
# In order to get the current base folder Liorsmagic is using,
# use the command `liorsmagic --path`.
# Binaries like liorsmagic, liorsmagicinit, and all symlinks to
# applets are directly stored in this path. This means when
# this is /liorsbin, these binaries will be directly in PATH.
LIORSMAGICTMP=$(liorsmagic --path)

# Liorsmagic internal stuffs
INTERNALDIR=$LIORSMAGICTMP/.liorsmagic

# /data/adb/modules will be bind mounted here.
# The original folder is not used due to nosuid mount flag.
$INTERNALDIR/modules

# The current Liorsmagic installation config
$INTERNALDIR/config

# Partition mirrors
# Each directory in this path will be mounted with the
# partition of its directory name.
# e.g. system, system_ext, vendor, data ...
$INTERNALDIR/mirror

# Root directory patch files
# On system-as-root devices, / is not writable.
# All pre-init patched files are stored here and bind mounted.
$INTERNALDIR/rootdir
```

### Paths in `/data`

Some binaries and files should be stored on non-volatile storages in `/data`. In order to prevent detection, everything has to be stored somewhere safe and undetectable in `/data`. The folder `/data/adb` was chosen because of the following advantages:

- It is an existing folder on modern Android, so it cannot be used as an indication of the existence of Liorsmagic.
- The permission of the folder is by default `700`, owner as `root`, so non-root processes are unable to enter, read, write the folder in any possible way.
- The folder is labeled with secontext `u:object_r:adb_data_file:s0`, and very few processes have the permission to do any interaction with that secontext.
- The folder is located in _Device encrypted storage_, so it is accessible as soon as data is properly mounted in FBE (File-Based Encryption) devices.

```
SECURE_DIR=/data/adb

# Folder storing general post-fs-data scripts
$SECURE_DIR/post-fs-data.d

# Folder storing general late_start service scripts
$SECURE_DIR/service.d

# Liorsmagic modules
$SECURE_DIR/modules

# Liorsmagic modules that are pending for upgrade
# Module files are not safe to be modified when mounted
# Modules installed through the Liorsmagic app will be stored here
# and will be merged into $SECURE_DIR/modules in the next reboot
$SECURE_DIR/modules_update

# Database storing settings and root permissions
LIORSMAGICDB=$SECURE_DIR/liorsmagic.db

# All liorsmagic related binaries, including busybox,
# scripts, and liorsmagic binaries. Used in supporting
# module installation, addon.d, the Liorsmagic app etc.
DATABIN=$SECURE_DIR/liorsmagic

```

## Liorsmagic Booting Process

### Pre-Init

`liorsmagicinit` will replace `init` as the first program to run.

- Early mount required partitions. On legacy system-as-root devices, we switch root to system; on 2SI devices, we patch the original `init` to redirect the 2nd stage init file to liorsmagicinit and execute it to mount partitions for us.
- Inject liorsmagic services into `init.rc`
- On devices using monolithic policy, load sepolicy from `/sepolicy`; otherwise we hijack nodes in selinuxfs with FIFO, set `LD_PRELOAD` to hook `security_load_policy` and assist hijacking on 2SI devices, and start a daemon to wait until init tries to load sepolicy.
- Patch sepolicy rules. If we are using "hijack" method, load patched sepolicy into kernel, unblock init and exit daemon
- Execute the original `init` to continue the boot process

### post-fs-data

This triggers on `post-fs-data` when `/data` is decrypted and mounted. The daemon `liorsmagicd` will be launched, post-fs-data scripts are executed, and module files are magic mounted.

### late_start

Later in the booting process, the class `late_start` will be triggered, and Liorsmagic "service" mode will be started. In this mode, service scripts are executed.

## Resetprop

Usually, system properties are designed to only be updated by `init` and read-only to non-root processes. With root you can change properties by sending requests to `property_service` (hosted by `init`) using commands such as `setprop`, but changing read-only props (props that start with `ro.` like `ro.build.product`) and deleting properties are still prohibited.

`resetprop` is implemented by distilling out the source code related to system properties from AOSP and patched to allow direct modification to property area, or `prop_area`, bypassing the need to go through `property_service`. Since we are bypassing `property_service`, there are a few caveats:

- `on property:foo=bar` actions registered in `*.rc` scripts will not be triggered if property changes does not go through `property_service`. The default set property behavior of `resetprop` matches `setprop`, which **WILL** trigger events (implemented by first deleting the property then set it via `property_service`). There is a flag `-n` to disable it if you need this special behavior.
- persist properties (props that starts with `persist.`, like `persist.sys.usb.config`) are stored in both `prop_area` and `/data/property`. By default, deleting props will **NOT** remove it from persistent storage, meaning the property will be restored after the next reboot; reading props will **NOT** read from persistent storage, as this is the behavior of `getprop`. With the flag `-p`, deleting props will remove the prop in **BOTH** `prop_area` and `/data/property`, and reading props will be read from **BOTH** `prop_area` and persistent storage.

## SELinux Policies

Liorsmagic will patch the stock `sepolicy` to make sure root and Liorsmagic operations can be done in a safe and secure way. The new domain `liorsmagic` is effectively permissive, which is what `liorsmagicd` and all root shell will run in. `liorsmagic_file` is a new file type that is setup to be allowed to be accessed by every domain (unrestricted file context).

Before Android 8.0, all allowed su client domains are allowed to directly connect to `liorsmagicd` and establish connection with the daemon to get a remote root shell. Liorsmagic also have to relax some `ioctl` operations so root shells can function properly.

After Android 8.0, to reduce relaxation of rules in Android's sandbox, a new SELinux model is deployed. The `liorsmagic` binary is labelled with `liorsmagic_exec` file type, and processes running as allowed su client domains executing the `liorsmagic` binary (this includes the `su` command) will transit to `liorsmagic_client` by using a `type_transition` rule. Rules strictly restrict that only `liorsmagic` domain processes are allowed to attribute files to `liorsmagic_exec`. Direct connection to sockets of `liorsmagicd` are not allowed; the only way to access the daemon is through a `liorsmagic_client` process. These changes allow us to keep the sandbox intact, and keep Liorsmagic specific rules separated from the rest of the policies.

The full set of rules can be found in `liorsmagicpolicy/rules.cpp`.
