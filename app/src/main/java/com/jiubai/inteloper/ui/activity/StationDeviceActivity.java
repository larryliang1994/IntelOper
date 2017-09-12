package com.jiubai.inteloper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Space;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.bean.StationDevice;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.presenter.StationPresenterImpl;
import com.jiubai.inteloper.ui.iview.IStationView;
import com.jiubai.inteloper.widget.RippleView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StationDeviceActivity extends BaseActivity implements IStationView {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.ripple_delete)
    RippleView mDeleteRipple;

    @Bind(R.id.ripple_save)
    RippleView mSaveRipple;

    @Bind(R.id.space)
    Space mSpace;

    @Bind(R.id.editText_name)
    EditText mNameEditText;

    @Bind(R.id.editText_rtu)
    EditText mRTUEditText;

    private String stationName = "";
    private StationDevice stationDevice = null;

    private int optType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_station_device);

        ButterKnife.bind(this);

        stationDevice = (StationDevice) getIntent().getSerializableExtra("stationDevice");
        stationName = getIntent().getStringExtra("stationName");

        initView();
    }

    private void initView() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StationDeviceActivity.this.onBackPressed();
            }
        });

        if (stationDevice == null) {
            mDeleteRipple.setVisibility(View.GONE);
            mSpace.setVisibility(View.GONE);
        } else {
            mNameEditText.setText(stationDevice.getName());
            mRTUEditText.setText(stationDevice.getRtu());
        }

        mSaveRipple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (TextUtils.isEmpty(mNameEditText.getText().toString())) {
                    Toast.makeText(StationDeviceActivity.this, "请填写设备名称", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(mRTUEditText.getText().toString())) {
                    Toast.makeText(StationDeviceActivity.this, "请填写设备RTU", Toast.LENGTH_SHORT).show();
                    return;
                }

                StationDevice device = new StationDevice(
                        mNameEditText.getText().toString(), mRTUEditText.getText().toString());

                if (stationDevice == null) {
                    optType = StationPresenterImpl.STATION_DEVICE_OPT_TYPE_ADD;
                } else {
                    optType = StationPresenterImpl.STATION_DEVICE_OPT_TYPE_EDIT;
                }

                UtilBox.showLoading(StationDeviceActivity.this);

                new StationPresenterImpl(StationDeviceActivity.this, StationDeviceActivity.this)
                        .editDeviceInfo(device, stationName, optType);
            }
        });

        mDeleteRipple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                optType = StationPresenterImpl.STATION_DEVICE_OPT_TYPE_DELETE;

                UtilBox.showLoading(StationDeviceActivity.this);

                new StationPresenterImpl(StationDeviceActivity.this, StationDeviceActivity.this)
                        .editDeviceInfo(stationDevice, stationName, optType);
            }
        });
    }

    @Override
    public void onEditStationDeviceResult(boolean result, String info, Object extras) {
        UtilBox.dismissLoading();

        if (result) {
            if (optType == StationPresenterImpl.STATION_DEVICE_OPT_TYPE_ADD) {
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            } else if (optType == StationPresenterImpl.STATION_DEVICE_OPT_TYPE_EDIT) {
                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
            }

            StationDevice device = new StationDevice(
                    mNameEditText.getText().toString(), mRTUEditText.getText().toString());

            Intent intent = new Intent();
            intent.putExtra("stationDevice", device);
            intent.putExtra("optType", optType);
            setResult(RESULT_OK, intent);
            UtilBox.returnActivity(this);
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        UtilBox.returnActivity(this);
    }

    @Override
    public void onGetStationListResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onGetStationInfoResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onGetRegionListResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onGetGroupListResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onGetStationDeviceListResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onEditStationInfoResult(boolean result, String info, Object extras) {

    }
}
