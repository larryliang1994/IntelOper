package com.jiubai.inteloper.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.adapter.AlarmAdapter;
import com.jiubai.inteloper.bean.Alarm;
import com.jiubai.inteloper.common.UtilBox;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        ButterKnife.bind(this);

        alarms = (ArrayList<Alarm>) getIntent().getSerializableExtra("alarms");
        title = getIntent().getStringExtra("title");
        mode = getIntent().getIntExtra("mode", AlarmAdapter.MODE_EXPAND);

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new AlarmAdapter(this, alarms);
        adapter.setMode(mode);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        UtilBox.returnActivity(this);
    }
}
