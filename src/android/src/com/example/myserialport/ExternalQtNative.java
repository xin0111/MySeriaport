package com.example.myserialport;
import java.lang.String;


public class ExternalQtNative {

    public native void onCmdReceived(String strRecvCmd);

    //触屏点击事件
    public native void onTouchEnvent();

}

