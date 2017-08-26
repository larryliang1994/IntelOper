package com.jiubai.inteloper.bean;

/**
 * Created by larry on 18/08/2017.
 */

public class Attr {
    public static final int TYPE_VOLTAGE = 1;
    public static final int TYPE_CURRENT = 2;
    public static final int TYPE_ACTIVE = 3;
    public static final int TYPE_IDLE = 4;

    private int type;
    private boolean isExist;
    private float upValue;
    private float downValue;

    public Attr() {
    }

    public Attr(int type, boolean isExist, float upValue, float downValue) {
        this.type = type;
        this.isExist = isExist;
        this.upValue = upValue;
        this.downValue = downValue;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }

    public float getUpValue() {
        return upValue;
    }

    public void setUpValue(float upValue) {
        this.upValue = upValue;
    }

    public float getDownValue() {
        return downValue;
    }

    public void setDownValue(float downValue) {
        this.downValue = downValue;
    }
}


