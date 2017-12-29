import QtQuick 2.2
import "./"

Rectangle{
    id:button
    width:100
    height:100
    property int ybak:y
    property url btnPicPress:""
    property url btnPicRelease: btnPicPress
    property alias btnIco: ico
    property alias btnText:btnText
    property alias tabBtnImg:pic.source
    property bool tabBtnFlg:false
    property alias pic:pic.source
    property alias btnTextColor:btnText.color
    property alias btnSelt:pic
    property int btnTextExtend:2
    property color bgColor: "transparent"
    property color bgPressColor: bgColor
    property bool enable:true
    property int  icoHorOffect:0
    property int nEnableBtnTime: 200    //防止按钮点击过快，规定按钮在点击之后的nEnableBtnTime时间内不能点击
    signal notifyBtnPress()
    signal notifyBtnRelease()
    signal notifyBtnCanceled()

    signal notifyBtnLeft()
    signal notifyBtnRight()
    signal notifyComplited()
    signal notifyBtnLongTouched()

    property int delayTimeOpacityBtn: 300
    property int delayTimeChangeYBtn: 250

    property bool btnMoveFlg: false
    property bool rightExtentTouchAreaFlg:false
    property bool changeFlg: false
    property bool scaleEnable: true
    property alias scaleValue: pic.scale
    property bool enableFlg:true
    property alias pngCache:pic.cache
    property alias picResource:pic.source
    property bool asyncFlg:true
    property alias picBorder:pic.border
    property bool pressed: false    //是否按下

    color:bgColor
    clip: true
    opacity: btnMoveFlg ? 0:1

    BorderImage{
        id:pic
        anchors.fill:parent
        border { left: 0; top: 0; right: 0; bottom: 0}
        source:btnPicRelease
        smooth:true
        antialiasing: true
        asynchronous:asyncFlg
        cache: false

    }

    Image{
        id:ico
        anchors.centerIn: parent
        anchors.horizontalCenterOffset:icoHorOffect
        asynchronous:asyncFlg
    }

    Text{
        id:btnText
        font.pixelSize: 20
        color: "red"
    }

    TouchArea{
        id:touch
        height:parent.height
        width:(rightExtentTouchAreaFlg == true)?btnTextExtend*parent.width:parent.width
        onAreaTouched:{
            if(!enableFlg)return
            if(!enable)return
            notifyBtnPress()
            pic.source = btnPicPress
            button.pressed = true
        }
        onAreaReleased:{
            if(!enableFlg)return
            if(!enable)return
            enableFlg=false
            enableBtn.running=true
            notifyBtnRelease()
            pic.source = btnPicRelease
            button.pressed = false
        }
        onAreaCanceled: {
            if(!enableFlg)return
            notifyBtnCanceled()
            pic.source = btnPicRelease
            button.pressed = false
        }
        onAreaTouchLeft: {
            if(!enableFlg)return
            notifyBtnLeft()
        }
        onAreaTouchRight: {
            if(!enableFlg)return
            notifyBtnRight()
        }
        onAreaLongTouched: {
            notifyBtnLongTouched()
        }
    }

    property int stateIndex: 0

    Timer{
        id:enableBtn
        interval: nEnableBtnTime
        repeat: false
        running: false
        onTriggered: {
            enableFlg=true  //防止按钮点击过快
        }
    }
    StateGroup{
        states:[
            State{
                 name:"pressed"
                 when: pressed
                 PropertyChanges {
                     target: button
                     color: bgPressColor
                     scale: scaleEnable ? 0.9 : 1
                 }
            },
            State{
                 name:"released"
                 when: !pressed
                 PropertyChanges {
                     target: button
                     color: bgColor
                     scale: 1
                 }
            }
        ]
        //过渡动画
        transitions: Transition {
            ParallelAnimation {
                PropertyAnimation { easing.type: Easing.OutCubic;  duration: 200; properties: "color" }
                PropertyAnimation { easing.type: Easing.OutCubic;  duration: 200; properties: "scale" }
            }
        }
    }
    Component.onCompleted: {
        notifyComplited()
    }

}
