#####################################################################
#   AVD LiorsmagicInit Setup
#####################################################################
#
# Support API level: 23 - 33
#
# With an emulator booted and accessible via ADB, usage:
# ./build.py avd_patch path/to/booted/avd-image/ramdisk.img
#
# The purpose of this script is to patch AVD ramdisk.img and do a
# full integration test of liorsmagicinit under several circumstances.
# After patching ramdisk.img, close the emulator, then select
# "Cold Boot Now" in AVD Manager to force a full reboot.
#
#####################################################################
# AVD Init Configurations:
#
# rootfs w/o early mount: API 23 - 25
# rootfs with early mount: API 26 - 27
# Legacy system-as-root: API 28
# 2 stage init: API 29 - 33
#####################################################################

if [ ! -f /system/build.prop ]; then
  # Running on PC
  echo 'Please run `./build.py avd_patch` instead of directly executing the script!'
  exit 1
fi

cd /data/local/tmp
chmod 755 busybox

if [ -z "$FIRST_STAGE" ]; then
  export FIRST_STAGE=1
  export ASH_STANDALONE=1
  # Re-exec script with busybox
  exec ./busybox sh $0
fi

# Extract files from APK
unzip -oj liorsmagic.apk 'assets/util_functions.sh' 'assets/stub.apk'
. ./util_functions.sh

api_level_arch_detect

unzip -oj liorsmagic.apk "lib/$ABI/*" "lib/$ABI32/libliorsmagic32.so" -x "lib/$ABI/libbusybox.so"
for file in lib*.so; do
  chmod 755 $file
  mv "$file" "${file:3:${#file}-6}"
done

./liorsmagicboot decompress ramdisk.cpio.tmp ramdisk.cpio
cp ramdisk.cpio ramdisk.cpio.orig

export KEEPVERITY=true
export KEEPFORCEENCRYPT=true

echo "KEEPVERITY=$KEEPVERITY" > config
echo "KEEPFORCEENCRYPT=$KEEPFORCEENCRYPT" >> config
if [ -f liorsmagic64 ]; then
  echo "PREINITDEVICE=$(./liorsmagic64 --preinit-device)" >> config
else
  echo "PREINITDEVICE=$(./liorsmagic32 --preinit-device)" >> config
fi
# For API 28, we also patch advancedFeatures.ini to disable SAR
# Manually override skip_initramfs by setting RECOVERYMODE=true
[ $API = "28" ] && echo 'RECOVERYMODE=true' >> config
cat config

SKIP32="#"
SKIP64="#"
if [ -f liorsmagic64 ]; then
  ./liorsmagicboot compress=xz liorsmagic64 liorsmagic64.xz
  unset SKIP64
fi
if [ -e "/system/bin/linker" ]; then
  ./liorsmagicboot compress=xz liorsmagic32 liorsmagic32.xz
  unset SKIP32
fi
./liorsmagicboot compress=xz stub.apk stub.xz

./liorsmagicboot cpio ramdisk.cpio \
"add 0750 init liorsmagicinit" \
"mkdir 0750 overlay.d" \
"mkdir 0750 overlay.d/sbin" \
"$SKIP32 add 0644 overlay.d/sbin/liorsmagic32.xz liorsmagic32.xz" \
"$SKIP64 add 0644 overlay.d/sbin/liorsmagic64.xz liorsmagic64.xz" \
"add 0644 overlay.d/sbin/stub.xz stub.xz" \
"patch" \
"backup ramdisk.cpio.orig" \
"mkdir 000 .backup" \
"add 000 .backup/.liorsmagic config"

rm -f ramdisk.cpio.orig config liorsmagic*.xz stub.xz
./liorsmagicboot compress=gzip ramdisk.cpio ramdisk.cpio.gz
