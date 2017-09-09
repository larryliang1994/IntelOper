package com.jiubai.inteloper.presenter;

import android.app.Activity;

import com.jiubai.inteloper.common.DataTypeConverter;
import com.jiubai.inteloper.net.RequestUtil;
import com.jiubai.inteloper.ui.iview.ITelemetryView;

import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by larry on 18/08/2017.
 */

public class TelemetryPresenterImpl implements ITelemetryPresenter {
    private ITelemetryView mITelemetryView;
    private Activity mActivity;

    public TelemetryPresenterImpl(Activity activity, ITelemetryView iTelemetryView) {
        this.mActivity = activity;
        this.mITelemetryView = iTelemetryView;
    }

    @Override
    public void getTelemetryData(String deviceName, String type, String queryDate) {
        byte[] requestCode = DataTypeConverter.int2byte(4); // 操作码
        byte[] msgNum = DataTypeConverter.int2byte(1); // 消息数

        final Charset cs = Charset.forName("GBK");

        byte[] id = (deviceName + "\0").getBytes(cs);
        byte[] id_offset = new byte[64-id.length];

        byte[] which = null;
        switch (type) {
            case "电压值": which = DataTypeConverter.int2byte(1);  break;
            case "电流值": which = DataTypeConverter.int2byte(2);  break;
            case "温度值": which = DataTypeConverter.int2byte(3);  break;
        }

        byte[] date = (queryDate + "\0").getBytes(cs);

        // 把所有字节合并成一条
        byte[] input = DataTypeConverter.concatAll(requestCode, msgNum, id, id_offset, which, date);

        final int requestMsgLength = 4;

        RequestUtil.request(input, 5, requestMsgLength, false,
                new RequestUtil.RequestCallback() {
                    @Override
                    public void success(int msgNum, byte[] msgContent) {
                        final ArrayList<Float> values = new ArrayList<>();

                        byte[] value;

                        for (int i = 0; i < msgNum; i++) {
                            value = DataTypeConverter.readBytes(msgContent, i * requestMsgLength, 4);

                            values.add(DataTypeConverter.byte2float(value));
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mITelemetryView.onGetTelemetryDataResult(true, "", values);
                            }
                        });
                    }

                    @Override
                    public void error(final String info, Exception exception) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mITelemetryView.onGetTelemetryDataResult(false, "获取历史遥测失败", info);
                            }
                        });
                    }
                });

//        final ArrayList<Float> values = new ArrayList<>();
//        for (int i = 0; i < 1440 * 3; i++) {
//            float value = (float)Math.random() * 20f;
//            if (Math.random() > 0.5) {
//                values.add(value);
//            } else {
//                values.add(value * -1f);
//            }
//        }
//
//        mITelemetryView.onGetTelemetryDataResult(true, "", values);
    }
}
