#pragma once

#include <string>

#define JAVA_PACKAGE_NAME "com.topjohnwu.liorsmagic"
#define LOGFILE         "/cache/liorsmagic.log"
#define SECURE_DIR      "/data/adb"
#define MODULEROOT      SECURE_DIR "/modules"
#define MODULEUPGRADE   SECURE_DIR "/modules_update"
#define DATABIN         SECURE_DIR "/liorsmagic"
#define LIORSMAGICDB        SECURE_DIR "/liorsmagic.db"

// tmpfs paths
extern std::string    LIORSMAGICTMP;
#define INTLROOT      ".liorsmagic"
#define MIRRDIR       INTLROOT "/mirror"
#define PREINITMIRR   INTLROOT "/preinit"
#define BLOCKDIR      INTLROOT "/block"
#define PREINITDEV    BLOCKDIR "/preinit"
#define WORKERDIR     INTLROOT "/worker"
#define MODULEMNT     INTLROOT "/modules"
#define BBPATH        INTLROOT "/busybox"
#define ROOTOVL       INTLROOT "/liorootdir"
#define SHELLPTS      INTLROOT "/pts"
#define ROOTMNT       ROOTOVL  "/.mount_list"
#define ZYGISKBIN     INTLROOT "/zygisk"
#define SELINUXMOCK   INTLROOT "/selinux"
#define MAIN_CONFIG   INTLROOT "/config"
#define MAIN_SOCKET   INTLROOT "/socket"

constexpr const char *applet_names[] = { "liorsu", "resetprop", nullptr };

#define POST_FS_DATA_WAIT_TIME       40
#define POST_FS_DATA_SCRIPT_MAX_TIME 35

extern int SDK_INT;
#define APP_DATA_DIR (SDK_INT >= 24 ? "/data/user_de" : "/data/user")

// Multi-call entrypoints
int liorsmagic_main(int argc, char *argv[]);
int su_client_main(int argc, char *argv[]);
int resetprop_main(int argc, char *argv[]);
int app_process_main(int argc, char *argv[]);
int zygisk_main(int argc, char *argv[]);
