package com.jiubai.inteloper.bean;

/**
 * Created by larry on 31/08/2017.
 */

public class Telemetry {
    int attrType;
    String desrc;
    int value;
    float v;

    public Telemetry() {
    }

    public Telemetry(int attrType, String desrc, int value, float v) {
        this.attrType = attrType;
        this.desrc = desrc;
        this.value = value;
        this.v = v;
    }

    public int getAttrType() {
        return attrType;
    }

    public void setAttrType(int attrType) {
        this.attrType = attrType;
    }

    public String getDesrc() {
        return desrc;
    }

    public void setDesrc(String desrc) {
        this.desrc = desrc;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public float getV() {
        return v;
    }

    public void setV(float v) {
        this.v = v;
    }
}
