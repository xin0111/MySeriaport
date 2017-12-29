#include <QDebug>
#include <QGuiApplication>

#include "candroidmethod.h"

CAndroidMethod::CAndroidMethod()
{
    mActivity = QtAndroid::androidActivity();
    connect(qApp,SIGNAL(aboutToQuit()),this,SLOT(closeSerialPort()));
}

//callStaticMethod
void CAndroidMethod::openSerialPort(int baudrate,int port){
    if(mActivity.isValid()){
        mActivity.callMethod<int>("openSerialPort","(II)V",baudrate,port);
    }
    QAndroidJniEnvironment env;
    if (env->ExceptionCheck())
    {
        env->ExceptionClear();
        qCritical() << "Something went wrong!";
    }
}
void CAndroidMethod::closeSerialPort(){
    if(mActivity.isValid()){
        mActivity.callMethod<int>("closeSerialPort","()V");
    }
}



