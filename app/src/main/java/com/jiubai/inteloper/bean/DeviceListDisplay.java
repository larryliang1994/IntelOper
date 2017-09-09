package com.jiubai.inteloper.bean;

/**
 * Created by larry on 07/09/2017.
 */

public class DeviceListDisplay {
    private String name;
    private String desc;
    private String firstWord;
    private boolean isIndex;

    public DeviceListDisplay() {
    }

    public DeviceListDisplay(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public DeviceListDisplay(String name, String desc, String firstWord, boolean isIndex) {
        this.name = name;
        this.desc = desc;
        this.firstWord = firstWord;
        this.isIndex = isIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
