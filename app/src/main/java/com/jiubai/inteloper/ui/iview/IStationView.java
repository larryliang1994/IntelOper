package com.jiubai.inteloper.ui.iview;

/**
 * Created by larry on 08/09/2017.
 */

public interface IStationView {
    void onGetStationListResult(boolean result, String info, Object extras);
    void onGetStationInfoResult(boolean result, String info, Object extras);
    void onEditStationInfoResult(boolean result, String info, Object extras);
    void onGetRegionListResult(boolean result, String info, Object extras);
    void onGetGroupListResult(boolean result, String info, Object extras);
    void onEditStationDeviceResult(boolean result, String info, Object extras);
    void onGetStationDeviceListResult(boolean result, String info, Object extras);
}
