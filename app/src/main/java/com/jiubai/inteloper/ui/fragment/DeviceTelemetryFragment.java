package com.jiubai.inteloper.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.badoo.mobile.util.WeakHandler;
import com.jiubai.inteloper.R;
import com.jiubai.inteloper.bean.Device;
import com.jiubai.inteloper.bean.DeviceTelemetry;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.presenter.DevicePresenterImpl;
import com.jiubai.inteloper.ui.activity.MonitorActivity;
import com.jiubai.inteloper.ui.activity.TelemetryActivity;
import com.jiubai.inteloper.ui.iview.IDeviceView;
import com.jiubai.inteloper.widget.RippleView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by larry on 18/08/2017.
 */

public class DeviceTelemetryFragment extends Fragment implements RippleView.OnRippleCompleteListener,
        IDeviceView {
    @Bind(R.id.textView_name)
    TextView mNameTextView;

    @Bind(R.id.textView_voltageA)
    TextView mVoltageATextView;

    @Bind(R.id.textView_voltageB)
    TextView mVoltageBTextView;

    @Bind(R.id.textView_voltageC)
    TextView mVoltageCTextView;

    @Bind(R.id.textView_currentA)
    TextView mCurrentATextView;

    @Bind(R.id.textView_currentB)
    TextView mCurrentBTextView;

    @Bind(R.id.textView_currentC)
    TextView mCurrentCTextView;

    @Bind(R.id.textView_temp)
    TextView mTempTextView;

    @Bind(R.id.ripple_voltage)
    RippleView mVoltageRipple;

    @Bind(R.id.ripple_current)
    RippleView mCurrentRipple;

    @Bind(R.id.ripple_temp)
    RippleView mTempRipple;

    @Bind(R.id.textView_time_voltage)
    TextView mTimeVoltageTextView;

    @Bind(R.id.textView_time_current)
    TextView mTimeCurrentTextView;

    @Bind(R.id.textView_time_temp)
    TextView mTimeTempTextView;

    @Bind(R.id.textView_desc)
    TextView mDescTextView;

    private Device device;

    private DeviceTelemetry deviceTelemetry;

    private WeakHandler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_telemetry, container, false);

        ButterKnife.bind(this, view);

        initView(view);

        return view;
    }

    private void initView(View view) {
        mVoltageRipple.setOnRippleCompleteListener(this);
        mCurrentRipple.setOnRippleCompleteListener(this);
        mTempRipple.setOnRippleCompleteListener(this);

        device = (Device) getArguments().getSerializable("device");

        mNameTextView.setText(device.getName());
        mDescTextView.setText(device.getDesc());

        if (TextUtils.isEmpty(device.getName()) || TextUtils.isEmpty(device.getDesc())) {
            return;
        }

        UtilBox.showLoading(getActivity());

        handler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message.what == 1) {
                    new DevicePresenterImpl(getActivity(), DeviceTelemetryFragment.this).getDeviceTelemetry(device.getName());

                    handler.sendEmptyMessageDelayed(1, 5000);
                } else if (message.what == 0) {

                }

                return false;
            }
        });

        handler.sendEmptyMessage(1);

//        new DevicePresenterImpl(getActivity(), this).getDeviceTelemetry(deviceName);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        Intent intent = new Intent(getActivity(), TelemetryActivity.class);
        intent.putExtra("deviceName", device.getName());

        String type = "";
        switch (rippleView.getId()) {
            case R.id.ripple_voltage:
                type = "电压值";
                break;

            case R.id.ripple_current:
                type = "电流值";
                break;

            case R.id.ripple_temp:
                type = "温度值";
                break;
        }
        intent.putExtra("type", type);

        UtilBox.startActivity(getActivity(), intent, false);
    }

    @Override
    public void onGetDeviceTelemetryResult(boolean result, String info, Object extras) {
        MonitorActivity activity = ((MonitorActivity)getActivity());

        if (activity == null) {
            return;
        }

        activity.currentInitNum++;

        if (activity.currentInitNum == activity.initNum) {
            UtilBox.dismissLoading();
        }

        if (result) {
            deviceTelemetry = (DeviceTelemetry) extras;

            mVoltageATextView.setText(deviceTelemetry.getVoltageA() + "");
            mVoltageBTextView.setText(deviceTelemetry.getVoltageB() + "");
            mVoltageCTextView.setText(deviceTelemetry.getVoltageC() + "");
            mCurrentATextView.setText(deviceTelemetry.getCurrentA() + "");
            mCurrentBTextView.setText(deviceTelemetry.getCurrentB() + "");
            mCurrentCTextView.setText(deviceTelemetry.getCurrentC() + "");
            mTempTextView.setText(deviceTelemetry.getTemp() + "");

            if (deviceTelemetry.getVoltageAQuality() != 31) {
                mVoltageATextView.setTextColor(Color.parseColor("#FF9900"));
            } else {
                mVoltageATextView.setTextColor(Color.parseColor("#909090"));
            }

            if (deviceTelemetry.getVoltageBQuality() != 31) {
                mVoltageBTextView.setTextColor(Color.parseColor("#FF9900"));
            } else {
                mVoltageBTextView.setTextColor(Color.parseColor("#909090"));
            }

            if (deviceTelemetry.getVoltageCQuality() != 31) {
                mVoltageCTextView.setTextColor(Color.parseColor("#FF9900"));
            } else {
                mVoltageCTextView.setTextColor(Color.parseColor("#909090"));
            }

            if (deviceTelemetry.getCurrentAQuality() != 31) {
                mCurrentATextView.setTextColor(Color.parseColor("#FF9900"));
            } else {
                mCurrentATextView.setTextColor(Color.parseColor("#909090"));
            }

            if (deviceTelemetry.getCurrentBQuality() != 31) {
                mCurrentBTextView.setTextColor(Color.parseColor("#FF9900"));
            } else {
                mCurrentBTextView.setTextColor(Color.parseColor("#909090"));
            }

            if (deviceTelemetry.getCurrentCQuality() != 31) {
                mCurrentCTextView.setTextColor(Color.parseColor("#FF9900"));
            } else {
                mCurrentCTextView.setTextColor(Color.parseColor("#909090"));
            }

            if (deviceTelemetry.getTempQuality() != 31) {
                mTempTextView.setTextColor(Color.parseColor("#FF9900"));
            } else {
                mTempTextView.setTextColor(Color.parseColor("#909090"));
            }

            String time = UtilBox.getDateToString(Calendar.getInstance().getTimeInMillis(), UtilBox.YEAR_DATE_TIME);

            mTimeVoltageTextView.setText(time);
            mTimeCurrentTextView.setText(time);
            mTimeTempTextView.setText(time);
        } else {
            Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onGetDeviceTelecommendResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onDestroy() {
        if (handler != null) {
            handler.sendEmptyMessage(0);
            handler.removeMessages(1);

            handler = null;
        }

        super.onDestroy();
    }

    @Override
    public void onGetDeviceInfoResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onEditDeviceInfoResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onAddNewDeviceResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onGetDeviceListResult(boolean result, String info, Object extras) {

    }
}
