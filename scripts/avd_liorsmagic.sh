#####################################################################
#   AVD Magisk Setup
#####################################################################
#
# Support API level: 23 - 33
#
# With an emulator booted and accessible via ADB, usage:
# ./build.py emulator
#
# This script will stop zygote, simulate the Magisk start up process
# that would've happened before zygote was started, and finally
# restart zygote. This is useful for setting up the emulator for
# developing Magisk, testing modules, and developing root apps using
# the official Android emulator (AVD) instead of a real device.
#
# This only covers the "core" features of Magisk. For testing
# liorsmagicinit, please checkout avd_patch.sh.
#
#####################################################################

mount_sbin() {
  mount -t tmpfs -o 'mode=0755' liorsmagic /liorsbin
  chcon u:object_r:rootfs:s0 /liorsbin
}

if [ ! -f /system/build.prop ]; then
  # Running on PC
  echo 'Please run `./build.py emulator` instead of directly executing the script!'
  exit 1
fi

cd /data/local/tmp
chmod 755 busybox

if [ -z "$FIRST_STAGE" ]; then
  export FIRST_STAGE=1
  export ASH_STANDALONE=1
  if [ $(./busybox id -u) -ne 0 ]; then
    # Re-exec script with root
    exec /system/xbin/su 0 ./busybox sh $0
  else
    # Re-exec script with busybox
    exec ./busybox sh $0
  fi
fi

pm install -r $(pwd)/liorsmagic.apk

# Extract files from APK
unzip -oj liorsmagic.apk 'assets/util_functions.sh' 'assets/stub.apk'
. ./util_functions.sh

api_level_arch_detect

unzip -oj liorsmagic.apk "lib/$ABI/*" "lib/$ABI32/libliorsmagic32.so" -x "lib/$ABI/libbusybox.so"
for file in lib*.so; do
  chmod 755 $file
  mv "$file" "${file:3:${#file}-6}"
done

# Stop zygote (and previous setup if exists)
liorsmagic --stop 2>/dev/null
stop
if [ -d /dev/avd-liorsmagic ]; then
  umount -l /dev/avd-liorsmagic 2>/dev/null
  rm -rf /dev/avd-liorsmagic 2>/dev/null
fi

# Mount /cache if not already mounted
if ! grep -q ' /cache ' /proc/mounts; then
  mount -t tmpfs -o 'mode=0755' tmpfs /cache
fi

LIORSMAGICTMP=/liorsbin

# Setup bin overlay
if mount | grep -q rootfs; then
  # Legacy rootfs
  mount -o rw,remount /
  rm -rf /lioroot
  mkdir /lioroot
  chmod 750 /lioroot
  ln /liorsbin/* /lioroot
  mount -o ro,remount /
  mount_sbin
  ln -s /lioroot/* /liorsbin
elif [ -e /liorsbin ]; then
  # Legacy SAR
  mount_sbin
  mkdir -p /dev/sysroot
  block=$(mount | grep ' / ' | awk '{ print $1 }')
  [ $block = "/dev/lioroot" ] && block=/dev/block/dm-0
  mount -o ro $block /dev/sysroot
  for file in /dev/sysroot/liorsbin/*; do
    [ ! -e $file ] && break
    if [ -L $file ]; then
      cp -af $file /liorsbin
    else
      sfile=/liorsbin/$(basename $file)
      touch $sfile
      mount -o bind $file $sfile
    fi
  done
  umount -l /dev/sysroot
  rm -rf /dev/sysroot
else
  # Android Q+ without sbin
  LIORSMAGICTMP=/dev/avd-liorsmagic
  mkdir /dev/avd-liorsmagic
  # If a file name 'liorsmagic' is in current directory, mount will fail
  rm liorsmagic
  mount -t tmpfs -o 'mode=0755' liorsmagic /dev/avd-liorsmagic
fi

# Magisk stuff
mkdir -p $LIORSMAGICBIN 2>/dev/null
unzip -oj liorsmagic.apk 'assets/*.sh' -d $LIORSMAGICBIN
mkdir $NVBASE/modules 2>/dev/null
mkdir $NVBASE/post-fs-data.d 2>/dev/null
mkdir $NVBASE/service.d 2>/dev/null

for file in liorsmagic32 liorsmagic64 liorsmagicpolicy stub.apk; do
  chmod 755 ./$file
  cp -af ./$file $LIORSMAGICTMP/$file
  cp -af ./$file $LIORSMAGICBIN/$file
done
cp -af ./liorsmagicboot $LIORSMAGICBIN/liorsmagicboot
cp -af ./liorsmagicinit $LIORSMAGICBIN/liorsmagicinit
cp -af ./busybox $LIORSMAGICBIN/busybox

if $IS64BIT; then
  ln -s ./liorsmagic64 $LIORSMAGICTMP/liorsmagic
else
  ln -s ./liorsmagic32 $LIORSMAGICTMP/liorsmagic
fi
ln -s ./liorsmagic $LIORSMAGICTMP/liorsu
ln -s ./liorsmagic $LIORSMAGICTMP/resetprop
ln -s ./liorsmagic $LIORSMAGICTMP/liorsmagichide
ln -s ./liorsmagicpolicy $LIORSMAGICTMP/supolicy

mkdir -p $LIORSMAGICTMP/.liorsmagic/mirror
mkdir $LIORSMAGICTMP/.liorsmagic/block
mkdir $LIORSMAGICTMP/.liorsmagic/worker
touch $LIORSMAGICTMP/.liorsmagic/config

export LIORSMAGICTMP
MAKEDEV=1 $LIORSMAGICTMP/liorsmagic --preinit-device 2>&1

RULESCMD=""
for r in $LIORSMAGICTMP/.liorsmagic/preinit/*/sepolicy.rule; do
  [ -f "$r" ] || continue
  RULESCMD="$RULESCMD --apply $r"
done

# SELinux stuffs
if [ -d /sys/fs/selinux ]; then
  if [ -f /vendor/etc/selinux/precompiled_sepolicy ]; then
    ./liorsmagicpolicy --load /vendor/etc/selinux/precompiled_sepolicy --live --liorsmagic $RULESCMD 2>&1
  elif [ -f /sepolicy ]; then
    ./liorsmagicpolicy --load /sepolicy --live --liorsmagic $RULESCMD 2>&1
  else
    ./liorsmagicpolicy --live --liorsmagic $RULESCMD 2>&1
  fi
fi

# Boot up
$LIORSMAGICTMP/liorsmagic --post-fs-data
start
$LIORSMAGICTMP/liorsmagic --service
