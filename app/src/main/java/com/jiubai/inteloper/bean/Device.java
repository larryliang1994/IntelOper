package com.jiubai.inteloper.bean;

import android.support.annotation.NonNull;

/**
 * Created by Larry Liang on 30/04/2017.
 */

public class Device {

    private String name;
    private String firstWord;
    private boolean isIndex;

    public Device() {
    }

    public Device(String name, String firstWord) {
        this.name = name;
        this.firstWord = firstWord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstWord() {
        return firstWord;
    }

    public void setFirstWord(String firstWord) {
        this.firstWord = firstWord;
    }

    public boolean isIndex() {
        return isIndex;
    }

    public void setIndex(boolean index) {
        isIndex = index;
    }
}
