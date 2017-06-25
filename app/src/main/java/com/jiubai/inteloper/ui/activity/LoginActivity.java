package com.jiubai.inteloper.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jiubai.inteloper.EntryActivity;
import com.jiubai.inteloper.R;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.config.Config;
import com.jiubai.inteloper.presenter.DevicePresenterImpl;
import com.jiubai.inteloper.presenter.ILoginPresenter;
import com.jiubai.inteloper.presenter.LoginPresenterImpl;
import com.jiubai.inteloper.ui.iview.IDeviceView;
import com.jiubai.inteloper.ui.iview.ILoginView;
import com.jiubai.inteloper.widget.RippleView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Larry Liang on 26/04/2017.
 */

public class LoginActivity extends BaseActivity implements ILoginView, RippleView.OnRippleCompleteListener, IDeviceView {

    @Bind(R.id.appbar)
    AppBarLayout mAppbarLayout;

    @Bind(R.id.ripple_login)
    RippleView mLoginRipple;

    @Bind(R.id.edt_account)
    EditText mAccountEditText;

    @Bind(R.id.edt_password)
    EditText mPasswordEditText;

    private ILoginPresenter mLoginPresenter;
    private ProgressDialog mProgressDialog;

    private final int requestNum = 1;
    private int initRequestNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mLoginRipple.setOnRippleCompleteListener(this);

        mLoginPresenter = new LoginPresenterImpl(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("连接中...");
    }

    @Override
    public void onComplete(RippleView rippleView) {
        switch (rippleView.getId()) {
            case R.id.ripple_login:
                UtilBox.toggleSoftInput(mPasswordEditText, false);

                if (!Config.IS_CONNECTED) {
                    Toast.makeText(this, "啊哦，网络好像抽风了~", Toast.LENGTH_SHORT).show();
                    return;
                }

                String account = mAccountEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();

                if (account.isEmpty()) {
                    Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.isEmpty()) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                mProgressDialog.show();

                mLoginPresenter.doLogin(account, password);
                break;
        }
    }

    @Override
    public void onLoginResult(boolean result, String info) {
        mProgressDialog.hide();

        if (result) {
            new DevicePresenterImpl(this).GetDeviceList();
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
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
        }
    }

    private void entry() {
        Intent intent = new Intent(this, MainActivity.class);
        UtilBox.startActivity(this, intent, Pair.create(mAppbarLayout, "appbar"));
        finish();
    }
}
