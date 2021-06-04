LOCAL_PATH := $(call my-dir)

#include $(CLEAR_VARS)
#LOCAL_MODULE := NdkTest
#LOCAL_SRC_FILES := JniTest.cpp
#include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := NdkTest2
LOCAL_SRC_FILES := JniTest2.cpp
include $(BUILD_SHARED_LIBRARY)