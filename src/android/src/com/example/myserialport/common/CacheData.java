package com.example.myserialport.common;
import java.util.Map;
import java.util.HashMap;
import java.lang.String;
import java.lang.Integer;

public class CacheData {
        public static final int LOAD_SINGER_COUNT = 200;
        public static final int LOAD_SONG_COUNT = 400;
        public static final int LOAD_ALL_SEARCH_SINGER_COUNT = 200;
        public static final int LOAD_ALL_SEARCH_SONG_COUNT = 400;
        public static double[] COUNT_SATA_SIZE_ARRAY = new double[2];

        /**
            *串口设备对应和波特率
         *
         */
        public static Map<String,Integer> serialPortbaudrate = new HashMap<String,Integer>();
        /**
         * 中控类型
         */
        public static int centerConsoleType = 1;
        /**
         * 串口位置
         */
        public static String serialPort = "/dev/ttyS3";//串口1(up)
        /**
         *
         *已经被占用的串口设备
         */
        public static String serialPortOccupation = null;//默认值为null
}

