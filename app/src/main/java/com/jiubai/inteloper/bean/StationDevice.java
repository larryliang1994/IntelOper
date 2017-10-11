package com.jiubai.inteloper.bean;

import java.io.Serializable;

/**
 * Created by larry on 06/09/2017.
 */

public class StationDevice implements Serializable {
    private String name;

    public StationDevice() {
    }

    public StationDevice(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
