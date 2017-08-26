package com.jiubai.inteloper.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.bean.Alarm;
import com.jiubai.inteloper.common.UtilBox;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AlarmDetailActivity extends BaseActivity {

    @Bind(R.id.textView_status)
    TextView mStatusTextView;

    @Bind(R.id.textView_time)
    TextView mTimeTextView;

    @Bind(R.id.textView_desc)
    TextView mDescTextView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private Alarm alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm_detail);

        ButterKnife.bind(this);

        alarm = (Alarm) getIntent().getSerializableExtra("alarm");

        initView();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmDetailActivity.this.onBackPressed();
            }
        });

        if (alarm.getStatus() == Alarm.STATUS_NORMAL) {
            mStatusTextView.setTextColor(Color.parseColor("#09BB07"));
        } else {
            mStatusTextView.setTextColor(Color.parseColor("#FF3B30"));
        }

        switch (alarm.getStatus()) {
            case Alarm.STATUS_NORMAL:
                mStatusTextView.setText("正常");
                break;

            case Alarm.STATUS_UP:
                mStatusTextView.setText("越上限");
                break;

            case Alarm.STATUS_DOWN:
                mStatusTextView.setText("越下限");
                break;

            case Alarm.STATUS_UP_2:
                mStatusTextView.setText("越上限2");
                break;

            case Alarm.STATUS_DOWN_2:
                mStatusTextView.setText("越下限2");
                break;
        }

        mTimeTextView.setText(alarm.getOccurTime());

        mDescTextView.setText(alarm.getWarnStr());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        UtilBox.returnActivity(this);
    }
}
