package com.jiubai.inteloper.bean;

import java.io.Serializable;

/**
 * Created by larry on 18/08/2017.
 */

public class Alarm implements Serializable {
    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_UP = 2;
    public static final int STATUS_DOWN = 3;
    public static final int STATUS_UP_2 = 4;
    public static final int STATUS_DOWN_2 = 5;

    private int status;
    private String occurTime;
    private String warnStr;

    public Alarm() {
    }

    public Alarm(int status, String occurTime, String warnStr) {
        this.status = status;
        this.occurTime = occurTime;
        this.warnStr = warnStr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(String occurTime) {
        this.occurTime = occurTime;
    }

    public String getWarnStr() {
        return warnStr;
    }

    public void setWarnStr(String warnStr) {
        this.warnStr = warnStr;
    }
}
