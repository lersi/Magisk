#LIORSMAGIC
############################################
# Liorsmagic Uninstaller (updater-script)
############################################

##############
# Preparation
##############

# Default permissions
umask 022

OUTFD=$2
COMMONDIR=$INSTALLER/assets
CHROMEDIR=$INSTALLER/assets/chromeos

if [ ! -f $COMMONDIR/util_functions.sh ]; then
  echo "! Unable to extract zip file!"
  exit 1
fi

# Load utility functions
. $COMMONDIR/util_functions.sh

setup_flashable

############
# Detection
############

if echo $LIORSMAGIC_VER | grep -q '\.'; then
  PRETTY_VER=$LIORSMAGIC_VER
else
  PRETTY_VER="$LIORSMAGIC_VER($LIORSMAGIC_VER_CODE)"
fi
print_title "Liorsmagic $PRETTY_VER Uninstaller"

is_mounted /data || mount /data || abort "! Unable to mount /data, please uninstall with the Liorsmagic app"
mount_partitions
check_data
$DATA_DE || abort "! Cannot access /data, please uninstall with the Liorsmagic app"
get_flags
find_boot_image

[ -z $BOOTIMAGE ] && abort "! Unable to detect target image"
ui_print "- Target image: $BOOTIMAGE"

# Detect version and architecture
api_level_arch_detect

ui_print "- Device platform: $ABI"

BINDIR=$INSTALLER/lib/$ABI
cd $BINDIR
for file in lib*.so; do mv "$file" "${file:3:${#file}-6}"; done
cd /
cp -af $CHROMEDIR/. $BINDIR/chromeos
chmod -R 755 $BINDIR

############
# Uninstall
############

cd $BINDIR

CHROMEOS=false

ui_print "- Unpacking boot image"
# Dump image for MTD/NAND character device boot partitions
if [ -c $BOOTIMAGE ]; then
  nanddump -f boot.img $BOOTIMAGE
  BOOTNAND=$BOOTIMAGE
  BOOTIMAGE=boot.img
fi
./liorsmagicboot unpack "$BOOTIMAGE"

case $? in
  1 )
    abort "! Unsupported/Unknown image format"
    ;;
  2 )
    ui_print "- ChromeOS boot image detected"
    CHROMEOS=true
    ;;
esac

# Restore the original boot partition path
[ "$BOOTNAND" ] && BOOTIMAGE=$BOOTNAND

# Detect boot image state
ui_print "- Checking ramdisk status"
if [ -e ramdisk.cpio ]; then
  ./liorsmagicboot cpio ramdisk.cpio test
  STATUS=$?
else
  # Stock A only system-as-root
  STATUS=0
fi
case $((STATUS & 3)) in
  0 )  # Stock boot
    ui_print "- Stock boot image detected"
    ;;
  1 )  # Liorsmagic patched
    ui_print "- Liorsmagic patched image detected"
    # Find SHA1 of stock boot image
    ./liorsmagicboot cpio ramdisk.cpio "extract .backup/.liorsmagic config.orig"
    if [ -f config.orig ]; then
      chmod 0644 config.orig
      SHA1=$(grep_prop SHA1 config.orig)
      rm config.orig
    fi
    BACKUPDIR=/data/liorsmagic_backup_$SHA1
    if [ -d $BACKUPDIR ]; then
      ui_print "- Restoring stock boot image"
      flash_image $BACKUPDIR/boot.img.gz $BOOTIMAGE
      for name in dtb dtbo dtbs; do
        [ -f $BACKUPDIR/${name}.img.gz ] || continue
        IMAGE=$(find_block $name$SLOT)
        [ -z $IMAGE ] && continue
        ui_print "- Restoring stock $name image"
        flash_image $BACKUPDIR/${name}.img.gz $IMAGE
      done
    else
      ui_print "! Boot image backup unavailable"
      ui_print "- Restoring ramdisk with internal backup"
      ./liorsmagicboot cpio ramdisk.cpio restore
      if ! ./liorsmagicboot cpio ramdisk.cpio "exists init"; then
        # A only system-as-root
        rm -f ramdisk.cpio
      fi
      ./liorsmagicboot repack $BOOTIMAGE
      # Sign chromeos boot
      $CHROMEOS && sign_chromeos
      ui_print "- Flashing restored boot image"
      flash_image new-boot.img $BOOTIMAGE || abort "! Insufficient partition size"
    fi
    ;;
  2 )  # Unsupported
    ui_print "! Boot image patched by unsupported programs"
    abort "! Cannot uninstall"
    ;;
esac

if $BOOTMODE; then
  ui_print "- Removing modules"
  liorsmagic --remove-modules -n
fi

ui_print "- Removing Liorsmagic files"
rm -rf \
/cache/*liorsmagic* /cache/unblock /data/*liorsmagic* /data/cache/*liorsmagic* /data/property/*liorsmagic* \
/data/Liorsmagic.apk /data/busybox /data/custom_ramdisk_patch.sh /data/adb/*liorsmagic* \
/data/adb/post-fs-data.d /data/adb/service.d /data/adb/modules* \
/data/unencrypted/liorsmagic /metadata/liorsmagic /persist/liorsmagic /mnt/vendor/persist/liorsmagic

ADDOND=/system/addon.d/99-liorsmagic.sh
if [ -f $ADDOND ]; then
  blockdev --setrw /dev/block/mapper/system$SLOT 2>/dev/null
  mount -o rw,remount /system || mount -o rw,remount /
  rm -f $ADDOND
fi

cd /

if $BOOTMODE; then
  ui_print "********************************************"
  ui_print " The Liorsmagic app will uninstall itself, and"
  ui_print " the device will reboot after a few seconds"
  ui_print "********************************************"
  (sleep 8; /system/bin/reboot)&
else
  ui_print "********************************************"
  ui_print " The Liorsmagic app will not be uninstalled"
  ui_print " Please uninstall it manually after reboot"
  ui_print "********************************************"
  recovery_cleanup
  ui_print "- Done"
fi

rm -rf $TMPDIR
exit 0
