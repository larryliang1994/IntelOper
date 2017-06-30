package com.jiubai.inteloper.presenter;

import com.jiubai.inteloper.ui.iview.ILoginView;

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
        mILoginView.onLoginResult(true, "");
    }
}