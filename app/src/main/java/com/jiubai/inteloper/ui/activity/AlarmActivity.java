package com.jiubai.inteloper.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.badoo.mobile.util.WeakHandler;
import com.jiubai.inteloper.R;
import com.jiubai.inteloper.adapter.AlarmAdapter;
import com.jiubai.inteloper.bean.Alarm;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.config.Config;
import com.jiubai.inteloper.manager.AlarmManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AlarmActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private ArrayList<Alarm> alarms;
    private String title;
    private AlarmAdapter adapter;
    private int mode;

    private WeakHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        ButterKnife.bind(this);

        title = getIntent().getStringExtra("title");
        mode = getIntent().getIntExtra("mode", AlarmAdapter.MODE_EXPAND);

        if ("实时告警".equals(title)) {
            alarms = AlarmManager.alarms;
        } else {
            alarms = Config.HistoryAlarms;
        }

        initView();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmActivity.this.onBackPressed();
            }
        });

        if (!TextUtils.isEmpty(title)) {
            mToolbar.setTitle(title);
        }

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new AlarmAdapter(this, alarms);
        adapter.setMode(mode);
        mRecyclerView.setAdapter(adapter);

        if (title.equals("实时告警")) {
            handler = new WeakHandler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    if (message.what == 1) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int dataSize = alarms.size();

                                alarms = AlarmManager.alarms;
                                adapter = new AlarmAdapter(AlarmActivity.this, alarms);
                                adapter.setMode(mode);

                                if (dataSize <= 8 || linearLayoutManager.findFirstVisibleItemPosition() == 0) {
                                    mRecyclerView.setLayoutManager(linearLayoutManager);
                                    mRecyclerView.setAdapter(adapter);
                                } else {
                                    adapter.notifyDataSetChanged();
                                }

                                if (AlarmManager.datasetChanged) {
                                    AlarmManager.datasetChanged = false;

                                    Toast.makeText(AlarmActivity.this, "有新告警送达", Toast.LENGTH_SHORT).show();

                                    if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {
                                        mRecyclerView.smoothScrollToPosition(0);
                                    }
                                }
                            }
                        });


                        handler.sendEmptyMessageDelayed(1, 5000);
                    }

                    return false;
                }
            });

            handler.sendEmptyMessageDelayed(1, 5000);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        UtilBox.returnActivity(this);
    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.sendEmptyMessage(0);
            handler.removeMessages(1);

            handler = null;
        }

        super.onDestroy();
    }
}
