package com.jiubai.inteloper.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.widget.RippleView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DeviceDefinitionActivity extends BaseActivity implements RippleView.OnRippleCompleteListener {

    @Bind(R.id.ripple_voltage)
    RippleView mVoltageRipple;

    @Bind(R.id.ripple_current)
    RippleView mCurrentRipple;

    @Bind(R.id.ripple_active_power)
    RippleView mActivePowerRipple;

    @Bind(R.id.ripple_reactive_power)
    RippleView mReactivePowerRipple;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_device_definition);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mVoltageRipple.setOnRippleCompleteListener(this);
        mCurrentRipple.setOnRippleCompleteListener(this);
        mActivePowerRipple.setOnRippleCompleteListener(this);
        mReactivePowerRipple.setOnRippleCompleteListener(this);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceDefinitionActivity.this.finish();
            }
        });
    }

    @Override
    public void onComplete(RippleView rippleView) {
        Intent intent;

        intent = new Intent(this, DeviceDefinitionModifyActivity.class);

        switch (rippleView.getId()) {
            case R.id.ripple_voltage:
                intent.putExtra("name", "电压");
                intent.putExtra("column", 2);
                break;

            case R.id.ripple_current:
                intent.putExtra("name", "电流");
                intent.putExtra("column", 2);
                break;

            case R.id.ripple_active_power:
                intent.putExtra("name", "有功");
                intent.putExtra("column", 1);
                break;

            case R.id.ripple_reactive_power:
                intent.putExtra("name", "无功");
                intent.putExtra("column", 1);
                break;
        }

        startActivity(intent);
    }
}
