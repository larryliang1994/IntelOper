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
            final Station station = new Station(name, "区域1", "班组1", "192.168.1.1");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIStationView.onGetStationInfoResult(true, "", station);
                }
            }, 1000);
        }
    }

    @Override
    public void editStationInfo(Station sourceStation, Station newStation) {

    }

    @Override
    public void addStation(Station station) {
        if (!Config.NO_NETWORK) {
            byte[] requestCode = DataTypeConverter.int2byte(25); // 操作码
            byte[] msgNum = DataTypeConverter.int2byte(1); // 消息数

            Charset cs = Charset.forName("GBK");
            byte[] group = station.getGroup().getBytes(cs);
            byte[] group_offset = new byte[65 - group.length];
            final byte[] region = station.getRegion().getBytes(cs);
            byte[] region_offset = new byte[65 - region.length];
            byte[] name = station.getName().getBytes(cs);
            byte[] name_offset = new byte[65 - name.length];
            byte[] ip = station.getIp().getBytes(cs);
            byte[] ip_offset = new byte[65 - ip.length];

            // 把所有字节合并成一条
            byte[] input = DataTypeConverter.concatAll(requestCode, msgNum,
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
                                        mIStationView.onAddStationResult(true, "", "");
                                    } else {
                                        mIStationView.onAddStationResult(false, "添加厂站失败", "");
                                    }
                                }
                            });
                        }

                        @Override
                        public void error(final String info, final Exception exception) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIStationView.onAddStationResult(false, "添加厂站失败", exception);
                                }
                            });
                        }
                    });
        } else {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIStationView.onAddStationResult(true, "", "");
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
    public void addDevice(StationDevice stationDevice) {

    }

    @Override
    public void getDeviceList(Station station) {

    }
}
