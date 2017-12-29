#ifndef CANDROIDMETHOD_H
#define CANDROIDMETHOD_H

#include <QObject>
#include <QAndroidJniEnvironment>
#include <QAndroidJniObject>
#include <QtAndroid>

#include "../csingleton.h"

#define PACKAGE_NAME  ("com.example.myserialport")


class CAndroidMethod:public QObject
{
    Q_OBJECT
public:
    CAndroidMethod();
public slots:
    void openSerialPort(int baudrate,int port);
    void closeSerialPort();
protected:
    inline char* getClassName(const char* funName) const {
        return QString("%1%2").arg(PACKAGE_NAME).arg(funName).toUtf8().data();
    }
private:
    QAndroidJniObject mActivity;
};
typedef Singleton<CAndroidMethod> SingleAndroidMethod;


#endif // CANDROIDMETHOD_H
