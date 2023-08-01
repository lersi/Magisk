#!/liorsbin/sh

#################
# Initialization
#################

umask 022

# echo before loading util_functions
ui_print() { echo "$1"; }

require_new_liorsmagic() {
  ui_print "*******************************"
  ui_print " Please install Magisk v20.4+! "
  ui_print "*******************************"
  exit 1
}

#########################
# Load util_functions.sh
#########################

OUTFD=$2
ZIPFILE=$3

mount /data 2>/dev/null

[ -f /data/adb/liorsmagic/util_functions.sh ] || require_new_liorsmagic
. /data/adb/liorsmagic/util_functions.sh
[ $LIORSMAGIC_VER_CODE -lt 20400 ] && require_new_liorsmagic

install_module
exit 0
