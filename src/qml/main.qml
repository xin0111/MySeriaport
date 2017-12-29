import QtQuick 2.1
import QtQuick.Window 2.0
import QtQuick.Controls 1.4

import MyHmiInterface 2.0
import "./"

Window {
    visible: true
    id:main
    width: 1280
    height: 800

    CHmiInterface{
        id:hmiInterface
        Component.onCompleted: {
            console.log(CHmiInterface.Ready);
        }
    }


    Column {
        spacing:20

        Row{
            spacing:20
            anchors.left: main.left
            anchors.leftMargin: 600
            anchors.top: main.top
            anchors.topMargin:50
            ComboBox {
                id:baudrateCombo
                width: 100
                model: [ "9600", "19200","38400","57600", "115200" ]
            }
            ComboBox {
                id:portCombo
                width: 100
                model: [ "up", "down"]
            }
        }

        Row{
            spacing:20
            anchors.left: main.left
            anchors.leftMargin: 150
            anchors.top: main.top
            anchors.topMargin:150
            BasicButton{
                id:btnrun
                border.width: 1
                btnText.text:"开始"
                onNotifyClicked:{
                    qmlInterface.openSerialPort(baudrateCombo.currentText,portCombo.currentIndex);
                }
            }
            BasicButton{
                id:btnstop
                border.width: 1
                btnText.text:"结束"
                onNotifyClicked:{
                    qmlInterface.closeSerialPort();
                }
            }
        }
    }
}
