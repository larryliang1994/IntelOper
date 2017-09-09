package com.jiubai.inteloper.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by larry on 07/09/2017.
 */

public class Station implements Serializable {
    private String name;
    private String group;
    private String region;
    private String ip;
    private ArrayList<StationDevice> devices;

    public Station() {
    }

    public Station(String name, String region, String group) {
        this.name = name;
        this.region = region;
        this.group = group;
    }

    public Station(String name, String region, String group, String ip) {
        this.name = name;
        this.region = region;
        this.group = group;
        this.ip = ip;
    }

    public Station(String name, String region, String group, String ip, ArrayList<StationDevice> devices) {
        this.name = name;
        this.region = region;
        this.group = group;
        this.ip = ip;
        this.devices = devices;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public ArrayList<StationDevice> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<StationDevice> devices) {
        this.devices = devices;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
