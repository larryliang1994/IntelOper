package com.jiubai.inteloper.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jiubai.inteloper.App;
import com.jiubai.inteloper.R;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.config.Config;
import com.jiubai.inteloper.presenter.ILoginPresenter;
import com.jiubai.inteloper.presenter.LoginPresenterImpl;
import com.jiubai.inteloper.ui.iview.ILoginView;
import com.jiubai.inteloper.widget.RippleView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Larry Liang on 26/04/2017.
 */

public class LoginActivity extends BaseActivity implements ILoginView, RippleView.OnRippleCompleteListener {

    @Bind(R.id.appbar)
    AppBarLayout mAppbarLayout;

    @Bind(R.id.ripple_login)
    RippleView mLoginRipple;

    @Bind(R.id.edt_account)
    EditText mAccountEditText;

    @Bind(R.id.edt_password)
    EditText mPasswordEditText;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private ILoginPresenter mLoginPresenter;
    private ProgressDialog mProgressDialog;

    private final int requestNum = 0;
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

        mLoginPresenter = new LoginPresenterImpl(this, this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("连接中...");

        final SharedPreferences sp = App.sp;

        mAccountEditText.setText(sp.getString("account", ""));
        mPasswordEditText.setText(sp.getString("password", ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, NetworkTestActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.textView_network_test})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_network_test:
                startActivity(new Intent(this, NetworkTestActivity.class));
                break;
        }
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
        if (result) {
            mProgressDialog.dismiss();

            SharedPreferences.Editor editor = App.sp.edit();
            editor.putString("account", mAccountEditText.getText().toString());
            editor.putString("password", mPasswordEditText.getText().toString());
            editor.apply();

            entry();
        } else {
            mProgressDialog.dismiss();
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    private void entry() {
        Intent intent = new Intent(this, MainActivity.class);
        UtilBox.startActivity(this, intent, true);
        finish();
    }
}
