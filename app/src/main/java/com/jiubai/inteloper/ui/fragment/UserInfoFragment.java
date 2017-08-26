package com.jiubai.inteloper.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.ui.activity.FeedbackActivity;
import com.jiubai.inteloper.ui.activity.LoginActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Larry Liang on 29/04/2017.
 */

public class UserInfoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userinfo, container, false);

        ButterKnife.bind(this, view);

        //initView(view);

        return view;
    }

    @OnClick({R.id.layout_about, R.id.layout_feedback, R.id.layout_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_about:
                UtilBox.alert(getActivity(), "软件版本：v" + UtilBox.getPackageInfo(getActivity()).versionName, "关闭", null);
                break;

            case R.id.layout_feedback:
                UtilBox.startActivity(getActivity(), new Intent(getActivity(), FeedbackActivity.class), false);
                break;

            case R.id.layout_logout:
                UtilBox.alert(getActivity(), "确定要退出登录吗",
                        "退出登录", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                UtilBox.startActivity(getActivity(), new Intent(getActivity(), LoginActivity.class), true);
                            }
                        },
                        "关闭", null);
                break;
        }
    }
}
