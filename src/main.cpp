#include <QApplication>
#include <QQmlApplicationEngine>
#include <QQmlContext>
#include <QDesktopWidget>
#include <QQuickView>

#include "qmlcppinterface.h"

#include "hmiinterface.h"
#ifdef ANDROID_VERSION
#include "androidjni/jniinterface.h"
#endif
int main(int argc, char *argv[])
{
#ifdef ANDROID_VERSION
    registerNativeMethods();
#endif
    QApplication app(argc, argv);
    //registers the C++ class HmiInterface as a QML type named MyHmiInterface for version 2.0
    //of a type namespace called "MyHmiInterface":
    qmlRegisterType<HmiInterface> ("MyHmiInterface",2,0,"CHmiInterface");


    QQmlApplicationEngine engine;
    //Set the value(QmlCppInterface Singleton Pointer) of the name(qmlInterface) property on this context.
    engine.rootContext()->setContextProperty("qmlInterface", QmlCppInterface::getInterface());

    engine.load(QUrl(QStringLiteral("qrc:/main.qml")));

    return app.exec();
}
