#ifndef HMIINTERFACE_H
#define HMIINTERFACE_H

#include <QObject>
#include "csingleton.h"

class HmiInterface:public QObject
{
    Q_OBJECT
    //data type
    Q_ENUMS(Status)
public:
    HmiInterface();
public:
    enum Status {
        Ready,
        Loading,
        Error
    };

};
//typedef Singleton<HmiInterface> SingleHmiInterface;
#endif // HMIINTERFACE_H
