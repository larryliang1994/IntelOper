package com.jiubai.inteloper.presenter;

import android.app.Activity;
import android.os.Handler;

import com.jiubai.inteloper.bean.Attr;
import com.jiubai.inteloper.bean.Device;
import com.jiubai.inteloper.bean.DeviceTelemetry;
import com.jiubai.inteloper.common.DataTypeConverter;
import com.jiubai.inteloper.config.Config;
import com.jiubai.inteloper.net.RequestUtil;
import com.jiubai.inteloper.ui.iview.IDeviceView;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry Liang on 03/05/2017.
 */

public class DevicePresenterImpl implements IDevicePresenter {

    private IDeviceView mIDeviceView;
    private Activity mActivity;

    public DevicePresenterImpl(Activity activity, IDeviceView iDeviceView) {
        mActivity = activity;
        mIDeviceView = iDeviceView;
    }

    @Override
    public void getDeviceList() {
        if (!Config.NO_NETWORK) {
            byte[] requestCode = DataTypeConverter.int2byte(8); // 操作码
            byte[] msgNum = DataTypeConverter.int2byte(1); // 消息数
            // 把所有字节合并成一条
            byte[] input = DataTypeConverter.concatAll(requestCode, msgNum);
            final int requestMsgLength = 64 * 4;

            RequestUtil.request(input, 9, requestMsgLength, true,
                    new RequestUtil.RequestCallback() {
                        @Override
                        public void success(int msgNum, byte[] msgContent) {


                            final List<Device> deviceList = new ArrayList<>();

                            byte[] id;
                            byte[] name;
                            byte[] name2, name3, name4;

                            for (int i = 0; i < msgNum; i++) {

                                //id = DataTypeConverter.readBytes(msgContent, i * requestMsgLength, 19);
                                name = DataTypeConverter.readBytes(msgContent, i * requestMsgLength , 64);
                                name2 = DataTypeConverter.readBytes(msgContent, i * requestMsgLength + 64 * 1, 64);
                                name3 = DataTypeConverter.readBytes(msgContent, i * requestMsgLength + 64 * 2, 64);
                                name4 = DataTypeConverter.readBytes(msgContent, i * requestMsgLength + 64 * 3, 64);

                                int index = 0;
                                for(int j = 0; j < 64; j++) {
                                    if (name[j] == 0) {
                                        index = j;
                                        break;
                                    }
                                }

                                String desc = "";

                                int index2 = 0, index3 = 0, index4 = 0;
                                for(int j = 0; j < 64; j++) {
                                    if (name2[j] == 0) {
                                        index2 = j;
                                        break;
                                    }
                                }

                                for(int j = 0; j < 64; j++) {
                                    if (name3[j] == 0) {
                                        index3 = j;
                                        break;
                                    }
                                }

                                for(int j = 0; j < 64; j++) {
                                    if (name4[j] == 0) {
                                        index4 = j;
                                        break;
                                    }
                                }

                                Charset charset = Charset.forName("GBK");

                                desc += new String(DataTypeConverter.readBytes(name2, 0, index2), charset) + "/";
                                desc += new String(DataTypeConverter.readBytes(name3, 0, index3), charset) + "/";
                                desc += new String(DataTypeConverter.readBytes(name4, 0, index4), charset);

                                deviceList.add(new Device("",
                                        new String(DataTypeConverter.readBytes(name, 0, index), charset), desc));
                            }

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIDeviceView.onGetDeviceListResult(true, "", deviceList);
                                }
                            });
                        }

                        @Override
                        public void error(final String info, final Exception exception) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIDeviceView.onGetDeviceListResult(false, "获取设备列表失败", exception);
                                }
                            });
                        }
                    });
        } else {
            final List<Device> list = new ArrayList<>();

            list.add(new Device("12", "加内特11", "某区某责任段"));
            list.add(new Device("12", "韦德33", "某区某责任段"));
            list.add(new Device("12", "詹姆斯", "某区某责任段"));
            list.add(new Device("12", "安东尼", "某区某责任段"));
            list.add(new Device("12", "科比", "某区某责任段"));
            list.add(new Device("12", "乔丹", "某区某责任段"));
            list.add(new Device("12", "奥尼尔", "某区某责任段"));
            list.add(new Device("12", "麦格雷迪", "某区某责任段"));
            list.add(new Device("12", "艾弗森", "某区某责任段"));
            list.add(new Device("12", "哈达威", "某区某责任段"));
            list.add(new Device("12", "纳什", "某区某责任段"));
            list.add(new Device("12", "弗朗西斯", "某区某责任段"));
            list.add(new Device("12", "姚明", "某区某责任段"));
            list.add(new Device("12", "库里", "某区某责任段"));
            list.add(new Device("12", "邓肯", "某区某责任段"));
            list.add(new Device("12", "吉诺比利", "某区某责任段"));
            list.add(new Device("12", "帕克", "某区某责任段"));
            list.add(new Device("12", "杜兰特", "某区某责任段"));
            list.add(new Device("12", "韦伯", "某区某责任段"));
            list.add(new Device("12", "威斯布鲁克", "某区某责任段"));
            list.add(new Device("12", "霍华德", "某区某责任段"));
            list.add(new Device("12", "保罗", "某区某责任段"));
            list.add(new Device("12", "罗斯", "某区某责任段"));
            list.add(new Device("12", "加索尔", "某区某责任段"));
            list.add(new Device("12", "隆多", "某区某责任段"));
            list.add(new Device("12", "诺维斯基", "某区某责任段"));
            list.add(new Device("12", "格里芬", "某区某责任段"));
            list.add(new Device("12", "波什", "某区某责任段"));
            list.add(new Device("12", "伊戈达拉", "某区某责任段"));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIDeviceView.onGetDeviceListResult(true, "", list);
                }
            }, 500);
        }
    }

    @Override
    public void getDeviceInfo(String name) {
        byte[] requestCode = DataTypeConverter.int2byte(14); // 操作码
        byte[] msgNum = DataTypeConverter.int2byte(1); // 消息数

        Charset cs = Charset.forName("GBK");
        byte[] b1 = name.getBytes(cs);
        byte[] b2 = new byte[64 - b1.length];

        // 把所有字节合并成一条
        byte[] input = DataTypeConverter.concatAll(requestCode, msgNum, b1, b2);
        final int requestMsgLength = 64 + 4 + 4 + 4 + 4;

        RequestUtil.request(input, 15, requestMsgLength, false,
                new RequestUtil.RequestCallback() {
                    @Override
                    public void success(int msgNum, byte[] msgContent) {
                        final Device device = new Device();

                        ArrayList<Attr> attrs = new ArrayList<>();

                        byte[] name;
                        byte[] attrType;
                        byte[] isExist;
                        byte[] upValue;
                        byte[] downValue;

                        for (int i = 0; i < msgNum; i++) {

                            name = DataTypeConverter.readBytes(msgContent, i * requestMsgLength, 64);
                            attrType = DataTypeConverter.readBytes(msgContent, i * requestMsgLength + 64, 4);
                            isExist = DataTypeConverter.readBytes(msgContent, i * requestMsgLength + 64 + 4, 4);
                            upValue = DataTypeConverter.readBytes(msgContent, i * requestMsgLength + 64 + 4 + 4, 4);
                            downValue = DataTypeConverter.readBytes(msgContent, i * requestMsgLength + 64 + 4 + 4 + 4, 4);

                            attrs.add(new Attr(
                                    DataTypeConverter.byte2int(attrType),
                                    DataTypeConverter.byte2int(isExist) == 1,
                                    DataTypeConverter.byte2float(upValue),
                                    DataTypeConverter.byte2float(downValue)
                            ));
                        }

                        device.setAttrs(attrs);

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mIDeviceView.onGetDeviceInfoResult(true, "", device);
                            }
                        });
                    }

                    @Override
                    public void error(final String info, final Exception exception) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mIDeviceView.onGetDeviceInfoResult(false, "设备属性不存在", exception);
                            }
                        });
                    }
                });
    }

    @Override
    public void editDeviceInfo(final Device sourceDevice, Device newDevice) {
        byte[] input = getEditDeviceInput(sourceDevice, newDevice);

        int msgLength = 4;

        RequestUtil.request(input, 11, msgLength, false,
                new RequestUtil.RequestCallback() {
                    @Override
                    public void success(int msgNum, byte[] msgContent) {
                        int success = DataTypeConverter.byte2int(msgContent);

                        if (success == 1) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIDeviceView.onEditDeviceInfoResult(true, "", "");
                                }
                            });
                        } else {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIDeviceView.onEditDeviceInfoResult(false, "修改设备信息失败", "");
                                }
                            });
                        }
                    }

                    @Override
                    public void error(String info, final Exception exception) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mIDeviceView.onEditDeviceInfoResult(false, "修改设备信息失败", exception);
                            }
                        });
                    }
                });
    }

    @Override
    public void addNewDevice(final Device device) {
        byte[] requestCode = DataTypeConverter.int2byte(12); // 操作码
        byte[] msgNum = DataTypeConverter.int2byte(1); // 消息数

        Charset cs = Charset.forName("GBK");
        byte[] b1 = device.getName().getBytes(cs);
        byte[] b2 = new byte[64 - b1.length];

        // 把所有字节合并成一条
        byte[] input = DataTypeConverter.concatAll(requestCode, msgNum, b1, b2);
        final int requestMsgLength = 4;

        RequestUtil.request(input, 13, requestMsgLength, false,
                new RequestUtil.RequestCallback() {
                    @Override
                    public void success(int msgNum, byte[] msgContent) {
                        int success = DataTypeConverter.byte2int(msgContent);

                        if (success == 1) {
                            byte[] input = getEditDeviceInput(null, device);

                            int msgLength = 4;

                            RequestUtil.request(input, 11, msgLength, false,
                                    new RequestUtil.RequestCallback() {
                                        @Override
                                        public void success(int msgNum, byte[] msgContent) {
                                            int success = DataTypeConverter.byte2int(msgContent);

                                            if (success == 1) {
                                                mActivity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mIDeviceView.onAddNewDeviceResult(true, "", "");
                                                    }
                                                });
                                            } else {
                                                mActivity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mIDeviceView.onAddNewDeviceResult(false, "添加新设备失败", "");

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void error(String info, final Exception exception) {
                                            mActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mIDeviceView.onAddNewDeviceResult(false, "添加新设备失败", exception);
                                                }
                                            });
                                        }
                                    });
                        } else {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIDeviceView.onAddNewDeviceResult(false, "添加新设备失败", "");
                                }
                            });
                        }
                    }

                    @Override
                    public void error(String info, final Exception exception) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mIDeviceView.onAddNewDeviceResult(false, "添加新设备失败", exception);
                            }
                        });
                    }
                });
    }

    @Override
    public void getDeviceTelemetry(String name) {
        byte[] requestCode = DataTypeConverter.int2byte(1); // 操作码
        byte[] msgNum = DataTypeConverter.int2byte(1); // 消息数

        final Charset cs = Charset.forName("GBK");
        byte[] b1 = name.getBytes(cs);
        byte[] b2 = new byte[64 - b1.length];

        // 把所有字节合并成一条
        byte[] input = DataTypeConverter.concatAll(requestCode, msgNum, b1, b2);
        final int requestMsgLength = 4 * 14;

        RequestUtil.request(input, 2, requestMsgLength, false,
                new RequestUtil.RequestCallback() {
                    @Override
                    public void success(int msgNum, byte[] msgContent) {

                        byte[] voltageA;
                        byte[] voltageAQuality;
                        byte[] voltageB;
                        byte[] voltageBQuality;
                        byte[] voltageC;
                        byte[] voltageCQuality;
                        byte[] currentA;
                        byte[] currentAQuality;
                        byte[] currentB;
                        byte[] currentBQuality;
                        byte[] currentC;
                        byte[] currentCQuality;
                        byte[] temp;
                        byte[] tempQuality;

                        if (msgNum == 0) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIDeviceView.onGetDeviceTelemetryResult(false, "数据为空", "");
                                }
                            });
                        } else {
                            voltageA = DataTypeConverter.readBytes(msgContent, 4 * 0, 4);
                            voltageAQuality = DataTypeConverter.readBytes(msgContent, 4 * 1, 4);
                            voltageB = DataTypeConverter.readBytes(msgContent, 4 * 2, 4);
                            voltageBQuality = DataTypeConverter.readBytes(msgContent, 4 * 3, 4);
                            voltageC = DataTypeConverter.readBytes(msgContent, 4 * 4, 4);
                            voltageCQuality = DataTypeConverter.readBytes(msgContent, 4 * 5, 4);
                            currentA = DataTypeConverter.readBytes(msgContent, 4 * 6, 4);
                            currentAQuality = DataTypeConverter.readBytes(msgContent, 4 * 7, 4);
                            currentB = DataTypeConverter.readBytes(msgContent, 4 * 8, 4);
                            currentBQuality = DataTypeConverter.readBytes(msgContent, 4 * 9, 4);
                            currentC = DataTypeConverter.readBytes(msgContent, 4 * 10, 4);
                            currentCQuality = DataTypeConverter.readBytes(msgContent, 4 * 11, 4);
                            temp = DataTypeConverter.readBytes(msgContent, 4 * 12, 4);
                            tempQuality = DataTypeConverter.readBytes(msgContent, 4 * 13, 4);

                            final DeviceTelemetry deviceTelemetry = new DeviceTelemetry(
                                    DataTypeConverter.byte2float(voltageA),
                                    DataTypeConverter.byte2int(voltageAQuality),
                                    DataTypeConverter.byte2float(voltageB),
                                    DataTypeConverter.byte2int(voltageBQuality),
                                    DataTypeConverter.byte2float(voltageC),
                                    DataTypeConverter.byte2int(voltageCQuality),
                                    DataTypeConverter.byte2float(currentA),
                                    DataTypeConverter.byte2int(currentAQuality),
                                    DataTypeConverter.byte2float(currentB),
                                    DataTypeConverter.byte2int(currentBQuality),
                                    DataTypeConverter.byte2float(currentC),
                                    DataTypeConverter.byte2int(currentCQuality),
                                    DataTypeConverter.byte2float(temp),
                                    DataTypeConverter.byte2int(tempQuality)
                            );

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIDeviceView.onGetDeviceTelemetryResult(true, "", deviceTelemetry);
                                }
                            });
                        }


                    }

                    @Override
                    public void error(String info, final Exception exception) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mIDeviceView.onGetDeviceTelemetryResult(false, "获取实时遥测数据失败", exception);
                            }
                        });
                    }
                });
    }

    @Override
    public void getDeviceTelecommend(String name) {
        byte[] requestCode = DataTypeConverter.int2byte(17); // 操作码
        byte[] msgNum = DataTypeConverter.int2byte(1); // 消息数

        final Charset cs = Charset.forName("GBK");
        byte[] b1 = name.getBytes(cs);
        byte[] b2 = new byte[64 - b1.length];

        // 把所有字节合并成一条
        byte[] input = DataTypeConverter.concatAll(requestCode, msgNum, b1, b2);
        final int requestMsgLength = 4 * 26;

        RequestUtil.request(input, 18, requestMsgLength, false,
                new RequestUtil.RequestCallback() {
                    @Override
                    public void success(int msgNum, byte[] msgContent) {

                        byte[] value;

                        final ArrayList<Integer> values = new ArrayList<>();

                        if (msgNum == 0) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIDeviceView.onGetDeviceTelecommendResult(false, "数据为空", "");
                                }
                            });
                        } else {
                            for (int i = 0; i < 26; i++) {
                                value = DataTypeConverter.readBytes(msgContent, i * 4, 4);

                                values.add(DataTypeConverter.byte2int(value));
                            }

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIDeviceView.onGetDeviceTelecommendResult(true, "", values);
                                }
                            });
                        }

                    }

                    @Override
                    public void error(String info, final Exception exception) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mIDeviceView.onGetDeviceTelecommendResult(false, "获取实时遥信数据失败", exception);
                            }
                        });
                    }
                });
    }

    private byte[] getEditDeviceInput(final Device sourceDevice, Device newDevice) {
        byte[] requestCode = DataTypeConverter.int2byte(10); // 操作码

        int iMsgNum = 0;

        byte[] data = null;
        byte[] voltage = getDeviceData(sourceDevice, newDevice, Attr.TYPE_CURRENT_A);
        byte[] current = getDeviceData(sourceDevice, newDevice, Attr.TYPE_CURRENT_B);
        byte[] active = getDeviceData(sourceDevice, newDevice, Attr.TYPE_CURRENT_C);

        if (voltage != null) {
            if (data == null) {
                data = DataTypeConverter.concatAll(voltage);
            } else {
                data = DataTypeConverter.concatAll(data, voltage);
            }
            iMsgNum++;
        }

        if (current != null) {
            if (data == null) {
                data = DataTypeConverter.concatAll(current);
            } else {
                data = DataTypeConverter.concatAll(data, current);
            }
            iMsgNum++;
        }

        if (active != null) {
            if (data == null) {
                data = DataTypeConverter.concatAll(active);
            } else {
                data = DataTypeConverter.concatAll(data, active);
            }
            iMsgNum++;
        }

        byte[] msgNum = DataTypeConverter.int2byte(iMsgNum); // 消息数

        byte[] input = DataTypeConverter.concatAll(requestCode, msgNum, data);

        return input;
    }

    private byte[] getDeviceData(Device sourceDevice, Device newDevice, int type) {
        Attr source = null;
        if (sourceDevice != null) {
            for (Attr attr : sourceDevice.getAttrs()) {
                if (attr.getType() == type) {
                    source = attr;
                    break;
                }
            }
        }

        Attr dest = null;
        if (newDevice != null) {
            for (Attr attr : newDevice.getAttrs()) {
                if (attr.getType() == type) {
                    dest = attr;
                    break;
                }
            }
        }

        byte[] data = null;
        Charset cs = Charset.forName("GBK");
        byte[] name = newDevice.getName().getBytes(cs);
        byte[] nameOffset = new byte[64 - name.length];
        byte[] attrType = DataTypeConverter.int2byte(type);

        if (source == null && dest != null) {
            // 新增
            byte[] optType = DataTypeConverter.int2byte(1);
            byte[] upValue = DataTypeConverter.float2byte(dest.getUpValue());
            byte[] downValue = DataTypeConverter.float2byte(dest.getDownValue());
            data = DataTypeConverter.concatAll(name, nameOffset, attrType, optType, upValue, downValue);
        } else if (source != null && dest != null) {
            // 修改
            byte[] optType = DataTypeConverter.int2byte(3);
            byte[] upValue = DataTypeConverter.float2byte(dest.getUpValue());
            byte[] downValue = DataTypeConverter.float2byte(dest.getDownValue());
            data = DataTypeConverter.concatAll(name, nameOffset, attrType, optType, upValue, downValue);
        } else if (source != null && dest == null) {
            // 删除
            byte[] optType = DataTypeConverter.int2byte(2);
            byte[] upValue = new byte[4];
            byte[] downValue = new byte[4];
            data = DataTypeConverter.concatAll(name, nameOffset, attrType, optType, upValue, downValue);
        } else if (source == null && dest == null) {
            // 无变更
            data = null;
        }

        return data;
    }
}
