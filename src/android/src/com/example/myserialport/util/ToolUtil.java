package com.example.myserialport.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.net.ParseException;
import android.os.Environment;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ToolUtil {
    /**
     * 防止过快二次点击
     * */
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 300) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
        /**
         * 将字节数组按16进制解析成字符串
         *
         * @param src
         *            字节数组
         * @param size
         *            要从字节数组中去除元素的个数
         * @return
         */
        public static String bytesToHexString(byte[] src, int size) {
                StringBuilder stringBuilder = new StringBuilder("");
                if (src == null || src.length <= 0) {
                        return null;
                }
                for (int i = 0; i < size; i++) {
                        int v = src[i] & 0xFF;
                        String hv = Integer.toHexString(v);
                        if (hv.length() < 2) {
                                stringBuilder.append(0);
                        }
                        stringBuilder.append(hv);
                }
                return stringBuilder.toString().toUpperCase(Locale.CHINESE);

        }
        /**
         * 十六进制字符串转为字节数组
         *
         * @param inputStr
         * @return
         */
        public static byte[] hexStringToBytes(String inputStr) {
                byte[] result = new byte[inputStr.length() / 2];
                for (int i = 0; i < inputStr.length() / 2; ++i)
                        result[i] = (byte) (Integer.parseInt(
                                        inputStr.substring(i * 2, i * 2 + 2), 16) & 0xff);
                return result;
        }

    private static SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd");
    public static void copyMscLogCfg(Context context) {
        String mscPath = Environment.getExternalStorageDirectory().getPath() + "/msc/";
        String timePath = Environment.getExternalStorageDirectory().getPath() + "/msc_log_time.txt";

        File mscDir = new File(mscPath);
        if (!mscDir.exists()) {
            mscDir.mkdirs();
        }
        File file = new File(timePath);
        try {
            String encoding="GBK";
            if(!file.isFile() || !file.exists()){ //判断文件是否存在
                file.createNewFile();
            }
            InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            lineTxt = bufferedReader.readLine().trim();
            if (lineTxt.isEmpty()) {
                writeLittleFile(file, "0");
            } else {
                try {
                    System.out.println("lineTxt-->" + lineTxt);
                    int times = Integer.parseInt(lineTxt);		//获取启动次数，第三次启动或者日志总大小大于100M的时候删除讯飞日志
                    long mscSize = getDirSize(new File(mscPath));
                    System.out.println("mscSize-->" + mscSize);
                    if (mscSize > 10 * 1024 * 1024 || times >= 1) {
                        deleteAllFiles(mscDir);
                        mscDir.mkdirs();
                        writeLittleFile(file, "0");
                    } else {
                        writeLittleFile(file, "1");
                    }
                }
                catch (ParseException e) {
                    writeLittleFile(file, "1");
                    e.printStackTrace();
                }
            }
            System.out.println(lineTxt);
            read.close();
        } catch (Exception e) {
            writeLittleFile(file, "1");
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        boolean b = copyFile(context, "iflytek/msc.cfg", mscPath + "msc.cfg", true);
        System.out.println("copyMscLogCfg:" + b);
    }
    /**
     * 递归方式 计算文件夹的大小
     * @param file
     * @return
     */
    private static long getDirSize(final File file) {
        if (file.isFile())
            return file.length();
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (final File child : children)
                total += getDirSize(child);
        return total;
    }
    public static void writeLittleFile(File file, String content) {
        try {
            byte bt[] = new byte[1024];
            bt = content.getBytes();
            FileOutputStream in = new FileOutputStream(file);
            try {
                in.write(bt, 0, bt.length);
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void deleteAllFiles(File root) {
          File files[] = root.listFiles();
          if (files != null)
              for (File f : files) {
                  if (f.isDirectory()) { // 判断是否为文件夹
                      deleteAllFiles(f);
                      try {
                          f.delete();
                      } catch (Exception e) {
                      }
                  } else {
                      if (f.exists()) { // 判断是否存在
                          deleteAllFiles(f);
                          try {
                              f.delete();
                          } catch (Exception e) {
                          }
                      }
                  }
              }
      }
     /**
      * 复制单个文件
      *
      * @param srcFileName
      *            待复制的文件名
      * @param descFileName
      *            目标文件名
      * @param overlay
      *            如果目标文件存在，是否覆盖
      * @return 如果复制成功返回true，否则返回false
      */
     public static boolean copyFile(Context context, String srcFileName, String destFileName,
             boolean overlay) {
         InputStream inputStream = null;
         try {
             inputStream = context.getResources().getAssets().open(srcFileName);
         } catch (IOException e) {
             e.printStackTrace();
             return false;
         }
         // 判断目标文件是否存在
         File destFile = new File(destFileName);
         if (destFile.exists()) {
             // 如果目标文件存在并允许覆盖
             if (overlay) {
                 // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                 new File(destFileName).delete();
             }
         } else {
             // 如果目标文件所在目录不存在，则创建目录
             if (!destFile.getParentFile().exists()) {
                 // 目标文件所在目录不存在
                 if (!destFile.getParentFile().mkdirs()) {
                     // 复制文件失败：创建目标文件所在目录失败
                     return false;
                 }
             }
         }
         // 复制文件
         int byteread = 0; // 读取的字节数
         OutputStream out = null;
         try {
             out = new FileOutputStream(destFile);
             byte[] buffer = new byte[1024];

             while ((byteread = inputStream.read(buffer)) != -1) {
                 out.write(buffer, 0, byteread);
             }
             return true;
         } catch (FileNotFoundException e) {
             return false;
         } catch (IOException e) {
             return false;
         } finally {
             try {
                 if (out != null)
                     out.close();
                 if (inputStream != null)
                     inputStream.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }
     /**
      * 判断字符串是否为数字
      */
     public static boolean isNumeric(String str){
         Pattern pattern = Pattern.compile("[0-9]*");
         Matcher isNum = pattern.matcher(str);
         if( !isNum.matches() ){
             return false;
         }
         return true;
     }
     /**
      * 十六进制字符串装十进制
      *
      * @param hex
      *            十六进制字符串
      * @return 十进制数值
      */
      public static int hexStringToAlgorism(String hex) {
          hex = hex.toUpperCase();
          int max = hex.length();
          int result = 0;
          for (int i = max; i > 0; i--) {
              char c = hex.charAt(i - 1);
              int algorism = 0;
              if (c >= '0' && c <= '9') {
                  algorism = c - '0';
              } else {
                  algorism = c - 55;
              }
              result += Math.pow(16, max - i) * algorism;
          }
          return result;
      }
      /**
       * 十六进制转字符串
       *
       * @param hexString
       *            十六进制字符串
       * @param encodeType
       *            编码类型4：Unicode，2：普通编码
       * @return 字符串
       */
      public static String hexStringToString(String hexString, int encodeType) {
          String result = "";
          int max = hexString.length() / encodeType;
          for (int i = 0; i < max; i++) {
              char c = (char) hexStringToAlgorism(hexString
                      .substring(i * encodeType, (i + 1) * encodeType));
              result += c;
          }
          return result;
      }

}
