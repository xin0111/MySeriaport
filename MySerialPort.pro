TEMPLATE = app

DEFINES += WIN_VERSION

QT += qml quick opengl

SOURCES += src/main.cpp \
    src/qmlcppinterface.cpp \
    src/hmiinterface.cpp

RESOURCES += src/qml/qml.qrc



# Additional import path used to resolve QML modules in Qt Creator's code model
QML_IMPORT_PATH =

# Default rules for deployment.
include(deployment.pri)



HEADERS += \
    src/qmlcppinterface.h \
    src/csingleton.h \
    src/hmiinterface.h

!android:{

}

android:{
    QT += androidextras
    DEFINES -= WIN_VERSION
    DEFINES += ANDROID_VERSION

    SOURCES += \
      src/androidjni/candroidmethod.cpp\
      src/androidjni/jniinterface.cpp
    HEADERS += \
      src/androidjni/candroidmethod.h\
      src/androidjni/jniinterface.h

    DISTFILES += \
        src/android/AndroidManifest.xml\
        src/android/src/com/example/myserialport/MainActivity.java\
        src/android/src/org/qtproject/qt5/android/bindings/QtActivity.java \
        src/android/src/org/qtproject/qt5/android/bindings/QtApplication.java \
        src/android/src/com/example/myserialport/ExternalQtNative.java \
        src/android/gradle/wrapper/gradle-wrapper.jar \
        src/android/gradlew \
        src/android/res/values/libs.xml \
        src/android/build.gradle \
        src/android/gradle/wrapper/gradle-wrapper.properties \
        src/android/gradlew.bat

    ANDROID_PACKAGE_SOURCE_DIR = $$PWD/src/android
}

OBJECTS_DIR += obj




