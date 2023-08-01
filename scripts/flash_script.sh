#LIORSMAGIC
############################################
# Liorsmagic Flash Script (updater-script)
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
print_title "Liorsmagic $PRETTY_VER Installer"

is_mounted /data || mount /data || is_mounted /cache || mount /cache
mount_partitions
check_data
get_flags
find_boot_image

[ -z $BOOTIMAGE ] && abort "! Unable to detect target image"
ui_print "- Target image: $BOOTIMAGE"

# Detect version and architecture
api_level_arch_detect

[ $API -lt 23 ] && abort "! Liorsmagic only support Android 6.0 and above"

ui_print "- Device platform: $ABI"

BINDIR=$INSTALLER/lib/$ABI
cd $BINDIR
for file in lib*.so; do mv "$file" "${file:3:${#file}-6}"; done
cd /
cp -af $INSTALLER/lib/$ABI32/libliorsmagic32.so $BINDIR/liorsmagic32 2>/dev/null

# Check if system root is installed and remove
$BOOTMODE || remove_system_su

##############
# Environment
##############

ui_print "- Constructing environment"

# Copy required files
rm -rf $LIORSMAGICBIN/* 2>/dev/null
mkdir -p $LIORSMAGICBIN 2>/dev/null
cp -af $BINDIR/. $COMMONDIR/. $BBBIN $LIORSMAGICBIN

# Remove files only used by the Liorsmagic app
rm -f $LIORSMAGICBIN/bootctl $LIORSMAGICBIN/main.jar \
  $LIORSMAGICBIN/module_installer.sh $LIORSMAGICBIN/uninstaller.sh

chmod -R 755 $LIORSMAGICBIN

# addon.d
if [ -d /system/addon.d ]; then
  ui_print "- Adding addon.d survival script"
  blockdev --setrw /dev/block/mapper/system$SLOT 2>/dev/null
  mount -o rw,remount /system || mount -o rw,remount /
  ADDOND=/system/addon.d/99-liorsmagic.sh
  cp -af $COMMONDIR/addon.d.sh $ADDOND
  chmod 755 $ADDOND
fi

##################
# Image Patching
##################

install_liorsmagic

# Cleanups
$BOOTMODE || recovery_cleanup
rm -rf $TMPDIR

ui_print "- Done"
exit 0
