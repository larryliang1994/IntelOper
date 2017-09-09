package com.jiubai.inteloper.presenter;

import com.jiubai.inteloper.bean.Station;
import com.jiubai.inteloper.bean.StationDevice;

/**
 * Created by larry on 07/09/2017.
 */

public interface IStationPresenter {
    void getStationList();
    void getStationInfo(String name);
    void editStationInfo(Station sourceStation, Station newStation);
    void addStation(Station station);
    void getRegionList();
    void getGroupList();
    void addDevice(StationDevice stationDevice);
    void getDeviceList(Station station);
}
