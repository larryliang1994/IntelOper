package com.jiubai.inteloper.presenter;

import android.app.Activity;
import android.os.Handler;

import com.jiubai.inteloper.bean.Station;
import com.jiubai.inteloper.bean.StationDevice;
import com.jiubai.inteloper.common.DataTypeConverter;
import com.jiubai.inteloper.config.Config;
import com.jiubai.inteloper.net.RequestUtil;
import com.jiubai.inteloper.ui.iview.IStationView;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by larry on 07/09/2017.
 */

public class StationPresenterImpl implements IStationPresenter {
    public static final int STATION_DEVICE_OPT_TYPE_ADD = 1;
    public static final int STATION_DEVICE_OPT_TYPE_DELETE = 2;
    public static final int STATION_DEVICE_OPT_TYPE_EDIT = 3;

    public static final int STATION_OPT_TYPE_ADD = 1;
    public static final int STATION_OPT_TYPE_DELETE = 2;
    public static final int STATION_OPT_TYPE_EDIT = 3;

    private IStationView mIStationView;
    private Activity mActivity;

    public StationPresenterImpl(IStationView mIStationView, Activity mActivity) {
        this.mIStationView = mIStationView;
        this.mActivity = mActivity;
    }

    @Override
    public void getStationList() {
        if (!Config.NO_NETWORK) {
            byte[] requestCode = DataTypeConverter.int2byte(19); // 操作码
            byte[] msgNum = DataTypeConverter.int2byte(1); // 消息数
            // 把所有字节合并成一条
            byte[] input = DataTypeConverter.concatAll(requestCode, msgNum);
            final int requestMsgLength = 64 * 3;

            RequestUtil.request(input, 20, requestMsgLength, true,
                    new RequestUtil.RequestCallback() {
                        @Override
                        public void success(int msgNum, byte[] msgContent) {

                            final List<Station> stations = new ArrayList<>();

                            byte[] name;
                            byte[] group;
                            byte[] region;

                            for (int i = 0; i < msgNum; i++) {

                                name = DataTypeConverter.readBytes(msgContent, i * requestMsgLength , 64);
                                group = DataTypeConverter.readBytes(msgContent, i * requestMsgLength + 64 * 1, 64);
                                region = DataTypeConverter.readBytes(msgContent, i * requestMsgLength + 64 * 2, 64);

                                int name_index = 0, group_index = 0, region_index = 0;
                                for(int j = 0; j < 64; j++) {
                                    if (name[j] == 0) {
                                        name_index = j;
                                        break;
                                    }
                                }

                                for(int j = 0; j < 64; j++) {
                                    if (group[j] == 0) {
                                        group_index = j;
                                        break;
                                    }
                                }

                                for(int j = 0; j < 64; j++) {
                                    if (region[j] == 0) {
                                        region_index = j;
                                        break;
                                    }
                                }

                                Charset charset = Charset.forName("GBK");

                                stations.add(new Station(
                                        new String(DataTypeConverter.readBytes(name, 0, name_index), charset),
                                        new String(DataTypeConverter.readBytes(region, 0, region_index), charset),
                                        new String(DataTypeConverter.readBytes(group, 0, group_index), charset)
                                ));
                            }

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIStationView.onGetStationListResult(true, "", stations);
                                }
                            });
                        }

                        @Override
                        public void error(final String info, final Exception exception) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIStationView.onGetStationListResult(false, "获取厂站列表失败", exception);
                                }
                            });
                        }
                    });
        } else {
            final List<Station> stations = new ArrayList<>();

            for (int i = 0; i < 20; i++) {

                stations.add(new Station("厂站" + i, "区域" + i, "班组" + i));
            }

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mIStationView.onGetStationListResult(true, "", stations);
                        }
                    }, 1000);
                }
            });
        }
    }

    @Override
    public void getStationInfo(String name) {
        if (!Config.NO_NETWORK) {
            byte[] requestCode = DataTypeConverter.int2byte(27); // 操作码
            byte[] msgNum = DataTypeConverter.int2byte(1); // 消息数

            Charset cs = Charset.forName("GBK");
            byte[] b1 = name.getBytes(cs);
            byte[] b2 = new byte[64 - b1.length];

            // 把所有字节合并成一条
            byte[] input = DataTypeConverter.concatAll(requestCode, msgNum, b1, b2);
            final int requestMsgLength = 65 * 4;

            RequestUtil.request(input, 28, requestMsgLength, false,
                    new RequestUtil.RequestCallback() {
                        @Override
                        public void success(int msgNum, byte[] msgContent) {
                            byte[] group;
                            byte[] region;
                            byte[] name;
                            byte[] ip;

                            group  = DataTypeConverter.readBytes(msgContent, requestMsgLength + 65 * 0, 65);
                            region = DataTypeConverter.readBytes(msgContent, requestMsgLength + 65 * 1, 65);
                            name   = DataTypeConverter.readBytes(msgContent, requestMsgLength + 65 * 2, 65);
                            ip     = DataTypeConverter.readBytes(msgContent, requestMsgLength + 65 * 3, 65);

                            int group_index = 0, region_index = 0, name_index = 0, ip_index = 0;
                            for(int j = 0; j < 65; j++) {
                                if (group[j] == 0) {
                                    group_index = j;
                                    break;
                                }
                            }

                            for(int j = 0; j < 65; j++) {
                                if (region[j] == 0) {
                                    region_index = j;
                                    break;
                                }
                            }

                            for(int j = 0; j < 65; j++) {
                                if (name[j] == 0) {
                                    name_index = j;
                                    break;
                                }
                            }

                            for(int j = 0; j < 65; j++) {
                                if (ip[j] == 0) {
                                    ip_index = j;
                                    break;
                                }
                            }

                            Charset charset = Charset.forName("GBK");

                            final Station station = new Station(
                                    new String(DataTypeConverter.readBytes(name, 0, name_index), charset),
                                    new String(DataTypeConverter.readBytes(region, 0, region_index), charset),
                                    new String(DataTypeConverter.readBytes(group, 0, group_index), charset),
                                    new String(DataTypeConverter.readBytes(ip, 0, ip_index), charset)
                            );

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIStationView.onGetStationInfoResult(true, "", station);
                                }
                            });
                        }

                        @Override
                        public void error(final String info, final Exception exception) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIStationView.onGetStationInfoResult(false, "获取厂站信息失败", exception);
                                }
                            });
                        }
                    });
        } else {
            final Station station = new Station(name, "区域1号", "班组1号", "192.168.1.1");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIStationView.onGetStationInfoResult(true, "", station);
                }
            }, 1000);
        }
    }

    @Override
    public void editStationInfo(Station station, int optType) {
        if (!Config.NO_NETWORK) {
            byte[] requestCode = DataTypeConverter.int2byte(25); // 操作码
            byte[] msgNum = DataTypeConverter.int2byte(1); // 消息数

            Charset cs = Charset.forName("GBK");
            byte[] opt = DataTypeConverter.int2byte(optType);
            byte[] group = station.getGroup().getBytes(cs);
            byte[] group_offset = new byte[65 - group.length];
            final byte[] region = station.getRegion().getBytes(cs);
            byte[] region_offset = new byte[65 - region.length];
            byte[] name = station.getName().getBytes(cs);
            byte[] name_offset = new byte[65 - name.length];
            byte[] ip = station.getIp().getBytes(cs);
            byte[] ip_offset = new byte[65 - ip.length];

            // 把所有字节合并成一条
            byte[] input = DataTypeConverter.concatAll(requestCode, msgNum, opt,
                    group, group_offset, region, region_offset, name, name_offset, ip, ip_offset);
            final int requestMsgLength = 4;

            RequestUtil.request(input, 26, requestMsgLength, false,
                    new RequestUtil.RequestCallback() {
                        @Override
                        public void success(int msgNum, byte[] msgContent) {

                            final int result = DataTypeConverter.byte2int(DataTypeConverter.readBytes(msgContent, 0, 4));

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (result == 1) {
                                        mIStationView.onEditStationInfoResult(true, "", "");
                                    } else if (result == -1) {
                                        mIStationView.onEditStationInfoResult(false, "该厂站已存在", "");
                                    } else {
                                        mIStationView.onEditStationInfoResult(false, "修改厂站信息失败", "");
                                    }
                                }
                            });
                        }

                        @Override
                        public void error(final String info, final Exception exception) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIStationView.onEditStationInfoResult(false, "修改厂站信息失败", exception);
                                }
                            });
                        }
                    });
        } else {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mIStationView.onEditStationInfoResult(true, "", "");
                        }
                    }, 1000);
                }
            });
        }
    }

    @Override
    public void getRegionList() {
        if (!Config.NO_NETWORK) {
            byte[] requestCode = DataTypeConverter.int2byte(23); // 操作码
            byte[] msgNum = DataTypeConverter.int2byte(1); // 消息数
            // 把所有字节合并成一条
            byte[] input = DataTypeConverter.concatAll(requestCode, msgNum);
            final int requestMsgLength = 64;

            RequestUtil.request(input, 24, requestMsgLength, true,
                    new RequestUtil.RequestCallback() {
                        @Override
                        public void success(int msgNum, byte[] msgContent) {

                            final String[] regions = new String[msgNum];

                            byte[] name;

                            for (int i = 0; i < msgNum; i++) {

                                name = DataTypeConverter.readBytes(msgContent, i * requestMsgLength , 64);

                                int name_index = 0;
                                for(int j = 0; j < 64; j++) {
                                    if (name[j] == 0) {
                                        name_index = j;
                                        break;
                                    }
                                }

                                Charset charset = Charset.forName("GBK");

                                regions[i] = new String(DataTypeConverter.readBytes(name, 0, name_index), charset);
                            }

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIStationView.onGetRegionListResult(true, "", regions);
                                }
                            });
                        }

                        @Override
                        public void error(final String info, final Exception exception) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIStationView.onGetRegionListResult(true, "获取班组列表失败", exception);
                                }
                            });
                        }
                    });
        } else {
            final String[] regions = new String[10];

            for (int i = 0; i < 10; i++) {
                regions[i] = "区域" + i + "号";
            }

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mIStationView.onGetRegionListResult(true, "", regions);
                        }
                    }, 1000);
                }
            });
        }
    }

    @Override
    public void getGroupList() {
        if (!Config.NO_NETWORK) {
            byte[] requestCode = DataTypeConverter.int2byte(21); // 操作码
            byte[] msgNum = DataTypeConverter.int2byte(1); // 消息数
            // 把所有字节合并成一条
            byte[] input = DataTypeConverter.concatAll(requestCode, msgNum);
            final int requestMsgLength = 64;

            RequestUtil.request(input, 22, requestMsgLength, true,
                    new RequestUtil.RequestCallback() {
                        @Override
                        public void success(int msgNum, byte[] msgContent) {

                            final String[] groups = new String[msgNum];

                            byte[] name;

                            for (int i = 0; i < msgNum; i++) {

                                name = DataTypeConverter.readBytes(msgContent, i * requestMsgLength , 64);

                                int name_index = 0;
                                for(int j = 0; j < 64; j++) {
                                    if (name[j] == 0) {
                                        name_index = j;
                                        break;
                                    }
                                }

                                Charset charset = Charset.forName("GBK");

                                groups[i] = new String(DataTypeConverter.readBytes(name, 0, name_index), charset);
                            }

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIStationView.onGetGroupListResult(true, "", groups);
                                }
                            });
                        }

                        @Override
                        public void error(final String info, final Exception exception) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIStationView.onGetGroupListResult(true, "获取班组列表失败", exception);
                                }
                            });
                        }
                    });
        } else {
            final String[] groups = new String[10];

            for (int i = 0; i < 10; i++) {
                groups[i] = "班组" + i + "号";
            }

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mIStationView.onGetGroupListResult(true, "", groups);
                        }
                    }, 1000);
                }
            });
        }
    }

    @Override
    public void editDeviceInfo(StationDevice stationDevice, String stationName, int optType) {
        if (!Config.NO_NETWORK) {
            byte[] requestCode = DataTypeConverter.int2byte(12); // 操作码
            byte[] msgNum = DataTypeConverter.int2byte(1); // 消息数

            Charset cs = Charset.forName("GBK");
            byte[] opt = DataTypeConverter.int2byte(optType);

            byte[] name = stationDevice.getName().getBytes(cs);
            byte[] name_offset = new byte[65 - name.length];

            byte[] station = stationName.getBytes(cs);
            byte[] station_offset = new byte[station.length];

            byte[] rtu = stationDevice.getRtu().getBytes(cs);
            byte[] rtu_offset = new byte[65 - rtu.length];

            // 把所有字节合并成一条
            byte[] input = DataTypeConverter.concatAll(requestCode, msgNum, opt,
                    name, name_offset, station, station_offset, rtu, rtu_offset);
            final int requestMsgLength = 4;

            RequestUtil.request(input, 13, requestMsgLength, false,
                    new RequestUtil.RequestCallback() {
                        @Override
                        public void success(int msgNum, byte[] msgContent) {
                            int success = DataTypeConverter.byte2int(msgContent);

                            if (success == 1) {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mIStationView.onEditStationDeviceResult(true, "", "");
                                    }
                                });
                            } else {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mIStationView.onEditStationDeviceResult(false, "修改设备信息失败", "");
                                    }
                                });
                            }
                        }

                        @Override
                        public void error(final String info, final Exception exception) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIStationView.onEditStationDeviceResult(false, "修改设备信息失败", exception);
                                }
                            });
                        }
                    });
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIStationView.onEditStationDeviceResult(true, "", "");
                }
            }, 500);
        }
    }

    @Override
    public void getDeviceList(String name) {
        if (!Config.NO_NETWORK) {
            byte[] requestCode = DataTypeConverter.int2byte(8); // 操作码
            byte[] msgNum = DataTypeConverter.int2byte(1); // 消息数

            Charset cs = Charset.forName("GBK");
            byte[] stationName = name.getBytes(cs);
            byte[] stationName_offset = new byte[65 - stationName.length];

            // 把所有字节合并成一条
            byte[] input = DataTypeConverter.concatAll(requestCode, msgNum, stationName, stationName_offset);
            final int requestMsgLength = 64 * 4;

            RequestUtil.request(input, 9, requestMsgLength, true,
                    new RequestUtil.RequestCallback() {
                        @Override
                        public void success(int msgNum, byte[] msgContent) {
                            final List<StationDevice> deviceList = new ArrayList<>();

                            byte[] name;
                            byte[] station;
                            byte[] region;
                            byte[] group;
                            byte[] rtu;

                            for (int i = 0; i < msgNum; i++) {

                                name = DataTypeConverter.readBytes(msgContent, i * requestMsgLength , 64);
                                station = DataTypeConverter.readBytes(msgContent, i * requestMsgLength + 64 * 1, 64);
                                region = DataTypeConverter.readBytes(msgContent, i * requestMsgLength + 64 * 2, 64);
                                group = DataTypeConverter.readBytes(msgContent, i * requestMsgLength + 64 * 3, 64);
                                rtu = DataTypeConverter.readBytes(msgContent, i * requestMsgLength + 64 * 4, 64);

                                int name_index = 0, station_index = 0, region_index = 0, group_index = 0, rtu_index = 0;
                                for(int j = 0; j < 64; j++) {
                                    if (name[j] == 0) {
                                        name_index = j;
                                        break;
                                    }
                                }

                                for(int j = 0; j < 64; j++) {
                                    if (station[j] == 0) {
                                        station_index = j;
                                        break;
                                    }
                                }

                                for(int j = 0; j < 64; j++) {
                                    if (region[j] == 0) {
                                        region_index = j;
                                        break;
                                    }
                                }

                                for(int j = 0; j < 64; j++) {
                                    if (group[j] == 0) {
                                        group_index = j;
                                        break;
                                    }
                                }

                                for(int j = 0; j < 64; j++) {
                                    if (group[j] == 0) {
                                        group_index = j;
                                        break;
                                    }
                                }

                                for(int j = 0; j < 64; j++) {
                                    if (rtu[j] == 0) {
                                        rtu_index = j;
                                        break;
                                    }
                                }

                                Charset charset = Charset.forName("GBK");

                                StationDevice stationDevice = new StationDevice(
                                        new String(DataTypeConverter.readBytes(name, 0, name_index), charset),
                                        new String(DataTypeConverter.readBytes(rtu, 0, rtu_index), charset)
                                );

                                deviceList.add(stationDevice);
                            }

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIStationView.onGetStationDeviceListResult(true, "", deviceList);
                                }
                            });
                        }

                        @Override
                        public void error(final String info, final Exception exception) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIStationView.onGetStationDeviceListResult(false, "获取厂站设备列表失败", exception);
                                }
                            });
                        }
                    });
        } else {
            final List<StationDevice> list = new ArrayList<>();

            list.add(new StationDevice("加内特", "RTU"));
            list.add(new StationDevice("韦德", "RTU"));
            list.add(new StationDevice("詹姆斯", "RTU"));
            list.add(new StationDevice("安东尼", "RTU"));
            list.add(new StationDevice("科比", "RTU"));
            list.add(new StationDevice("乔丹", "RTU"));
            list.add(new StationDevice("奥尼尔", "RTU"));
            list.add(new StationDevice("麦格雷迪", "RTU"));
            list.add(new StationDevice("艾弗森", "RTU"));
            list.add(new StationDevice("哈达威", "RTU"));
            list.add(new StationDevice("纳什", "RTU"));
            list.add(new StationDevice("弗朗西斯", "RTU"));
            list.add(new StationDevice("姚明", "RTU"));
            list.add(new StationDevice("库里", "RTU"));
            list.add(new StationDevice("邓肯", "RTU"));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIStationView.onGetStationDeviceListResult(true, "", list);
                }
            }, 500);
        }
    }
}
