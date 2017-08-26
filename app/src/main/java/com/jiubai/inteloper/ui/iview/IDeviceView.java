package com.jiubai.inteloper.ui.iview;

/**
 * Created by Larry Liang on 03/05/2017.
 */

public interface IDeviceView {
    void onGetDeviceListResult(boolean result, String info, Object extras);
    void onGetDeviceInfoResult(boolean result, String info, Object extras);
    void onEditDeviceInfoResult(boolean result, String info, Object extras);
    void onAddNewDeviceResult(boolean result, String info, Object extras);
}
