package com.example.myserialport.serialport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.myserialport.util.ToolUtil;

/**
 * 中控管理类
 *
 * @author
 *
 */
public class CenterConsoleHolder {
        /** 串口上 */
        public static final String SERIAL_PORT_TOP = "/dev/ttyS3";
        /** 串口下 */
        public static final String SERIAL_PORT_BOTTOM = "/dev/ttyS2";
        /** 内部串口 */
        public static final String SERIAL_PORT_MAIN_BOARD = "/dev/ttyS4";
        /** RJ45 */
        public static final String SERIAL_PORT_RJ45 = "/dev/ttyS1";

        /** 不同的串口设备实例*/
        private SerialPort mSerialPort_rj45 = null;
        private SerialPort mSerialPort_up = null;
        private SerialPort mSerialPort_bottom = null;
        private SerialPort mSerialPort_internal = null;

        /** 不同的串口实例的输入流*/
        private FileInputStream mFileInputStream_rj45 = null;
        private FileInputStream mFileInputStream_up = null;
        private FileInputStream mFileInputStream_bottom = null;
        private FileInputStream mFileInputStream_internal = null;

        /** 不同的串口实例的输出流*/
        private FileOutputStream mFileOutputStream_rj45 = null;
        private FileOutputStream mFileOutputStream_up = null;
        private FileOutputStream mFileOutputStream_bottom = null;
        private FileOutputStream mFileOutputStream_internal = null;

        private static CenterConsoleHolder mCenterConsoleHolder;
        private static final int CMD_SEND = 0x00;

        private CenterConsoleHolder() {
            super();
        }

        /**
         * 拿到中控对象，拿到对象后通信串口时先打开串口{@link #openSerialPort(File, int, int)}
         *
         * @return
         */
        public static CenterConsoleHolder getInstance() {
            if (mCenterConsoleHolder == null) {
                mCenterConsoleHolder = new CenterConsoleHolder();
            }
            return mCenterConsoleHolder;
        }

        /**
         * 打开串口，在确定不需要时务必关闭串口
         *
         * @param device
         *            串口
         * @param baudrate
         *            波特率
         * @param flags
         *            传入0即可
         */
        public void openSerialPort(File device, int baudrate, int flags) {
            // 如果出现波特率没有设置之类的情况，则给一个默认值
            baudrate  = baudrate < 0 ? 9600 : baudrate;
            // 防止出现乱传值的问题
            flags = flags != 0 ? 0 : flags;

            if (device != null) {
                try {
                    System.out.println("device AbsolutePath: " + device.getAbsolutePath());
                    if(device.getAbsolutePath().equals("/dev/ttyS1")){//RJ45
                        mSerialPort_rj45 = new SerialPort(device, baudrate, flags);
                        mFileInputStream_rj45 = (FileInputStream) mSerialPort_rj45.getInputStream();
                    }else if(device.getAbsolutePath().equals("/dev/ttyS3")){//_3_5_mm_up
                        mSerialPort_up = new SerialPort(device, baudrate, flags);
                        mFileInputStream_up = (FileInputStream) mSerialPort_up.getInputStream();
                    }else if(device.getAbsolutePath().equals("/dev/ttyS2")){//_3_5_mm_down
                        mSerialPort_bottom = new SerialPort(device, baudrate, flags);
                        mFileInputStream_bottom = (FileInputStream) mSerialPort_bottom.getInputStream();
                    }else if(device.getAbsolutePath().equals("/dev/ttyS4")){//internal
                        mSerialPort_internal = new SerialPort(device, baudrate, flags);
                        mFileInputStream_internal = (FileInputStream) mSerialPort_internal.getInputStream();
                    }
                    System.out.println(device.getAbsolutePath() + "串口已经打开");
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 关闭串口
         *
         */
        public void closeSerialPort() {
            // 1.关闭输入输出
            try {
                if (mFileInputStream_rj45 != null) {
                    mFileInputStream_rj45.close();
                    mFileInputStream_rj45 = null;
                }
                if (mFileInputStream_up != null) {
                    mFileInputStream_up.close();
                    mFileInputStream_up = null;
                }
                if (mFileInputStream_bottom != null) {
                    mFileInputStream_bottom.close();
                    mFileInputStream_bottom = null;
                }
                if (mFileInputStream_internal != null) {
                    mFileInputStream_internal.close();
                    mFileInputStream_internal = null;
                }
                if (mFileOutputStream_rj45 != null) {
                    mFileOutputStream_rj45.close();
                    mFileOutputStream_rj45 = null;
                }
                if (mFileOutputStream_up != null) {
                    mFileOutputStream_up.close();
                    mFileOutputStream_up = null;
                }
                if (mFileOutputStream_bottom != null) {
                    mFileOutputStream_bottom.close();
                    mFileOutputStream_bottom = null;
                }
                if (mFileOutputStream_internal != null) {
                    mFileOutputStream_internal.close();
                    mFileOutputStream_internal = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 2.关闭串口
            if (mSerialPort_rj45 != null) {
                mSerialPort_rj45.close();
                mSerialPort_rj45 = null;
                System.out.println("串口RJ45已经关闭");
            }
            if (mSerialPort_up != null) {
                mSerialPort_up.close();
                mSerialPort_up = null;
                System.out.println("up串口已经关闭");
            }
            if (mSerialPort_bottom != null) {
                mSerialPort_bottom.close();
                mSerialPort_bottom = null;
                System.out.println("bottom串口已经关闭");
            }
            if (mSerialPort_internal != null) {
                mSerialPort_internal.close();
                mSerialPort_internal = null;
                System.out.println("内部串口已经关闭");
            }
        }
        /**
         * 发送指令到中控
         *
         * @param cmdContent
         *            指令内容
         */
        public void sendCmd(String cmdContent) {
            if (mSerialPort_rj45 != null) {
                mFileOutputStream_rj45 = (FileOutputStream) mSerialPort_rj45.getOutputStream();
                try {
                    System.out.println("SerialPort:开始向串口RJ45写入命令:"+cmdContent);
                    mFileOutputStream_rj45.write(ToolUtil.hexStringToBytes(cmdContent));
                    System.out.println("SerialPort:向串口RJ45写入命令结束:"+cmdContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mSerialPort_up != null){
                mFileOutputStream_up = (FileOutputStream) mSerialPort_up.getOutputStream();
                try {
                    System.out.println("SerialPort:开始向串口UP写入命令:"+cmdContent);
                    mFileOutputStream_up.write(ToolUtil.hexStringToBytes(cmdContent));
                    System.out.println("SerialPort:向串口UP写入命令结束:"+cmdContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mSerialPort_bottom != null){
                mFileOutputStream_bottom = (FileOutputStream) mSerialPort_bottom.getOutputStream();
                try {
                    System.out.println("SerialPort:开始向串口BOTTOM写入命令:"+cmdContent);
                    mFileOutputStream_bottom.write(ToolUtil.hexStringToBytes(cmdContent));
                    System.out.println("SerialPort:向串口BOTTOM写入命令结束:"+cmdContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mSerialPort_internal != null){
                mFileOutputStream_internal = (FileOutputStream) mSerialPort_internal.getOutputStream();
                try {
                    System.out.println("SerialPort:开始向串口INTERNAL写入命令:"+cmdContent);
                    mFileOutputStream_internal.write(ToolUtil.hexStringToBytes(cmdContent));
                    System.out.println("SerialPort:向串口INTERNAL写入命令结束:"+cmdContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 接收到的串口发来的指令字符串
         *
         * @param cmd
         */
        public void receivedCmd(String cmd) {
            if (mOnCenterConsoleListener != null) {
                System.out.println("接收到的串口发来的指令字符串："+cmd);
                mOnCenterConsoleListener.receiveCmd(cmd);
            }
        }

        /**
         * 获取串口输入流
         *
         * @return
         */
        public InputStream[] getInputStream() {
            InputStream[] inputArray = {mFileInputStream_rj45,mFileInputStream_up,mFileInputStream_bottom,mFileInputStream_internal};
            return inputArray;
        }

        /**
         * 获取串口输出流
         *
         * @return
         */
        public OutputStream[] getOutputStream() {
            OutputStream[] outputArray = {mFileOutputStream_rj45,mFileOutputStream_up,mFileOutputStream_bottom,mFileOutputStream_internal};
            return outputArray;
        }

        private OnCenterConsoleListener mOnCenterConsoleListener;

        public void setCenterConsoleListener(OnCenterConsoleListener onCenterConsoleListener) {
            mOnCenterConsoleListener = onCenterConsoleListener;
        }

        public interface OnCenterConsoleListener {
            /**
             * 接收到的中控发来的指令
             *
             * @param cmdContent
             */
            void receiveCmd(String cmdContent);
            /**
             * 发指令到中控
             *
             * @param cmdContent
             */
            void sendCmd(String cmdContent);
        }

}
