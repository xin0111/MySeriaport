#include <QAndroidJniEnvironment>
#include <QAndroidJniObject>
#include <qDebug>
#include <QMetaMethod>
#include "jniinterface.h"
//#include "candroidmethod.h"

// JNIEXPORT void JNICALL Java_com_example_myserialport_ExternalQtNative_onCmdReceived(JNIEnv *env, jobject obj,  jstring strRecvCmd);
void onCmdReceived(JNIEnv *env, jobject obj, jstring strRecvCmd){
    Q_UNUSED(obj);
    const char *cRecvCmd = env->GetStringUTFChars(strRecvCmd, JNI_FALSE);
    env->DeleteLocalRef(strRecvCmd);//释放掉jstring资源，以免内存泄漏
    qDebug() << "receive cmd : " + QString(cRecvCmd);
}

static void onTouchEnvent(JNIEnv *env, jobject thiz){
    Q_UNUSED(env);
    Q_UNUSED(thiz);
    qDebug() << "receive touch envent";
}

void registerNativeMethods()
{
    JNINativeMethod methods[] {
        {"onCmdReceived", "(Ljava/lang/String;)V", (void*)onCmdReceived},
        {"onTouchEnvent","()V",(void*)onTouchEnvent}
    };

    QAndroidJniObject javaClass("com/example/myserialport/ExternalQtNative");
    QAndroidJniEnvironment env;
    jclass objectClass = env->GetObjectClass(javaClass.object<jobject>());
    env->RegisterNatives(objectClass,
                         methods,
                         sizeof(methods) / sizeof(methods[0]));
    env->DeleteLocalRef(objectClass);
}
