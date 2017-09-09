package com.jiubai.inteloper.bean;

/**
 * Created by larry on 06/09/2017.
 */

public class StationDevice {
    private String name;
    private String rtu;

    public StationDevice() {
    }

    public StationDevice(String name, String rtu) {
        this.name = name;
        this.rtu = rtu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRtu() {
        return rtu;
    }

    public void setRtu(String rtu) {
        this.rtu = rtu;
    }
}
