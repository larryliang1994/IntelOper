package com.jiubai.inteloper.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.widget.RippleView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Larry Liang on 29/04/2017.
 */

public class SearchFragment extends Fragment implements RippleView.OnRippleCompleteListener {

    @Bind(R.id.ripple_fault)
    RippleView mFaultRipple;

    @Bind(R.id.ripple_history)
    RippleView mHistoryRipple;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, view);

        //initView(view);

        return view;
    }

    private void initView(View view) {
        mFaultRipple.setOnRippleCompleteListener(this);
        mHistoryRipple.setOnRippleCompleteListener(this);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        switch (rippleView.getId()) {
            case R.id.ripple_fault:
                break;

            case R.id.ripple_history:
                break;
        }
    }
}
