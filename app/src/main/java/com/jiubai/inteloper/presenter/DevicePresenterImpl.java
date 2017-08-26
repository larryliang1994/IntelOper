package com.jiubai.inteloper.presenter;

import android.app.Activity;
import android.os.Handler;

import com.jiubai.inteloper.bean.Attr;
import com.jiubai.inteloper.bean.Device;
import com.jiubai.inteloper.common.DataTypeConverter;
import com.jiubai.inteloper.net.RequestUtil;
import com.jiubai.inteloper.ui.iview.IDeviceView;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

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
//        byte[] requestCode = DataTypeConverter.int2byte(8); // 操作码
//        byte[] msgNum = DataTypeConverter.int2byte(1); // 消息数
//        // 把所有字节合并成一条
//        byte[] input = DataTypeConverter.concatAll(requestCode, msgNum);
//        final int requestMsgLength = 19 + 64;
//
//        RequestUtil.request(input, 9, requestMsgLength, true,
//                new RequestUtil.RequestCallback() {
//                    @Override
//                    public void success(int msgNum, byte[] msgContent) {
//                        final List<Device> deviceList = new ArrayList<>();
//
//                        byte[] id;
//                        byte[] name;
//
//                        for (int i = 0; i < msgNum; i++) {
//
//                            id = DataTypeConverter.readBytes(msgContent, i * requestMsgLength, 19);
//                            name = DataTypeConverter.readBytes(msgContent, i * requestMsgLength + 19, 64);
//
//                            try {
//                                deviceList.add(new Device(new String(id, "GBK"), new String(name, "GBK")));
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//
//                                mActivity.runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        mIDeviceView.onGetDeviceListResult(false, "编码格式转换错误", "");
//                                    }
//                                });
//                            }
//                        }
//
//                        // 配置拼音属性
//                        HanyuPinyinOutputFormat pyFormat = new HanyuPinyinOutputFormat();
//                        pyFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
//                        pyFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//                        pyFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
//
//                        for (int i = 0; i < deviceList.size(); i++) {
//                            Device device = deviceList.get(i);
//                            deviceList.get(i).setFirstWord(PinyinHelper.toHanyuPinyinString(device.getName().substring(3, 4),
//                                    pyFormat, "").substring(0, 1));
//                        }
//
//                        mActivity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mIDeviceView.onGetDeviceListResult(true, "", deviceList);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void error(final String info, final Exception exception) {
//                        mActivity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mIDeviceView.onGetDeviceListResult(false, "获取设备列表失败", exception);
//                            }
//                        });
//                    }
//                });

        final List<Device> list = new ArrayList<>();

        list.add(new Device("12", "加内特"));
        list.add(new Device("12", "韦德"));
        list.add(new Device("12", "詹姆斯"));
        list.add(new Device("12", "安东尼"));
        list.add(new Device("12", "科比"));
        list.add(new Device("12", "乔丹"));
        list.add(new Device("12", "奥尼尔"));
        list.add(new Device("12", "麦格雷迪"));
        list.add(new Device("12", "艾弗森"));
        list.add(new Device("12", "哈达威"));
        list.add(new Device("12", "纳什"));
        list.add(new Device("12", "弗朗西斯"));
        list.add(new Device("12", "姚明"));
        list.add(new Device("12", "库里"));
        list.add(new Device("12", "邓肯"));
        list.add(new Device("12", "吉诺比利"));
        list.add(new Device("12", "帕克"));
        list.add(new Device("12", "杜兰特"));
        list.add(new Device("12", "韦伯"));
        list.add(new Device("12", "威斯布鲁克"));
        list.add(new Device("12", "霍华德"));
        list.add(new Device("12", "保罗"));
        list.add(new Device("12", "罗斯"));
        list.add(new Device("12", "加索尔"));
        list.add(new Device("12", "隆多"));
        list.add(new Device("12", "诺维斯基"));
        list.add(new Device("12", "格里芬"));
        list.add(new Device("12", "波什"));
        list.add(new Device("12", "伊戈达拉"));

        // 配置拼音属性
        HanyuPinyinOutputFormat pyFormat = new HanyuPinyinOutputFormat();
        pyFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        pyFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        pyFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

        for (int i = 0; i < list.size(); i++) {
            Device device = list.get(i);
            list.get(i).setFirstWord(PinyinHelper.toHanyuPinyinString(device.getName(),
                    pyFormat, "").substring(0, 1));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mIDeviceView.onGetDeviceListResult(true, "", list);
            }
        }, 500);
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
                                mIDeviceView.onGetDeviceInfoResult(false, "获取设备属性失败", exception);
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

    private byte[] getEditDeviceInput(final Device sourceDevice, Device newDevice) {
        byte[] requestCode = DataTypeConverter.int2byte(10); // 操作码

        int iMsgNum = 0;

        byte[] data = null;
        byte[] voltage = getDeviceData(sourceDevice, newDevice, Attr.TYPE_VOLTAGE);
        byte[] current = getDeviceData(sourceDevice, newDevice, Attr.TYPE_CURRENT);
        byte[] active = getDeviceData(sourceDevice, newDevice, Attr.TYPE_ACTIVE);
        byte[] idle = getDeviceData(sourceDevice, newDevice, Attr.TYPE_IDLE);

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

        if (idle != null) {
            if (data == null) {
                data = DataTypeConverter.concatAll(idle);
            } else {
                data = DataTypeConverter.concatAll(data, idle);
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
