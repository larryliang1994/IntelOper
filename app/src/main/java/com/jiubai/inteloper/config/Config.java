package com.jiubai.inteloper.config;

import com.jiubai.inteloper.bean.Alarm;

import java.util.ArrayList;

/**
 * 存放通用变量
 */
public class Config {
    public static String UserName = "empty";

    public static ArrayList<Alarm> HistoryAlarms;

    public static String COOKIE = "";

    public static boolean STATION_ADD = false;

    public static boolean IS_CONNECTED = false;

    public static boolean NO_NETWORK = false;
}