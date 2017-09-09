package com.jiubai.inteloper.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.bean.Attr;
import com.jiubai.inteloper.bean.Device;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.presenter.DevicePresenterImpl;
import com.jiubai.inteloper.ui.iview.IDeviceView;
import com.jiubai.inteloper.widget.RippleView;
import com.jiubai.inteloper.widget.SmoothCheckBox;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DefinitionActivity extends BaseActivity implements IDeviceView {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.editText_name)
    EditText mNameEditText;

    @Bind(R.id.checkBox_currentA)
    SmoothCheckBox mCurrentACheckBox;

    @Bind(R.id.checkBox_currentB)
    SmoothCheckBox mCurrentBCheckBox;

    @Bind(R.id.checkBox_currentC)
    SmoothCheckBox mCurrentCCheckBox;

    @Bind(R.id.layout_currentA)
    LinearLayout mCurrentALayout;

    @Bind(R.id.layout_currentB)
    LinearLayout mCurrentBLayout;

    @Bind(R.id.layout_currentC)
    LinearLayout mCurrentCLayout;

    @Bind(R.id.layout_currentA_edit)
    LinearLayout mCurrentAEditLayout;

    @Bind(R.id.layout_currentB_edit)
    LinearLayout mCurrentBEditLayout;

    @Bind(R.id.layout_currentC_edit)
    LinearLayout mCurrentCEditLayout;

    @Bind(R.id.layout_currentA_divider)
    LinearLayout mCurrentADividerLayout;

    @Bind(R.id.layout_currentB_divider)
    LinearLayout mCurrentBDividerLayout;

    @Bind(R.id.layout_currentC_divider)
    LinearLayout mCurrentCDividerLayout;

    @Bind(R.id.editText_currentA_up)
    EditText mCurrentAUpEditText;

    @Bind(R.id.editText_currentA_down)
    EditText mCurrentADownEditText;

    @Bind(R.id.editText_currentB_up)
    EditText mCurrentBUpEditText;

    @Bind(R.id.editText_currentB_down)
    EditText mCurrentBDownEditText;

    @Bind(R.id.editText_currentC_up)
    EditText mCurrentCUpEditText;

    @Bind(R.id.editText_currentC_down)
    EditText mCurrentCDownEditText;

    @Bind(R.id.ripple_save)
    RippleView mSaveRipple;

    private Device device;
    private Device sourceDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_definition);

        ButterKnife.bind(this);

        device = (Device) getIntent().getSerializableExtra("device");

        initView();
    }

    private void initView() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefinitionActivity.this.onBackPressed();
            }
        });

        setupCheckBox();

        if (!TextUtils.isEmpty(device.getName())) {
            UtilBox.showLoading(this);
            new DevicePresenterImpl(this, this).getDeviceInfo(device.getName());
        }

        mSaveRipple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (TextUtils.isEmpty(device.getName())) {
                    // 新增
                    if (TextUtils.isEmpty(mNameEditText.getText().toString())) {
                        Toast.makeText(DefinitionActivity.this, "请填写设备名称", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Device newDevice = getDevice();

                    if (newDevice == null) {
                        return;
                    }

                    UtilBox.showLoading(DefinitionActivity.this);

                    new DevicePresenterImpl(DefinitionActivity.this, DefinitionActivity.this)
                            .addNewDevice(newDevice);
                } else {
                    // 修改
                    Device newDevice = getDevice();

                    if (newDevice == null) {
                        return;
                    }

                    UtilBox.showLoading(DefinitionActivity.this);

                    new DevicePresenterImpl(DefinitionActivity.this, DefinitionActivity.this)
                            .editDeviceInfo(sourceDevice, newDevice);
                }
            }
        });
    }

    @Override
    public void onEditDeviceInfoResult(boolean result, String info, Object extras) {
        UtilBox.dismissLoading();

        if (result) {
            Toast.makeText(this, "保存设备属性成功", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    DefinitionActivity.this.setResult(RESULT_OK);
                    UtilBox.returnActivity(DefinitionActivity.this);
                }
            }, 1000);
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAddNewDeviceResult(boolean result, String info, Object extras) {
        UtilBox.dismissLoading();

        if (result) {
            Toast.makeText(this, "添加新设备成功", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    DefinitionActivity.this.setResult(RESULT_OK);
                    UtilBox.returnActivity(DefinitionActivity.this);
                }
            }, 1000);
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetDeviceTelemetryResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onGetDeviceTelecommendResult(boolean result, String info, Object extras) {

    }

    private Device getDevice() {
        final Device device = new Device();

        ArrayList<Attr> attrs = new ArrayList<>();

        if (mCurrentACheckBox.isChecked()) {
            if (TextUtils.isEmpty(mCurrentAUpEditText.getText().toString())) {
                Toast.makeText(DefinitionActivity.this, "请填写A相电流上限", Toast.LENGTH_SHORT).show();
                return null;
            }

            if (TextUtils.isEmpty(mCurrentADownEditText.getText().toString())) {
                Toast.makeText(DefinitionActivity.this, "请填写A相电流下限", Toast.LENGTH_SHORT).show();
                return null;
            }

            attrs.add(new Attr(
                    Attr.TYPE_CURRENT_A, true,
                    Float.valueOf(mCurrentAUpEditText.getText().toString()),
                    Float.valueOf(mCurrentADownEditText.getText().toString())));
        }

        if (mCurrentBCheckBox.isChecked()) {
            if (TextUtils.isEmpty(mCurrentBUpEditText.getText().toString())) {
                Toast.makeText(DefinitionActivity.this, "请填写B相电流上限", Toast.LENGTH_SHORT).show();
                return null;
            }

            if (TextUtils.isEmpty(mCurrentBDownEditText.getText().toString())) {
                Toast.makeText(DefinitionActivity.this, "请填写B相电流下限", Toast.LENGTH_SHORT).show();
                return null;
            }

            attrs.add(new Attr(
                    Attr.TYPE_CURRENT_B, true,
                    Float.valueOf(mCurrentBUpEditText.getText().toString()),
                    Float.valueOf(mCurrentBDownEditText.getText().toString())));
        }

        if (mCurrentCCheckBox.isChecked()) {
            if (TextUtils.isEmpty(mCurrentCUpEditText.getText().toString())) {
                Toast.makeText(DefinitionActivity.this, "请填写C相电流上限", Toast.LENGTH_SHORT).show();
                return null;
            }

            if (TextUtils.isEmpty(mCurrentCDownEditText.getText().toString())) {
                Toast.makeText(DefinitionActivity.this, "请填写C相电流下限", Toast.LENGTH_SHORT).show();
                return null;
            }

            attrs.add(new Attr(
                    Attr.TYPE_CURRENT_C, true,
                    Float.valueOf(mCurrentCUpEditText.getText().toString()),
                    Float.valueOf(mCurrentCDownEditText.getText().toString())));
        }

        device.setAttrs(attrs);

        device.setName(mNameEditText.getText().toString());

        return device;
    }

    @Override
    public void onGetDeviceInfoResult(boolean result, String info, Object extras) {
        UtilBox.dismissLoading();

        if (result) {
            sourceDevice = (Device) extras;

            mNameEditText.setText(device.getName());

            mNameEditText.setEnabled(false);

            ArrayList<Attr> attrs = sourceDevice.getAttrs();

            for (Attr attr: attrs) {
                switch (attr.getType()) {
                    case Attr.TYPE_CURRENT_A:
                        if (attr.isExist()) {
                            mCurrentACheckBox.setChecked(true, true);
                            mCurrentAUpEditText.setText(String.valueOf(attr.getUpValue()));
                            mCurrentADownEditText.setText(String.valueOf(attr.getDownValue()));
                        }
                        break;

                    case Attr.TYPE_CURRENT_B:
                        if (attr.isExist()) {
                            mCurrentBCheckBox.setChecked(true, true);
                            mCurrentBUpEditText.setText(String.valueOf(attr.getUpValue()));
                            mCurrentBDownEditText.setText(String.valueOf(attr.getDownValue()));
                        }
                        break;

                    case Attr.TYPE_CURRENT_C:
                        if (attr.isExist()) {
                            mCurrentCCheckBox.setChecked(true, true);
                            mCurrentCUpEditText.setText(String.valueOf(attr.getUpValue()));
                            mCurrentCDownEditText.setText(String.valueOf(attr.getDownValue()));
                        }
                        break;
                }
            }
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupCheckBox() {
        mCurrentALayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentACheckBox.setChecked(!mCurrentACheckBox.isChecked(), true);
            }
        });

        mCurrentBLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentBCheckBox.setChecked(!mCurrentBCheckBox.isChecked(), true);
            }
        });

        mCurrentCLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentCCheckBox.setChecked(!mCurrentCCheckBox.isChecked(), true);
            }
        });

        mCurrentACheckBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    mCurrentADividerLayout.setVisibility(View.VISIBLE);
                    mCurrentAEditLayout.setVisibility(View.VISIBLE);
                } else {
                    mCurrentADividerLayout.setVisibility(View.GONE);
                    mCurrentAEditLayout.setVisibility(View.GONE);
                }
            }
        });

        mCurrentBCheckBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    mCurrentBDividerLayout.setVisibility(View.VISIBLE);
                    mCurrentBEditLayout.setVisibility(View.VISIBLE);
                } else {
                    mCurrentBDividerLayout.setVisibility(View.GONE);
                    mCurrentBEditLayout.setVisibility(View.GONE);
                }
            }
        });

        mCurrentCCheckBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    mCurrentCDividerLayout.setVisibility(View.VISIBLE);
                    mCurrentCEditLayout.setVisibility(View.VISIBLE);
                } else {
                    mCurrentCDividerLayout.setVisibility(View.GONE);
                    mCurrentCEditLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        DefinitionActivity.this.setResult(RESULT_CANCELED);
        UtilBox.returnActivity(this);
    }

    @Override
    public void onGetDeviceListResult(boolean result, String info, Object extras) {

    }

}
