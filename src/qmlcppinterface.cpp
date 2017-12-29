#include <QDebug>

#include "qmlcppinterface.h"

#ifdef ANDROID_VERSION
#include "androidjni/candroidmethod.h"
#endif

QmlCppInterface *QmlCppInterface::sInstance = 0;

QmlCppInterface::QmlCppInterface()
{
    sInstance = this;
}

QmlCppInterface* QmlCppInterface::getInterface(){
    if(sInstance==NULL)
        sInstance = new QmlCppInterface;
    return sInstance;
}


void QmlCppInterface::openSerialPort(QString sBuadrate,int port){
#ifdef ANDROID_VERSION
    SingleAndroidMethod::getInstance().openSerialPort(sBuadrate.toInt(),port);
#endif
    qDebug()<<"Open";
}


void QmlCppInterface::closeSerialPort(){
#ifdef ANDROID_VERSION
    SingleAndroidMethod::getInstance().closeSerialPort();
#endif
    qDebug()<<"Close";
}


