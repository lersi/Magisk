LOCAL_PATH := $(call my-dir)

###########################
# Rust compilation outputs
###########################

LIBRARY_PATH = ../out/$(TARGET_ARCH_ABI)/libliorsmagic-rs.a
ifneq (,$(wildcard $(LOCAL_PATH)/$(LIBRARY_PATH)))
include $(CLEAR_VARS)
LOCAL_MODULE := liorsmagic-rs
LOCAL_SRC_FILES := $(LIBRARY_PATH)
include $(PREBUILT_STATIC_LIBRARY)
endif

LIBRARY_PATH = ../out/$(TARGET_ARCH_ABI)/libliorsmagicboot-rs.a
ifneq (,$(wildcard $(LOCAL_PATH)/$(LIBRARY_PATH)))
include $(CLEAR_VARS)
LOCAL_MODULE := boot-rs
LOCAL_SRC_FILES := $(LIBRARY_PATH)
include $(PREBUILT_STATIC_LIBRARY)
endif

LIBRARY_PATH = ../out/$(TARGET_ARCH_ABI)/libliorsmagicinit-rs.a
ifneq (,$(wildcard $(LOCAL_PATH)/$(LIBRARY_PATH)))
include $(CLEAR_VARS)
LOCAL_MODULE := init-rs
LOCAL_SRC_FILES := $(LIBRARY_PATH)
include $(PREBUILT_STATIC_LIBRARY)
endif

LIBRARY_PATH = ../out/$(TARGET_ARCH_ABI)/libliorsmagicpolicy-rs.a
ifneq (,$(wildcard $(LOCAL_PATH)/$(LIBRARY_PATH)))
include $(CLEAR_VARS)
LOCAL_MODULE := policy-rs
LOCAL_SRC_FILES := $(LIBRARY_PATH)
include $(PREBUILT_STATIC_LIBRARY)
endif
