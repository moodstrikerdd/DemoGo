#include <stdio.h>
#include <jni.h>
#include <stdlib.h>
//
/**
  * @author moodstrikerdd 
  * @date 2019/1/10
**/
//
jstring getString(JNIEnv *env, jclass jclass){
return env->NewStringUTF("Hello World From JNI222222222222222!!!!!");
}

static JNINativeMethod gMethods[] = {
{"get","()Ljava/lang/String;",(void *)getString }
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm,void *reserved){
    JNIEnv *env = NULL;
    jint result = JNI_FALSE;

    if(jvm->GetEnv((void**) & env,JNI_VERSION_1_6) != JNI_OK){
        return result;
    }
    if(env == NULL){
        return result;
    }
    jclass clazz = env->FindClass("com/moo/demogo/mainframe/ndk/NdkTest2");
    if(clazz == NULL){
        return result;
    }
    if(env->RegisterNatives(clazz,gMethods,sizeof(gMethods)/sizeof(gMethods[0]))<0){
        return result;
    }

    result = JNI_VERSION_1_6;
    return result;
}

