package com.jiubai.inteloper.bean;

/**
 * Created by larry on 31/08/2017.
 */

public class DeviceTelemetry {
    float voltageA;
    int voltageAQuality;
    float voltageB;
    int voltageBQuality;
    float voltageC;
    int voltageCQuality;
    float currentA;
    int currentAQuality;
    float currentB;
    int currentBQuality;
    float currentC;
    int currentCQuality;
    float temp;
    int tempQuality;

    public DeviceTelemetry() {
    }

    public DeviceTelemetry(float voltageA, int voltageAQuality, float voltageB, int voltageBQuality, float voltageC, int voltageCQuality, float currentA, int currentAQuality, float currentB, int currentBQuality, float currentC, int currentCQuality, float temp, int tempQuality) {
        this.voltageA = voltageA;
        this.voltageAQuality = voltageAQuality;
        this.voltageB = voltageB;
        this.voltageBQuality = voltageBQuality;
        this.voltageC = voltageC;
        this.voltageCQuality = voltageCQuality;
        this.currentA = currentA;
        this.currentAQuality = currentAQuality;
        this.currentB = currentB;
        this.currentBQuality = currentBQuality;
        this.currentC = currentC;
        this.currentCQuality = currentCQuality;
        this.temp = temp;
        this.tempQuality = tempQuality;
    }

    public float getVoltageA() {
        return voltageA;
    }

    public void setVoltageA(float voltageA) {
        this.voltageA = voltageA;
    }

    public int getVoltageAQuality() {
        return voltageAQuality;
    }

    public void setVoltageAQuality(int voltageAQuality) {
        this.voltageAQuality = voltageAQuality;
    }

    public float getVoltageB() {
        return voltageB;
    }

    public void setVoltageB(float voltageB) {
        this.voltageB = voltageB;
    }

    public int getVoltageBQuality() {
        return voltageBQuality;
    }

    public void setVoltageBQuality(int voltageBQuality) {
        this.voltageBQuality = voltageBQuality;
    }

    public float getVoltageC() {
        return voltageC;
    }

    public void setVoltageC(float voltageC) {
        this.voltageC = voltageC;
    }

    public int getVoltageCQuality() {
        return voltageCQuality;
    }

    public void setVoltageCQuality(int voltageCQuality) {
        this.voltageCQuality = voltageCQuality;
    }

    public float getCurrentA() {
        return currentA;
    }

    public void setCurrentA(float currentA) {
        this.currentA = currentA;
    }

    public int getCurrentAQuality() {
        return currentAQuality;
    }

    public void setCurrentAQuality(int currentAQuality) {
        this.currentAQuality = currentAQuality;
    }

    public float getCurrentB() {
        return currentB;
    }

    public void setCurrentB(float currentB) {
        this.currentB = currentB;
    }

    public int getCurrentBQuality() {
        return currentBQuality;
    }

    public void setCurrentBQuality(int currentBQuality) {
        this.currentBQuality = currentBQuality;
    }

    public float getCurrentC() {
        return currentC;
    }

    public void setCurrentC(float currentC) {
        this.currentC = currentC;
    }

    public int getCurrentCQuality() {
        return currentCQuality;
    }

    public void setCurrentCQuality(int currentCQuality) {
        this.currentCQuality = currentCQuality;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public int getTempQuality() {
        return tempQuality;
    }

    public void setTempQuality(int tempQuality) {
        this.tempQuality = tempQuality;
    }
}
