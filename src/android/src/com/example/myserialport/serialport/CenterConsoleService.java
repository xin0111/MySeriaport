
package com.example.myserialport.serialport;

import java.io.File;
import java.io.IOException;
import java.lang.InterruptedException;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import java.util.Set;
import java.util.HashSet;
import java.io.InputStream;
import com.example.myserialport.common.CacheData;
import com.example.myserialport.util.ToolUtil;


public class CenterConsoleService extends Service {
        private Service mService;
        private ReadSerialPortThread mReadSerialPortThread;
        private CenterConsoleHolder mCenterConsoleHolder;
        private IBinder mBinder = new CenterConsoleBinder();
        private boolean isStop;

        @Override
        public IBinder onBind(Intent arg0) {
            return mBinder;
        }

        @Override
        public void onCreate() {
            super.onCreate();
            isStop = false;
            mService = this;
            System.out.println("创建中控Service");
            new OpenSerialPortThread().start();
        }
        /**
         * 打开串口的线程
         * @author lance
         */
        private class OpenSerialPortThread extends Thread {
            @Override
            public void run() {
                if (mCenterConsoleHolder == null) {
                    mCenterConsoleHolder = CenterConsoleHolder.getInstance();
                }
                System.out.println("打开串口的线程");
                Set m_keys_set = new HashSet();
                m_keys_set = CacheData.serialPortbaudrate.keySet();
                Object[] m_keys_array = m_keys_set.toArray();
                System.out.println("打开串口个数-->" + m_keys_set.size());
                int nBaudrate = 0;
                for(int i = 0 ; i < m_keys_set.size();i++){
                    String sSerialPort = (String)m_keys_array[i];//串口
                    System.out.println("sSerialPort:" + sSerialPort);
                    nBaudrate = CacheData.serialPortbaudrate.get(sSerialPort).intValue();//中控类型
                    System.out.println("nBaudrate:" + nBaudrate);
                    openSerialPort(new SerialPortEntity(new File(sSerialPort), nBaudrate, 0));
                    System.out.println("等待2秒......");
                    try{
                        Thread.sleep(2000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        /**
         * 打开串口
         */
        public void openSerialPort(SerialPortEntity serialPortEntiity) {
            if (mCenterConsoleHolder != null) {
                mCenterConsoleHolder.openSerialPort(serialPortEntiity.device,serialPortEntiity.baudrate, serialPortEntiity.flags);
            }
        }

        /**
         * 关闭串口
         */
        public void closeSerialPort() {
            if (mCenterConsoleHolder != null)
                mCenterConsoleHolder.closeSerialPort();
        }

        /**
         * 停止服务
         */
        public void stopService() {
            // 1.停止服务
            mService.stopSelf();
            // 2.关闭串口
            closeSerialPort();
            // 3.销毁接收线程
            isStop = true;
            mReadSerialPortThread = null;
            System.gc();
            System.out.println("接收串口信息的线程已停止！！");
        }

        public static boolean isHandlingSerialPortCmd = false;

        /**
         * 接收串口发来的指令线程
         *
         *
         */
        private class ReadSerialPortThread extends Thread {
            public ReadSerialPortThread(InputStream inputStream) {
                mInputStream = inputStream;
            }
            public InputStream mInputStream = null;
            @Override
            public void run() {
                while (!isStop) {
                    if (!isHandlingSerialPortCmd) {
                        int size = 0;
                        try {
                            byte[] buffer = new byte[128];// 64
                            if (mInputStream == null){
                                isHandlingSerialPortCmd = false;
                                continue;
                            }
                            size = mInputStream.read(buffer);
                            System.out.println("循环获取串口值的大小："+size);
                            if (size > 0) {
                                // 不继续等待和接收指令
                                isHandlingSerialPortCmd = true;
                                onDataReceived(buffer, size);
                            }
                            buffer = null;
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try{
                        Thread.sleep(100);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }

        /**
         * 处理收到的串口发来的指令
         *
         * @param buffer
         * @param size
         */
        protected void onDataReceived(byte[] buffer, int size) {
            if (mCenterConsoleHolder != null) {
                mCenterConsoleHolder.receivedCmd(ToolUtil.bytesToHexString(buffer,size));
            }
        }

        /**
         * 向中控发送指令
         *
         * @param cmdContent
         */
        public void sendCmd(String cmdContent) {
            synchronized (mService) {
                if (mCenterConsoleHolder != null) {
                    mCenterConsoleHolder.sendCmd(cmdContent);
                }
            }
        }

        /**
         * 绑定服务
         *
         *
         */
        public class CenterConsoleBinder extends Binder {
            /**
             * 获取服务对象
             *
             * @return
             */
            public CenterConsoleService getCenterConsoleService() {
                return CenterConsoleService.this;
            }
        }
}
