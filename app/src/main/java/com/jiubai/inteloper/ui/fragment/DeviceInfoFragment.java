package com.jiubai.inteloper.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.bean.Attr;
import com.jiubai.inteloper.bean.Device;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.presenter.DevicePresenterImpl;
import com.jiubai.inteloper.ui.activity.TelemetryActivity;
import com.jiubai.inteloper.ui.iview.IDeviceView;
import com.jiubai.inteloper.widget.RippleView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by larry on 18/08/2017.
 */

public class DeviceInfoFragment extends Fragment implements RippleView.OnRippleCompleteListener,
        IDeviceView {
    @Bind(R.id.textView_name)
    TextView mNameTextView;

    @Bind(R.id.textView_voltage)
    TextView mVoltageTextView;

    @Bind(R.id.textView_current)
    TextView mCurrentTextView;

    @Bind(R.id.textView_active)
    TextView mActiveTextView;

    @Bind(R.id.textView_idle)
    TextView mIdleTextView;

    @Bind(R.id.ripple_voltage)
    RippleView mVoltageRipple;

    @Bind(R.id.ripple_current)
    RippleView mCurrentRipple;

    @Bind(R.id.ripple_active)
    RippleView mActiveRipple;

    @Bind(R.id.ripple_idle)
    RippleView mIdleRipple;

    private String deviceName;

    private Device device;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_info, container, false);

        ButterKnife.bind(this, view);

        initView(view);

        return view;
    }

    private void initView(View view) {
        mVoltageRipple.setOnRippleCompleteListener(this);
        mCurrentRipple.setOnRippleCompleteListener(this);
        mActiveRipple.setOnRippleCompleteListener(this);
        mIdleRipple.setOnRippleCompleteListener(this);

        deviceName = getArguments().getString("deviceName");

        mNameTextView.setText(deviceName);

        UtilBox.showLoading(getActivity());

        new DevicePresenterImpl(getActivity(), this).getDeviceInfo(deviceName);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        Intent intent = new Intent(getActivity(), TelemetryActivity.class);
        intent.putExtra("deviceName", deviceName);

        String type = "";
        switch (rippleView.getId()) {
            case R.id.ripple_voltage:
                type = "电压";
                break;

            case R.id.ripple_current:
                type = "电流";
                break;

            case R.id.ripple_active:
                type = "有功";
                break;

            case R.id.ripple_idle:
                type = "无功";
                break;
        }
        intent.putExtra("type", type);

        UtilBox.startActivity(getActivity(), intent, false);
    }

    @Override
    public void onGetDeviceInfoResult(boolean result, String info, Object extras) {
        UtilBox.dismissLoading();

        if (result) {
            device = (Device) extras;
            device.setName(deviceName);

            for (Attr attr : device.getAttrs()) {
                switch (attr.getType()) {
                    case Attr.TYPE_VOLTAGE:
                        if (attr.isExist()) {
                            mVoltageTextView.setText(attr.getDownValue() + " - " + attr.getUpValue());
                        } else {
                            mVoltageTextView.setText("不存在");
                        }
                        break;

                    case Attr.TYPE_CURRENT:
                        if (attr.isExist()) {
                            mCurrentTextView.setText(attr.getDownValue() + " - " + attr.getUpValue());
                        } else {
                            mCurrentTextView.setText("不存在");
                        }
                        break;

                    case Attr.TYPE_ACTIVE:
                        if (attr.isExist()) {
                            mActiveTextView.setText(attr.getDownValue() + " - " + attr.getUpValue());
                        } else {
                            mActiveTextView.setText("不存在");
                        }
                        break;

                    case Attr.TYPE_IDLE:
                        if (attr.isExist()) {
                            mIdleTextView.setText(attr.getDownValue() + " - " + attr.getUpValue());
                        } else {
                            mIdleTextView.setText("不存在");
                        }
                        break;
                }
            }
        } else {
            Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
        }
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
