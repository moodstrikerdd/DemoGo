//
/**
  * @author moodstrikerdd 
  * @date 2019/1/10
**/
//
#include "com_moo_demogo_mainframe_ndk_NdkTest.h"
JNIEXPORT jstring JNICALL Java_com_moo_demogo_mainframe_ndk_NdkTest_get
  (JNIEnv *env, jclass jclass){
return env->NewStringUTF("Hello World From JNI!!!!!");
}

