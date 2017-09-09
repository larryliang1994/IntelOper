package com.jiubai.inteloper.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.badoo.mobile.util.WeakHandler;
import com.jiubai.inteloper.R;
import com.jiubai.inteloper.adapter.AlarmAdapter;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.manager.AlarmManager;
import com.jiubai.inteloper.ui.activity.AlarmActivity;
import com.jiubai.inteloper.ui.activity.ChooseDateActivity;
import com.jiubai.inteloper.ui.activity.ChooseOperActivity;
import com.jiubai.inteloper.ui.activity.DeviceListActivity;
import com.jiubai.inteloper.widget.IndexBar;
import com.jiubai.inteloper.widget.RippleView;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    @Bind(R.id.ripple_alarm_history)
    RippleView mAlarmHistoryRipple;

    @Bind(R.id.textView_fault)
    TextView mFaultTextView;

    private static final int UPDATE_ALARM_FAULT_INTERVAL = 1000;

    private WeakHandler alarmHandler;

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
        mAlarmHistoryRipple.setOnRippleCompleteListener(this);

        mAppbarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AlarmActivity.class);
                intent.putExtra("title", "实时告警");
                intent.putExtra("mode", AlarmAdapter.MODE_BREVIARY);
                UtilBox.startActivity(getActivity(), intent, false);
            }
        });

        alarmHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message.what == 0) {
                    mFaultTextView.setText(String.valueOf(AlarmManager.getInstance().getAbnormalNum()));

                    alarmHandler.sendEmptyMessageDelayed(0, UPDATE_ALARM_FAULT_INTERVAL);
                } else if (message.what == 1) {
                    alarmHandler.removeMessages(0);
                    alarmHandler = null;
                }
                return false;
            }
        });

        alarmHandler.sendEmptyMessageDelayed(0, UPDATE_ALARM_FAULT_INTERVAL);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        Intent intent;

        switch (rippleView.getId()) {
            case R.id.ripple_monitor:
                IndexBar.offset = UtilBox.dip2px(getActivity(), 0);

                intent = new Intent(getActivity(), DeviceListActivity.class);
                intent.putExtra("source", DeviceListActivity.SOURCE_MONITOR);
                UtilBox.startActivity(getActivity(), intent, false);
                break;

            case R.id.ripple_definition:
                intent = new Intent(getActivity(), ChooseOperActivity.class);
                UtilBox.startActivity(getActivity(), intent, false);
                break;

            case R.id.ripple_alarm_history:
                intent = new Intent(getActivity(), ChooseDateActivity.class);
                UtilBox.startActivity(getActivity(), intent, false);
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (alarmHandler != null) {
            alarmHandler.removeMessages(0);
            alarmHandler = null;
        }

        AlarmManager.getInstance().stopListen();

        super.onDestroy();
    }
}
