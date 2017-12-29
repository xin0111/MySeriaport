#ifndef QMLCPPINTERFACE_H
#define QMLCPPINTERFACE_H

#include <QObject>


class QmlCppInterface :public QObject
{
    Q_OBJECT
private:
    QmlCppInterface();
    static QmlCppInterface * sInstance;

public slots:
      // qml call public slots or public method flagged with Q_INVOKABLE
     Q_INVOKABLE void openSerialPort(QString sBuadrate,int port);
     void closeSerialPort();
public:
    static QmlCppInterface * getInterface();

};

#endif // QMLCPPINTERFACE_H
