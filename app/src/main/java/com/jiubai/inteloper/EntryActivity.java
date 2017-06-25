package com.jiubai.inteloper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jiubai.inteloper.bean.Device;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.config.Config;
import com.jiubai.inteloper.presenter.DevicePresenterImpl;
import com.jiubai.inteloper.ui.activity.BaseActivity;
import com.jiubai.inteloper.ui.activity.LoginActivity;
import com.jiubai.inteloper.ui.activity.MainActivity;
import com.jiubai.inteloper.ui.iview.IDeviceView;
import com.umeng.analytics.MobclickAgent;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EntryActivity extends BaseActivity implements IDeviceView {

    @Bind(R.id.appbar)
    ImageView mAppbarImageView;

    @Bind(R.id.ll_no_network)
    LinearLayout mNoNetworkLinearLayout;

    private ProgressDialog mDialog;
    private Class mLauncherActivity;

    private final int requestNum = 1;
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

            new DevicePresenterImpl(EntryActivity.this).GetDeviceList();
        }
    }

    private void entry() {
        mDialog.dismiss();

        UtilBox.startActivity(this, new Intent(EntryActivity.this, mLauncherActivity), Pair.create(mAppbarImageView, "appbar"));

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

    @Override
    public void onGetDeviceListResult(boolean result, String info) {
        if (result) {
            initRequestNum ++;

            if (initRequestNum == requestNum) {
                entry();
            }
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();

            showReconnectDialog();
        }
    }

    private void showReconnectDialog() {
        mDialog.dismiss();
        initRequestNum = 0;

        mNoNetworkLinearLayout.setVisibility(View.VISIBLE);
    }
}