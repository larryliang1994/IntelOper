package com.jiubai.inteloper.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.ui.activity.DeviceMonitorActivity;
import com.jiubai.inteloper.ui.activity.SearchActivity;
import com.jiubai.inteloper.widget.RippleView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Larry Liang on 29/04/2017.
 */

public class HomeFragment extends Fragment implements RippleView.OnRippleCompleteListener {

    @Bind(R.id.appbar)
    AppBarLayout mAppbarLayout;

    @Bind(R.id.ripple_monitor)
    RippleView mMonitorRipple;

    @Bind(R.id.ripple_definition)
    RippleView mDefinitionRipple;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        initView(view);

        return view;
    }

    private void initView(View view) {
        mMonitorRipple.setOnRippleCompleteListener(this);
        mDefinitionRipple.setOnRippleCompleteListener(this);
    }

    @OnClick({R.id.layout_search, R.id.button_check})
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.layout_search:
                intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("source", "definition");
                UtilBox.startActivity(getActivity(), intent, Pair.create(mAppbarLayout, "appbar"));
                break;

            case R.id.button_check:
                break;
        }
    }

    @Override
    public void onComplete(RippleView rippleView) {
        Intent intent;

        switch (rippleView.getId()) {
            case R.id.ripple_monitor:
                intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("source", "monitor");
                UtilBox.startActivity(getActivity(), intent, Pair.create(mAppbarLayout, "appbar"));
                break;

            case R.id.ripple_definition:
                intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("source", "definition");
                UtilBox.startActivity(getActivity(), intent, Pair.create(mAppbarLayout, "appbar"));
                break;
        }
    }
}
