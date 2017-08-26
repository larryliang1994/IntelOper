package com.jiubai.inteloper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.config.Config;
import com.jiubai.inteloper.ui.activity.BaseActivity;
import com.jiubai.inteloper.ui.activity.LoginActivity;
import com.jiubai.inteloper.ui.activity.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EntryActivity extends BaseActivity {

    @Bind(R.id.appbar)
    ImageView mAppbarImageView;

    @Bind(R.id.ll_no_network)
    LinearLayout mNoNetworkLinearLayout;

    private ProgressDialog mDialog;
    private Class mLauncherActivity;

    private final int requestNum = 0;
    private int initRequestNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_entry);

        ButterKnife.bind(this);

        setLauncherActivity();

        initData();
    }

    private void initData() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("连接中...");

        if (TextUtils.isEmpty(Config.COOKIE)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    entry();
                }
            }, 500);

        } else {
            mDialog.show();

            if (!Config.IS_CONNECTED) {
                showReconnectDialog();

                Toast.makeText(EntryActivity.this, "啊哦，网络好像抽风了~", Toast.LENGTH_SHORT).show();

                return;
            }

            entry();
        }
    }

    private void entry() {
        mDialog.dismiss();

        UtilBox.startActivity(this, new Intent(EntryActivity.this, mLauncherActivity), true);

        finish();
    }

    private void setLauncherActivity() {
        if (Config.COOKIE == null || Config.COOKIE.isEmpty()) {
            mLauncherActivity = LoginActivity.class;
        } else {
            mLauncherActivity = MainActivity.class;
        }
    }

    @OnClick(R.id.btn_reconnect)
    public void onClick(View v) {
        initData();
    }

    private void showReconnectDialog() {
        mDialog.dismiss();
        initRequestNum = 0;

        mNoNetworkLinearLayout.setVisibility(View.VISIBLE);
    }
}