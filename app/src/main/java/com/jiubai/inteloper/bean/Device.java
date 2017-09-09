package com.jiubai.inteloper.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Larry Liang on 30/04/2017.
 */

public class Device implements Serializable {

    private String id;
    private String name;
    private String desc;

    private ArrayList<Attr> attrs;

    public Device() {
    }

    public Device(String id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
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
