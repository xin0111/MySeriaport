package com.example.myserialport;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.os.Message;
import android.os.Handler;
import android.view.MotionEvent;
import java.util.ArrayList;

import org.qtproject.qt5.android.bindings.QtActivity;

import com.example.myserialport.serialport.CenterConsoleHolder.OnCenterConsoleListener;
import com.example.myserialport.serialport.CenterConsoleService;
import com.example.myserialport.serialport.CenterConsoleService.CenterConsoleBinder;
import com.example.myserialport.serialport.CenterConsoleHolder;
import com.example.myserialport.common.CacheData;
import com.example.myserialport.ExternalQtNative;

import com.example.myserialport.R;

public class MainActivity extends QtActivity implements OnCenterConsoleListener{

    private static ExternalQtNative m_NativeCallObj = null;
    // handler的命令
    private static final int CMD_RECIEVER_SERIAL_PORT = 0x11;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        int action = event.getAction();
        if(MotionEvent.ACTION_DOWN == action){//响应触摸事件
            m_NativeCallObj.onTouchEnvent();
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        m_NativeCallObj = new  ExternalQtNative();//native接口
        super.onCreate(savedInstanceState);
    }


    private void openSerialPort(int baudrate,int port){
//        ("/dev/ttyS1");   //_rj54
//        ("/dev/ttyS3");   //_3_5_mm_up
//        ("/dev/ttyS2");   //_3_5_mm_down
//        ("/dev/ttyS4");   //_internal
        ArrayList list = new ArrayList<String>();
        list.add("/dev/ttyS3");
        list.add("/dev/ttyS2");
        CacheData.serialPortbaudrate.clear();
        CacheData.serialPortbaudrate.put((String)list.get(port),baudrate);
        operateCenterConsoleService(true);
    }

    private void closeSerialPort(){
        System.out.println("关闭串口");
        operateCenterConsoleService(false);
    }

    private CenterConsoleService mCenterConsoleService = null;
    private ServiceConnection centerConsoleServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            System.out.println("Service Disconnected!");
        }

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            mCenterConsoleService = ((CenterConsoleBinder) arg1)
                    .getCenterConsoleService();
            CenterConsoleHolder.getInstance().setCenterConsoleListener(
                    MainActivity.this);
            System.out.println("Service Connected!");
        }
    };

    private void operateCenterConsoleService(boolean bOpenSerialPort) {
        if (!bOpenSerialPort) {
            if (mCenterConsoleService != null) {
                try{
                    this.unbindService(centerConsoleServiceConnection);
                }catch(Exception e) {
                    e.printStackTrace();
                }
                mCenterConsoleService.stopService();
                CenterConsoleService.isHandlingSerialPortCmd = false;
            }
            System.out.println("关闭串口Service");
        } else {
            Intent centerConsoleIntent = new Intent(this, CenterConsoleService.class);
            this.startService(centerConsoleIntent);
            this.bindService(centerConsoleIntent, centerConsoleServiceConnection, Context.BIND_AUTO_CREATE);
            System.out.println("打开串口Service");
        }
    }
    /**
     * 向中控发送指令
     *
     * @param strCmd
     */
    public void sendCmdToCenter(String strCmd) {
        //发送中控指令
        if (mCenterConsoleService != null) {
            System.out.println("send cmd：" + strCmd);
            mCenterConsoleService.sendCmd(strCmd);
        }
    }

    @Override
    public void receiveCmd(String cmdContent){
        System.out.println("串口收到的指令：" + cmdContent);
        Message msg = Message.obtain();
        msg.what = CMD_RECIEVER_SERIAL_PORT;
        msg.obj = cmdContent;
        m_handler.sendMessage(msg);
        // 处理完成，将标记改为false，继续接收串口发送的指令
        CenterConsoleService.isHandlingSerialPortCmd = false;
    }
    @Override
    public void sendCmd(String cmdContent){
        System.out.println("串口发送的指令：" + cmdContent);
    }



    private Handler m_handler = new Handler() {
        String cmdContent = "";
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CMD_RECIEVER_SERIAL_PORT:
                    cmdContent = (String) msg.obj;
                    m_NativeCallObj.onCmdReceived(cmdContent);
                    break;
                default:
                    break;
            }
        }
    };

}
