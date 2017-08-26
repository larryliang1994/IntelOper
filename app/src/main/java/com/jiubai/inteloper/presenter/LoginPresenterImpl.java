package com.jiubai.inteloper.presenter;

import android.app.Activity;

import com.jiubai.inteloper.ui.iview.ILoginView;

/**
 * Created by howell on 2015/11/28.
 * LoginPresenter实现类
 */
public class LoginPresenterImpl implements ILoginPresenter {
    private ILoginView mILoginView;
    private Activity mActivity;

    public LoginPresenterImpl(Activity activity, ILoginView iLoginView) {
        mActivity = activity;
        this.mILoginView = iLoginView;
    }

    @Override
    public void doLogin(final String userName, final String password) {
//        byte[] requestCode = DataTypeConverter.int2byte(0); // 操作码
//        byte[] msgNum = DataTypeConverter.int2byte(1); // 消息数
//
//        Charset cs = Charset.forName("GBK");
//        byte[] b1 = userName.getBytes(cs);
//        byte[] b2 = new byte[20 - b1.length];
//        byte[] b3 = password.getBytes(cs);
//        byte[] b4 = new byte[10 - b3.length];
//
//        // 把所有字节合并成一条
//        byte[] input = DataTypeConverter.concatAll(requestCode, msgNum, b1, b2, b3, b4);
//        final int requestMsgLength = 19 + 64;
//
//        RequestUtil.request(input, 16, requestMsgLength, false,
//                new RequestUtil.RequestCallback() {
//                    @Override
//                    public void success(int msgNum, byte[] msgContent) {
//                        int success = DataTypeConverter.byte2int(DataTypeConverter.readBytes(msgContent, 0, 4));
//
//                        if (success == 1) {
//                            mActivity.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mILoginView.onLoginResult(true, "");
//                                }
//                            });
//                        } else {
//                            mActivity.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mILoginView.onLoginResult(true, "账号/密码错误");
//                                }
//                            });
//                        }
//                    }
//
//                    @Override
//                    public void error(String info, Exception exception) {
//                        mActivity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mILoginView.onLoginResult(true, "登录失败");
//                            }
//                        });
//                    }
//                });

        mILoginView.onLoginResult(true, "");
    }
}