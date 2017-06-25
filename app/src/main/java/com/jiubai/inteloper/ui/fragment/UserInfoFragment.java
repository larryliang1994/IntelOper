package com.jiubai.inteloper.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiubai.inteloper.R;

import butterknife.ButterKnife;

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
}
