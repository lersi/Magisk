# Liorsmagic Tools

Liorsmagic comes with a huge collections of tools for installation, daemons, and utilities for developers. This documentation covers the 4 binaries and all included applets. The binaries and applets are shown below:

```
liorsmagicboot                 /* binary */
liorsmagicinit                 /* binary */
liorsmagicpolicy               /* binary */
supolicy -> liorsmagicpolicy
liorsmagic                     /* binary */
resetprop -> liorsmagic
su -> liorsmagic
```

### liorsmagicboot

A tool to unpack / repack boot images, parse / patch / extract cpio, patch dtb, hex patch binaries, and compress / decompress files with multiple algorithms.

`liorsmagicboot` natively supports (which means it does not rely on external tools) common compression formats including `gzip`, `lz4`, `lz4_legacy` ([only used on LG](https://events.static.linuxfound.org/sites/events/files/lcjpcojp13_klee.pdf)), `lzma`, `xz`, and `bzip2`.

The concept of `liorsmagicboot` is to make boot image modification simpler. For unpacking, it parses the header and extracts all sections in the image, decompressing on-the-fly if compression is detected in any sections. For repacking, the original boot image is required so the original headers can be used, changing only the necessary entries such as section sizes and checksum. All sections will be compressed back to the original format if required. The tool also supports many CPIO and DTB operations.

```
Usage: ./liorsmagicboot <action> [args...]

Supported actions:
  unpack [-n] [-h] <bootimg>
    Unpack <bootimg> to its individual components, each component to
    a file with its corresponding file name in the current directory.
    Supported components: kernel, kernel_dtb, ramdisk.cpio, second,
    dtb, extra, and recovery_dtbo.
    By default, each component will be automatically decompressed
    on-the-fly before writing to the output file.
    If '-n' is provided, all decompression operations will be skipped;
    each component will remain untouched, dumped in its original format.
    If '-h' is provided, the boot image header information will be
    dumped to the file 'header', which can be used to modify header
    configurations during repacking.
    Return values:
    0:valid    1:error    2:chromeos

  repack [-n] <origbootimg> [outbootimg]
    Repack boot image components using files from the current directory
    to [outbootimg], or 'new-boot.img' if not specified.
    <origbootimg> is the original boot image used to unpack the components.
    By default, each component will be automatically compressed using its
    corresponding format detected in <origbootimg>. If a component file
    in the current directory is already compressed, then no addition
    compression will be performed for that specific component.
    If '-n' is provided, all compression operations will be skipped.
    If env variable PATCHVBMETAFLAG is set to true, all disable flags in
    the boot image's vbmeta header will be set.

  hexpatch <file> <hexpattern1> <hexpattern2>
    Search <hexpattern1> in <file>, and replace it with <hexpattern2>

  cpio <incpio> [commands...]
    Do cpio commands to <incpio> (modifications are done in-place)
    Each command is a single argument, add quotes for each command.
    Supported commands:
      exists ENTRY
        Return 0 if ENTRY exists, else return 1
      rm [-r] ENTRY
        Remove ENTRY, specify [-r] to remove recursively
      mkdir MODE ENTRY
        Create directory ENTRY in permissions MODE
      ln TARGET ENTRY
        Create a symlink to TARGET with the name ENTRY
      mv SOURCE DEST
        Move SOURCE to DEST
      add MODE ENTRY INFILE
        Add INFILE as ENTRY in permissions MODE; replaces ENTRY if exists
      extract [ENTRY OUT]
        Extract ENTRY to OUT, or extract all entries to current directory
      test
        Test the cpio's status
        Return value is 0 or bitwise or-ed of following values:
        0x1:Liorsmagic    0x2:unsupported    0x4:Sony
      patch
        Apply ramdisk patches
        Configure with env variables: KEEPVERITY KEEPFORCEENCRYPT
      backup ORIG
        Create ramdisk backups from ORIG
      restore
        Restore ramdisk from ramdisk backup stored within incpio
      sha1
        Print stock boot SHA1 if previously backed up in ramdisk

  dtb <file> <action> [args...]
    Do dtb related actions to <file>
    Supported actions:
      print [-f]
        Print all contents of dtb for debugging
        Specify [-f] to only print fstab nodes
      patch
        Search for fstab and remove verity/avb
        Modifications are done directly to the file in-place
        Configure with env variables: KEEPVERITY
      test
        Test the fstab's status
        Return values:
        0:valid    1:error

  split <file>
    Split image.*-dtb into kernel + kernel_dtb

  sha1 <file>
    Print the SHA1 checksum for <file>

  cleanup
    Cleanup the current working directory

  compress[=format] <infile> [outfile]
    Compress <infile> with [format] to [outfile].
    <infile>/[outfile] can be '-' to be STDIN/STDOUT.
    If [format] is not specified, then gzip will be used.
    If [outfile] is not specified, then <infile> will be replaced
    with another file suffixed with a matching file extension.
    Supported formats: gzip zopfli xz lzma bzip2 lz4 lz4_legacy lz4_lg 

  decompress <infile> [outfile]
    Detect format and decompress <infile> to [outfile].
    <infile>/[outfile] can be '-' to be STDIN/STDOUT.
    If [outfile] is not specified, then <infile> will be replaced
    with another file removing its archive format file extension.
    Supported formats: gzip zopfli xz lzma bzip2 lz4 lz4_legacy lz4_lg 
```

### liorsmagicinit

This binary will replace `init` in the ramdisk of a Liorsmagic patched boot image. It is originally created for supporting devices using system-as-root, but the tool is extended to support all devices and became a crucial part of Liorsmagic. More details can be found in the **Pre-Init** section in [Liorsmagic Booting Process](details.md#liorsmagic-booting-process).

### liorsmagicpolicy

(This tool is aliased to `supolicy` for compatibility with SuperSU's sepolicy tool)

This tool could be used for advanced developers to modify SELinux policies. In common scenarios like Linux server admins, they would directly modify the SELinux policy sources (`*.te`) and recompile the `sepolicy` binary, but here on Android we directly patch the binary file (or runtime policies).

All processes spawned from the Liorsmagic daemon, including root shells and all its forks, are running in the context `u:r:liorsmagic:s0`. The rule used on all Liorsmagic installed systems can be viewed as stock `sepolicy` with these patches: `liorsmagicpolicy --liorsmagic 'allow liorsmagic * * *'`.

```
Usage: ./liorsmagicpolicy [--options...] [policy statements...]

Options:
   --help            show help message for policy statements
   --load FILE       load monolithic sepolicy from FILE
   --load-split      load from precompiled sepolicy or compile
                     split cil policies
   --compile-split   compile split cil policies
   --save FILE       dump monolithic sepolicy to FILE
   --live            immediately load sepolicy into the kernel
   --liorsmagic          apply built-in Liorsmagic sepolicy rules
   --apply FILE      apply rules from FILE, read and parsed
                     line by line as policy statements
                     (multiple --apply are allowed)

If neither --load, --load-split, nor --compile-split is specified,
it will load from current live policies (/sys/fs/selinux/policy)

One policy statement should be treated as one parameter;
this means each policy statement should be enclosed in quotes.
Multiple policy statements can be provided in a single command.

Statements has a format of "<rule_name> [args...]".
Arguments labeled with (^) can accept one or more entries. Multiple
entries consist of a space separated list enclosed in braces ({}).
Arguments labeled with (*) are the same as (^), but additionally
support the match-all operator (*).

Example: "allow { s1 s2 } { t1 t2 } class *"
Will be expanded to:

allow s1 t1 class { all-permissions-of-class }
allow s1 t2 class { all-permissions-of-class }
allow s2 t1 class { all-permissions-of-class }
allow s2 t2 class { all-permissions-of-class }

Supported policy statements:

"allow *source_type *target_type *class *perm_set"
"deny *source_type *target_type *class *perm_set"
"auditallow *source_type *target_type *class *perm_set"
"dontaudit *source_type *target_type *class *perm_set"

"allowxperm *source_type *target_type *class operation xperm_set"
"auditallowxperm *source_type *target_type *class operation xperm_set"
"dontauditxperm *source_type *target_type *class operation xperm_set"
- The only supported operation is 'ioctl'
- xperm_set format is either 'low-high', 'value', or '*'.
  '*' will be treated as '0x0000-0xFFFF'.
  All values should be written in hexadecimal.

"permissive ^type"
"enforce ^type"

"typeattribute ^type ^attribute"

"type type_name ^(attribute)"
- Argument 'attribute' is optional, default to 'domain'

"attribute attribute_name"

"type_transition source_type target_type class default_type (object_name)"
- Argument 'object_name' is optional

"type_change source_type target_type class default_type"
"type_member source_type target_type class default_type"

"genfscon fs_name partial_path fs_context"
```

### liorsmagic

When the liorsmagic binary is called with the name `liorsmagic`, it works as a utility tool with many helper functions and the entry points for several Liorsmagic services.

```
Usage: liorsmagic [applet [arguments]...]
   or: liorsmagic [options]...

Options:
   -c                        print current binary version
   -v                        print running daemon version
   -V                        print running daemon version code
   --list                    list all available applets
   --remove-modules          remove all modules and reboot
   --install-module ZIP      install a module zip file

Advanced Options (Internal APIs):
   --daemon                  manually start liorsmagic daemon
   --stop                    remove all liorsmagic changes and stop daemon
   --[init trigger]          callback on init triggers. Valid triggers:
                             post-fs-data, service, boot-complete, zygote-restart
   --unlock-blocks           set BLKROSET flag to OFF for all block devices
   --restorecon              restore selinux context on Liorsmagic files
   --clone-attr SRC DEST     clone permission, owner, and selinux context
   --clone SRC DEST          clone SRC to DEST
   --sqlite SQL              exec SQL commands to Liorsmagic database
   --path                    print Liorsmagic tmpfs mount path
   --denylist ARGS           denylist config CLI

Available applets:
    su, resetprop

Usage: liorsmagic --denylist [action [arguments...] ]
Actions:
   status          Return the enforcement status
   enable          Enable denylist enforcement
   disable         Disable denylist enforcement
   add PKG [PROC]  Add a new target to the denylist
   rm PKG [PROC]   Remove target(s) from the denylist
   ls              Print the current denylist
   exec CMDs...    Execute commands in isolated mount
                   namespace and do all unmounts
```

### su

An applet of `liorsmagic`, the LiorsmagicSU entry point. Good old `su` command.

```
Usage: su [options] [-] [user [argument...]]

Options:
  -c, --command COMMAND         pass COMMAND to the invoked shell
  -h, --help                    display this help message and exit
  -, -l, --login                pretend the shell to be a login shell
  -m, -p,
  --preserve-environment        preserve the entire environment
  -s, --shell SHELL             use SHELL instead of the default /system/bin/sh
  -v, --version                 display version number and exit
  -V                            display version code and exit
  -mm, -M,
  --mount-master                force run in the global mount namespace
```

Note: even though the `-Z, --context` option is not listed above, the option still exists for CLI compatibility with apps designed for SuperSU. However the option is silently ignored since it's no longer relevant.

### resetprop

An applet of `liorsmagic`. An advanced system property manipulation utility. Check the [Resetprop Details](details.md#resetprop) for more background information.

```
Usage: resetprop [flags] [options...]

Options:
   -h, --help        show this message
   (no arguments)    print all properties
   NAME              get property
   NAME VALUE        set property entry NAME with VALUE
   --file FILE       load props from FILE
   --delete NAME     delete property

Flags:
   -v      print verbose output to stderr
   -n      set props without going through property_service
           (this flag only affects setprop)
   -p      read/write props from/to persistent storage
           (this flag only affects getprop and delprop)
```
