package com.jiubai.inteloper.bean;

import java.util.ArrayList;

/**
 * Created by Larry Liang on 30/04/2017.
 */

public class Device {

    private String id;
    private String name;
    private String firstWord;
    private boolean isIndex;

    private ArrayList<Attr> attrs;

    public Device() {
    }

    public Device(String id, String name) {
        this.id = id;
        this.name = name;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Attr> getAttrs() {
        return attrs;
    }

    public void setAttrs(ArrayList<Attr> attrs) {
        this.attrs = attrs;
    }
}
