import QtQuick 2.2

MouseArea
{
    id: touchArea
    /* Special Department of attributes */
    property alias touchX: touchArea.mouseX     //X coordinate
    property alias touchY: touchArea.mouseY     //Y coordinate
    property alias touched: touchArea.pressed   //Touch state
    property bool restriction: false            //The presence or absence of regulation
    property bool disabled: false               //Non-operational state

    property bool inside: false                 //Area
    property bool longTouched: false            //Long touch state
    property bool restricted: false             //State regulation

    /* Usually touch signal system */
    signal areaTouched(real x, real y)                //Touch
    signal areaReleased(real x, real y)               //Release
    signal areaCanceled()                             //Canceled
    signal areaTouchMoved(real x, real y)             //Move
    signal areaEntered()                              //Enter
    signal areaExited()                               //Exit
    signal areaTouchLeft() //left touch
    signal areaTouchRight() //right touch

    /* Special touch signal system */
    signal areaLongTouched(real x, real y)            //Press and hold
    signal areaShortTouched(real x, real y)           //Short press
    signal areaTapped(real x, real y)                 //Click
    signal areaDoubleTapped(real x, real y)           //Double-tap
    signal areaTouchStarted(real x, real y)           //Touch to start
    signal areaTouchEnded(real x, real y, int factor) //Touch the end of

    property real tmpX:10
    property real tmpY:10


    /* Mouse-down event */
    onPressed:
    {
        touchArea.areaTouchStarted(mouse.x, mouse.y)
        touchArea.areaTouched(mouse.x, mouse.y)
        tmpX = mouse.x
        tmpY = mouse.y
    }
    /* Mouse release events */
    onReleased:
    {
        /* If released in the region */
        if(false != inside)
        {
            /* Must also send a long press before short press */
            if(false == longTouched)
            {
                touchArea.areaShortTouched(mouse.x, mouse.y)  //Signal transmission
            }
            touchArea.areaReleased(mouse.x, mouse.y)
            touchArea.areaTouchEnded(mouse.x, mouse.y, 0)
            if(mouse.x >tmpX)
            {
                areaTouchRight()
            }
            else if(mouse.x <tmpX)
            {
                areaTouchLeft()
            }
            else
            {
                /* do nothing */
            }

        }

        /* If released outside the region */
        else
        {
            touchArea.areaCanceled()                   //Signal transmission
            touchArea.areaTouchEnded(mouse.x, mouse.y, 1)
        }

        longTouched = false
    }

    /* Canceled event */
    onCanceled:
    {
        //console.log("canceled")
        touchArea.areaCanceled()
        touchArea.areaTouchEnded(-1, -1, 2)
        longTouched = false
    }

    /* Moved event */
    onPositionChanged:
    {
        //console.log("positionChanged")
        touchArea.areaTouchMoved(mouse.x , mouse.y)
    }

    /* Enter event */
    onEntered:
    {
        //console.log("entered")
        touchArea.areaEntered()
        inside = true;             //In the region

    }

    /* Exit event */
    onExited:
    {
        //console.log("exited")
        touchArea.areaExited()
        inside = false;
    }

    /* Press and hold the event */
    onPressAndHold:
    {
        //console.log("pressAndHold")

        touchArea.areaLongTouched(mouse.x, mouse.y)

        longTouched = true

    }

    /* Click on event */
    onClicked:
    {
        //console.log("clicked")

        touchArea.areaTapped(mouse.x, mouse.y)   //Signal transmission
    }

    /* Double-click events */
    onDoubleClicked:
    {
        //console.log("doubleClicked")
        touchArea.areaTouchStarted(mouse.x, mouse.y)   //Signal transmission
        touchArea.areaTouched(mouse.x, mouse.y)
        touchArea.areaDoubleTapped(mouse.x, mouse.y)
    }


    /* enabled state of change */
    StateGroup
    {
        states: [
            /* When disabled the property to true , the enabled to false */
            State
            {
                name: "disabled"                      //Inoperable
                when: touchArea.disabled
                PropertyChanges {
                    target: touchArea
                    enabled: false
                }
            },
            /*When the restricted the property and the restriction the property to true , the enabled to false*/
            State
            {
                name: "restricted"                  //Regulation
                when: touchArea.restricted && touchArea.restriction
                PropertyChanges {
                    target: touchArea
                    enabled: false
                }
            },
            /* When the paternal enabled property to false , enabled to false */
            State
            {
                name: "parent is not enabled"   //Paternal non- operation
                when: !parent.enabled
                PropertyChanges {
                    target: touchArea
                    enabled: false
                }
            }
        ]
    }
}
