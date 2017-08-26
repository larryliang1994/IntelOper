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

    @Bind(R.id.checkBox_voltage)
    SmoothCheckBox mVoltageCheckBox;

    @Bind(R.id.checkBox_current)
    SmoothCheckBox mCurrentCheckBox;

    @Bind(R.id.checkBox_active)
    SmoothCheckBox mActiveCheckBox;

    @Bind(R.id.checkBox_idle)
    SmoothCheckBox mIdleCheckBox;

    @Bind(R.id.layout_voltage)
    LinearLayout mVoltageLayout;

    @Bind(R.id.layout_current)
    LinearLayout mCurrentLayout;

    @Bind(R.id.layout_active)
    LinearLayout mActiveLayout;

    @Bind(R.id.layout_idle)
    LinearLayout mIdleLayout;

    @Bind(R.id.layout_voltage_edit)
    LinearLayout mVoltageEditLayout;

    @Bind(R.id.layout_current_edit)
    LinearLayout mCurrentEditLayout;

    @Bind(R.id.layout_active_edit)
    LinearLayout mActiveEditLayout;

    @Bind(R.id.layout_idle_edit)
    LinearLayout mIdleEditLayout;

    @Bind(R.id.layout_voltage_divider)
    LinearLayout mVoltageDividerLayout;

    @Bind(R.id.layout_current_divider)
    LinearLayout mCurrentDividerLayout;

    @Bind(R.id.layout_active_divider)
    LinearLayout mActiveDividerLayout;

    @Bind(R.id.layout_idle_divider)
    LinearLayout mIdleDividerLayout;

    @Bind(R.id.editText_voltage_up)
    EditText mVoltageUpEditText;

    @Bind(R.id.editText_voltage_down)
    EditText mVoltageDownEditText;

    @Bind(R.id.editText_current_up)
    EditText mCurrentUpEditText;

    @Bind(R.id.editText_current_down)
    EditText mCurrentDownEditText;

    @Bind(R.id.editText_active_up)
    EditText mActiveUpEditText;

    @Bind(R.id.editText_active_down)
    EditText mActiveDownEditText;

    @Bind(R.id.editText_idle_up)
    EditText mIdleUpEditText;

    @Bind(R.id.editText_idle_down)
    EditText mIdleDownEditText;

    @Bind(R.id.ripple_save)
    RippleView mSaveRipple;

    private String deviceName;
    private Device sourceDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_definition);

        ButterKnife.bind(this);

        deviceName = getIntent().getStringExtra("deviceName");

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

        if (!TextUtils.isEmpty(deviceName)) {
            UtilBox.showLoading(this);
            new DevicePresenterImpl(this, this).getDeviceInfo(deviceName);
        }

        mSaveRipple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (TextUtils.isEmpty(deviceName)) {
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

    private Device getDevice() {
        final Device device = new Device();

        ArrayList<Attr> attrs = new ArrayList<>();

        if (mVoltageCheckBox.isChecked()) {
            if (TextUtils.isEmpty(mVoltageUpEditText.getText().toString())) {
                Toast.makeText(DefinitionActivity.this, "请填写电压上限", Toast.LENGTH_SHORT).show();
                return null;
            }

            if (TextUtils.isEmpty(mVoltageDownEditText.getText().toString())) {
                Toast.makeText(DefinitionActivity.this, "请填写电压下限", Toast.LENGTH_SHORT).show();
                return null;
            }

            attrs.add(new Attr(
                    Attr.TYPE_VOLTAGE, true,
                    Float.valueOf(mVoltageUpEditText.getText().toString()),
                    Float.valueOf(mVoltageDownEditText.getText().toString())));
        }

        if (mCurrentCheckBox.isChecked()) {
            if (TextUtils.isEmpty(mCurrentUpEditText.getText().toString())) {
                Toast.makeText(DefinitionActivity.this, "请填写电流上限", Toast.LENGTH_SHORT).show();
                return null;
            }

            if (TextUtils.isEmpty(mCurrentDownEditText.getText().toString())) {
                Toast.makeText(DefinitionActivity.this, "请填写电流下限", Toast.LENGTH_SHORT).show();
                return null;
            }

            attrs.add(new Attr(
                    Attr.TYPE_CURRENT, true,
                    Float.valueOf(mCurrentUpEditText.getText().toString()),
                    Float.valueOf(mCurrentDownEditText.getText().toString())));
        }

        if (mActiveCheckBox.isChecked()) {
            if (TextUtils.isEmpty(mActiveUpEditText.getText().toString())) {
                Toast.makeText(DefinitionActivity.this, "请填写有功压上限", Toast.LENGTH_SHORT).show();
                return null;
            }

            if (TextUtils.isEmpty(mActiveDownEditText.getText().toString())) {
                Toast.makeText(DefinitionActivity.this, "请填写有功下限", Toast.LENGTH_SHORT).show();
                return null;
            }

            attrs.add(new Attr(
                    Attr.TYPE_ACTIVE, true,
                    Float.valueOf(mActiveUpEditText.getText().toString()),
                    Float.valueOf(mActiveDownEditText.getText().toString())));
        }

        if (mIdleCheckBox.isChecked()) {
            if (TextUtils.isEmpty(mIdleUpEditText.getText().toString())) {
                Toast.makeText(DefinitionActivity.this, "请填写无功上限", Toast.LENGTH_SHORT).show();
                return null;
            }

            if (TextUtils.isEmpty(mIdleDownEditText.getText().toString())) {
                Toast.makeText(DefinitionActivity.this, "请填写无功下限", Toast.LENGTH_SHORT).show();
                return null;
            }

            attrs.add(new Attr(
                    Attr.TYPE_IDLE, true,
                    Float.valueOf(mIdleUpEditText.getText().toString()),
                    Float.valueOf(mIdleDownEditText.getText().toString())));
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

            mNameEditText.setText(deviceName);

            mNameEditText.setEnabled(false);

            ArrayList<Attr> attrs = sourceDevice.getAttrs();

            for (Attr attr: attrs) {
                switch (attr.getType()) {
                    case Attr.TYPE_VOLTAGE:
                        if (attr.isExist()) {
                            mVoltageCheckBox.setChecked(true, true);
                            mVoltageUpEditText.setText(String.valueOf(attr.getUpValue()));
                            mVoltageDownEditText.setText(String.valueOf(attr.getDownValue()));
                        }
                        break;

                    case Attr.TYPE_CURRENT:
                        if (attr.isExist()) {
                            mCurrentCheckBox.setChecked(true, true);
                            mCurrentUpEditText.setText(String.valueOf(attr.getUpValue()));
                            mCurrentDownEditText.setText(String.valueOf(attr.getDownValue()));
                        }
                        break;

                    case Attr.TYPE_ACTIVE:
                        if (attr.isExist()) {
                            mActiveCheckBox.setChecked(true, true);
                            mActiveUpEditText.setText(String.valueOf(attr.getUpValue()));
                            mActiveDownEditText.setText(String.valueOf(attr.getDownValue()));
                        }
                        break;

                    case Attr.TYPE_IDLE:
                        if (attr.isExist()) {
                            mIdleCheckBox.setChecked(true, true);
                            mIdleUpEditText.setText(String.valueOf(attr.getUpValue()));
                            mIdleDownEditText.setText(String.valueOf(attr.getDownValue()));
                        }
                        break;
                }
            }
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupCheckBox() {
        mVoltageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVoltageCheckBox.setChecked(!mVoltageCheckBox.isChecked(), true);
            }
        });

        mCurrentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentCheckBox.setChecked(!mCurrentCheckBox.isChecked(), true);
            }
        });

        mActiveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActiveCheckBox.setChecked(!mActiveCheckBox.isChecked(), true);
            }
        });

        mIdleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIdleCheckBox.setChecked(!mIdleCheckBox.isChecked(), true);
            }
        });

        mVoltageCheckBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    mVoltageDividerLayout.setVisibility(View.VISIBLE);
                    mVoltageEditLayout.setVisibility(View.VISIBLE);
                } else {
                    mVoltageDividerLayout.setVisibility(View.GONE);
                    mVoltageEditLayout.setVisibility(View.GONE);
                }
            }
        });

        mCurrentCheckBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    mCurrentDividerLayout.setVisibility(View.VISIBLE);
                    mCurrentEditLayout.setVisibility(View.VISIBLE);
                } else {
                    mCurrentDividerLayout.setVisibility(View.GONE);
                    mCurrentEditLayout.setVisibility(View.GONE);
                }
            }
        });

        mActiveCheckBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    mActiveDividerLayout.setVisibility(View.VISIBLE);
                    mActiveEditLayout.setVisibility(View.VISIBLE);
                } else {
                    mActiveDividerLayout.setVisibility(View.GONE);
                    mActiveEditLayout.setVisibility(View.GONE);
                }
            }
        });

        mIdleCheckBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    mIdleDividerLayout.setVisibility(View.VISIBLE);
                    mIdleEditLayout.setVisibility(View.VISIBLE);
                } else {
                    mIdleDividerLayout.setVisibility(View.GONE);
                    mIdleEditLayout.setVisibility(View.GONE);
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
