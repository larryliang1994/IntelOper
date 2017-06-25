package com.jiubai.inteloper.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jiubai.inteloper.R;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.widget.RippleView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Larry Liang on 01/05/2017.
 */

public class DeviceDefinitionModifyActivity extends BaseActivity implements RippleView.OnRippleCompleteListener {

    @Bind(R.id.layout_one_column)
    LinearLayout mOneColumnLayout;

    @Bind(R.id.layout_two_column)
    LinearLayout mTwoColumnLayout;

    @Bind(R.id.tv_name_one)
    TextView mNameOneTextView;

    @Bind(R.id.tv_name_two)
    TextView mNameTwoTextView;

    @Bind(R.id.ripple_one_one)
    RippleView mOneOneRipple;

    @Bind(R.id.ripple_two_one)
    RippleView mTwoOneRipple;

    @Bind(R.id.ripple_two_two)
    RippleView mTwoTwoRipple;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private MaterialDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_device_definition_modify);

        ButterKnife.bind(this);

        initData();

        initView();
    }

    private void initData() {
        Intent intent = getIntent();

        if (intent.getIntExtra("column", 0) == 1) {
            mTwoColumnLayout.setVisibility(View.GONE);

            mNameOneTextView.setText(intent.getStringExtra("name"));
        } else if (intent.getIntExtra("column", 0) == 2) {
            mOneColumnLayout.setVisibility(View.GONE);

            mNameTwoTextView.setText(intent.getStringExtra("name"));
        }
    }

    private void initView() {
        mOneOneRipple.setOnRippleCompleteListener(this);
        mTwoOneRipple.setOnRippleCompleteListener(this);
        mTwoTwoRipple.setOnRippleCompleteListener(this);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceDefinitionModifyActivity.this.finish();
            }
        });
    }

    @Override
    public void onComplete(RippleView rippleView) {
        mDialog = new MaterialDialog.Builder(this)
                .title("请输入新数值")
                .positiveText("保存")
                .negativeText("取消")
                .input("请输入新数值", "xxx", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                    }
                })
                .backgroundColor(Color.WHITE)
                .autoDismiss(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        UtilBox.toggleSoftInput(dialog.getView(), false);

                        Toast.makeText(DeviceDefinitionModifyActivity.this, "保存中", Toast.LENGTH_SHORT).show();

                        edit();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).build();

        mDialog.show();
    }

    private void edit() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDialog.dismiss();
                Toast.makeText(DeviceDefinitionModifyActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }
        }, 500);
    }
}
