import QtQuick 2.0

Rectangle {
    id:basicBtn
    width: 100
    height: 20

    property color backgroudColor: "transparent"
    property color clickedColor: "blue"

    property alias btnText: btnText
    property bool bClicked: false
    property bool bScaleEnable: true


    signal notifyClicked();
    signal notifyReleased();
    signal notifyongTouched();

    color:backgroudColor
    TouchArea{
        id:touchArea
        width: basicBtn.width
        height: basicBtn.height
        onAreaTouched:{
            basicBtn.notifyClicked();
            bClicked = true;           
        }
        onAreaReleased:{
            basicBtn.notifyReleased();
            bClicked = false;

        }
        onAreaLongTouched:{
            basicBtn.notifyongTouched();
        }

    }
    Text{
        id:btnText
        font.pixelSize: 20
        anchors.centerIn :basicBtn

    }
    Image{
        id:btnImg
    }

    StateGroup{
        states: [
            State{
                name:"clicked"
                when:bClicked
                PropertyChanges {
                    target: basicBtn
                    color:clickedColor
                    scale: bScaleEnable ? 0.9 : 1
                }
            },
            State{
                name:"released"
                when:!bClicked
                PropertyChanges {
                    target: basicBtn
                    color:"white"
                    scale: 1
                }
            }
        ]
        //过渡动画
        transitions: Transition{
            ParallelAnimation {
                PropertyAnimation { easing.type: Easing.OutCubic;  duration: 100; properties: "scale" }
            }
        }
    }
}

