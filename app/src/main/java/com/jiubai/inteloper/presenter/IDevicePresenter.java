package com.jiubai.inteloper.presenter;

import com.jiubai.inteloper.bean.Device;

/**
 * Created by Larry Liang on 03/05/2017.
 */

public interface IDevicePresenter {
    void getDeviceList();
    void getDeviceInfo(String name);
    void editDeviceInfo(Device sourceDevice, Device newDevice);
    void addNewDevice(Device device);
    void getDeviceTelemetry(String name);
    void getDeviceTelecommend(String name);
}
