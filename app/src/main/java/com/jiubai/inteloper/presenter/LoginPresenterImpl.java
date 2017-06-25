package com.jiubai.inteloper.presenter;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jiubai.inteloper.config.Constants;
import com.jiubai.inteloper.net.VolleyUtil;
import com.jiubai.inteloper.ui.iview.ILoginView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by howell on 2015/11/28.
 * LoginPresenter实现类
 */
public class LoginPresenterImpl implements ILoginPresenter {
    private ILoginView mILoginView;

    public LoginPresenterImpl(ILoginView iLoginView) {
        this.mILoginView = iLoginView;
    }

    @Override
    public void doLogin(final String phoneNum, final String verifyCode) {

        final String[] soapKey = {"type", "table_name", "feedback_url", "return"};
        final String[] soapValue = {"mobile_login", "12345", "", "1"};

        VolleyUtil.requestWithCookie("", soapKey, soapValue,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseJson = new JSONObject(response);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mILoginView.onLoginResult(true, "");
                                }
                            }, 500);

                            /*
                            if (Constants.SUCCESS.equals(responseJson.getString("status"))) {
                                handleLoginResponse(responseJson.getString("memberCookie"));

                                mILoginView.onLoginResult(true,
                                        responseJson.getString("info"));
                            } else {
                                mILoginView.onLoginResult(false,
                                        responseJson.getString("info"));
                            }
                            */
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        mILoginView.onLoginResult(false, "登录失败，请重试");
                    }
                });
    }

    /**
     * 处理登录成功后的cookie
     *
     * @param cookie 登录成功后返回的cookie
     */
    private void handleLoginResponse(final String cookie) {
/*
        // 延长cookie可用时间
        SoapUtil.extendCookieLifeTime(cookie);

        // 保存cookie
        SharedPreferences.Editor editor = App.sp.edit();
        if (Config.COOKIE != null) {
            editor.putString(Constants.SP_KEY_COOKIE, Config.COOKIE);
            editor.putString(Constants.SP_KEY_MID, Config.MID);
            editor.putString(Constants.SP_KEY_NICKNAME, Config.NICKNAME);
            editor.putString(Constants.SP_KEY_PORTRAIT, Config.PORTRAIT);
        }

        editor.apply();
        */
    }
}