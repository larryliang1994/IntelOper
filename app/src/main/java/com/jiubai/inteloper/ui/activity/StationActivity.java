package com.jiubai.inteloper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.bean.Station;
import com.jiubai.inteloper.bean.StationDevice;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.presenter.StationPresenterImpl;
import com.jiubai.inteloper.ui.iview.IStationView;
import com.jiubai.inteloper.widget.FullyLinearLayoutManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StationActivity extends BaseActivity implements IStationView {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.textView_name)
    TextView mNameTextView;

    @Bind(R.id.textView_desc)
    TextView mDescTextView;

    @Bind(R.id.textView_ip)
    TextView mIPTextView;

    private Station station;

    private DeviceAdapter adapter;

    private ArrayList<StationDevice> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_station);

        ButterKnife.bind(this);

        station = (Station) getIntent().getSerializableExtra("station");

        initView();

        UtilBox.showLoading(this);

        new StationPresenterImpl(this, this).getStationInfo(station.getName());
    }

    private void initView() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StationActivity.this.onBackPressed();
            }
        });

        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        deviceList = new ArrayList<>();

        adapter = new DeviceAdapter(this, deviceList);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onGetStationInfoResult(boolean result, String info, Object extras) {
        UtilBox.dismissLoading();

        if (result) {
            station = (Station) extras;

            mNameTextView.setText(station.getName());

            mDescTextView.setText(station.getRegion() + "/" + station.getGroup());

            mIPTextView.setText(station.getIp());
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEditStationInfoResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onAddStationResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onGetRegionListResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onGetGroupListResult(boolean result, String info, Object extras) {

    }

    @OnClick({R.id.layout_addDevice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_addDevice:
                deviceList.add(new StationDevice());
                adapter.notifyItemInserted(deviceList.size() - 1);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        UtilBox.returnActivity(this);
    }

    @Override
    public void onGetStationListResult(boolean result, String info, Object extras) {

    }

    class DeviceAdapter extends RecyclerView.Adapter {

        private Context mContext;
        private ArrayList<StationDevice> mList;

        public DeviceAdapter(Context context, ArrayList<StationDevice> list) {
            this.mContext = context;
            this.mList = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_station_device, parent, false);
            ContentViewHolder holder = new ContentViewHolder(view);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UtilBox.startActivity(StationActivity.this,
                            new Intent(StationActivity.this, StationDeviceActivity.class), false);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class ContentViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.layout)
            RelativeLayout layout;

            ContentViewHolder(View itemView) {
                super(itemView);

                ButterKnife.bind(this, itemView);
            }
        }
    }
}
