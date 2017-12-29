#ifndef JNIINTERFACE_H
#define JNIINTERFACE_H


#include <jni.h>

static void onCmdReceived(JNIEnv *env, jobject thiz, jstring strRecvCmd);

static void onTouchEnvent(JNIEnv *env, jobject thiz);

void registerNativeMethods();

#endif // JNIINTERFACE_H
