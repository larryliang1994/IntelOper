package com.jiubai.inteloper.presenter;

import android.app.Activity;
import android.util.Log;

import com.jiubai.inteloper.bean.Alarm;
import com.jiubai.inteloper.common.DataTypeConverter;
import com.jiubai.inteloper.config.Constants;
import com.jiubai.inteloper.net.RequestUtil;
import com.jiubai.inteloper.ui.iview.IAlarmView;

import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by larry on 18/08/2017.
 */

public class AlarmPresenterImpl implements IAlarmPresenter {
    private IAlarmView mIAlarmView;
    private Activity mActivity;

    public AlarmPresenterImpl(Activity activity, IAlarmView iAlarmView) {
        this.mActivity = activity;
        this.mIAlarmView = iAlarmView;
    }

    @Override
    public void getAlarmHistory(String startTime, String endTime, String keyword) {
        String formatStartTime = startTime.replace("-", "").replace(":", "").replace(" ", "") + "00\0";
        String formatEndTime = endTime.replace("-", "").replace(":", "").replace(" ", "") + "00\0";

        Log.i(Constants.TAG, formatStartTime);
        Log.i(Constants.TAG, formatEndTime);

        byte[] requestCode = DataTypeConverter.int2byte(6); // 操作码
        byte[] msgNum = DataTypeConverter.int2byte(1); // 消息数

        final Charset cs = Charset.forName("GBK");
        byte[] b1 = formatStartTime.getBytes(cs);
        byte[] b2 = formatEndTime.getBytes(cs);
        byte[] b3 = keyword.getBytes(cs);

        // 把所有字节合并成一条
        byte[] input = DataTypeConverter.concatAll(requestCode, msgNum, b1, b2, b3);

        final int requestMsgLength = 4 + 20 + 500;

        RequestUtil.request(input, 7, requestMsgLength, true,
                new RequestUtil.RequestCallback() {
                    @Override
                    public void success(int msgNum, byte[] msgContent) {
                        if (msgNum == -999) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIAlarmView.onGetAlarmHistoryResult(false, "查询时间跨度太大", null);
                                }
                            });
                        } else {

                            final ArrayList<Alarm> alarms = new ArrayList<>();

                            byte[] status;
                            byte[] occurTime;
                            byte[] warnStr;

                            for (int i = 0; i < msgNum; i++) {
                                status = DataTypeConverter.readBytes(msgContent, i * requestMsgLength, 4);
                                occurTime = DataTypeConverter.readBytes(msgContent, i * requestMsgLength + 4, 20);
                                warnStr = DataTypeConverter.readBytes(msgContent, i * requestMsgLength + 4 + 20, 500);

                                alarms.add(new Alarm(
                                        DataTypeConverter.byte2int(status),
                                        new String(occurTime, cs),
                                        new String(warnStr, cs)
                                ));

                                Log.i(Constants.TAG, DataTypeConverter.byte2int(status) + "");
                                Log.i(Constants.TAG, new String(occurTime, cs));
                                Log.i(Constants.TAG, new String(warnStr, cs));
                            }

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mIAlarmView.onGetAlarmHistoryResult(true, "", alarms);
                                }
                            });
                        }
                    }

                    @Override
                    public void error(final String info, Exception exception) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mIAlarmView.onGetAlarmHistoryResult(false, "获取历史告警失败", info);
                            }
                        });
                    }
                });

//        final ArrayList<Alarm> alarms = new ArrayList<>();
//
//        for (int i = 0; i < 1024; i++) {
//            alarms.add(new Alarm(
//                    i % 5 + 1,
//                    "2017-08-15  12:55:36",
//                    "这是具体描述，可能会很长。这是具体描述，可能会很长。这是具体描述，可能会很长。这是具体描述，可能会很长。这是具体描述，可能会很长。这是具体描述，可能会很长。这是具体描述，可能会很长。"
//            ));
//        }
//
//        mIAlarmView.onGetAlarmHistoryResult(true, "", alarms);
    }
}
