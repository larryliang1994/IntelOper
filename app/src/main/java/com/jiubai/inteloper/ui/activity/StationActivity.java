package com.jiubai.inteloper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

    private int requestNum = 0;
    private int totalRequestNum = 2;

    private Station station;

    private DeviceAdapter adapter;

    private ArrayList<StationDevice> deviceList;

    private FullyLinearLayoutManager linearLayoutManager;

    private int editDeviceIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_station);

        ButterKnife.bind(this);

        station = (Station) getIntent().getSerializableExtra("station");

        initView();

        UtilBox.showLoading(this);

        new StationPresenterImpl(this, this).getStationInfo(station.getName());
        new StationPresenterImpl(this, this).getDeviceList(station.getName());
    }

    private void initView() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StationActivity.this.onBackPressed();
            }
        });

        linearLayoutManager = new FullyLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        deviceList = new ArrayList<>();

        adapter = new DeviceAdapter(this, deviceList);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onGetStationInfoResult(boolean result, String info, Object extras) {
        requestNum++;

        if (requestNum == totalRequestNum) {
            UtilBox.dismissLoading();
        }

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
    public void onGetStationDeviceListResult(boolean result, String info, Object extras) {
        requestNum++;

        if (requestNum == totalRequestNum) {
            UtilBox.dismissLoading();
        }

        if (result) {
            deviceList = (ArrayList<StationDevice>) extras;
            adapter = new DeviceAdapter(this, deviceList);
            mRecyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.layout_addDevice, R.id.layout_editStation})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_addDevice:
                Intent intent1 = new Intent(this, StationDeviceActivity.class);
                intent1.putExtra("stationName", station.getName());

                startActivityForResult(intent1, 111);
                overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
                break;

            case R.id.layout_editStation:
                Intent intent = new Intent(this, StationEditActivity.class);
                intent.putExtra("station", station);

                startActivityForResult(intent, 222);
                overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        UtilBox.returnActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 111:
                if (resultCode == RESULT_OK) {
                    int optType = data.getIntExtra("optType", -1);
                    StationDevice stationDevice = (StationDevice) data.getSerializableExtra("stationDevice");

                    if (optType == -1) {
                        return;
                    } else if (optType == StationPresenterImpl.STATION_DEVICE_OPT_TYPE_ADD) {
                        deviceList.add(stationDevice);
                    } else if (optType == StationPresenterImpl.STATION_DEVICE_OPT_TYPE_EDIT) {
                        deviceList.get(editDeviceIndex).setName(stationDevice.getName());
                        //deviceList.get(editDeviceIndex).setRtu(stationDevice.getRtu());
                    } else if (optType == StationPresenterImpl.STATION_DEVICE_OPT_TYPE_DELETE) {
                        for (int i = 0; i < deviceList.size(); i++) {
                            if (deviceList.get(i).getName().equals(stationDevice.getName())) {
                                deviceList.remove(i);
                                break;
                            }
                        }
                    }

                    adapter = new DeviceAdapter(StationActivity.this, deviceList);

                    if (deviceList.size() <= 8 || linearLayoutManager.findFirstVisibleItemPosition() == 0) {
                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        mRecyclerView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
                break;

            case 222:
                if (resultCode == RESULT_OK) {
                    int optType = data.getIntExtra("optType", -1);
                    station = (Station) data.getSerializableExtra("station");

                    if (optType == -1) {
                        return;
                    } else if (optType == StationPresenterImpl.STATION_DEVICE_OPT_TYPE_EDIT) {
                        mNameTextView.setText(station.getName());

                        //mDescTextView.setText(station.getRegion() + "/" + station.getGroup());

                        mIPTextView.setText(station.getIp());

                        setResult(RESULT_OK);
                    } else if (optType == StationPresenterImpl.STATION_DEVICE_OPT_TYPE_DELETE) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setResult(RESULT_OK);
                                UtilBox.returnActivity(StationActivity.this);
                            }
                        }, 150);
                    }
                }
                break;
        }
    }

    @Override
    public void onEditStationInfoResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onGetRegionListResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onGetGroupListResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onEditStationDeviceResult(boolean result, String info, Object extras) {

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
            return new ContentViewHolder(view);
        }
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ContentViewHolder viewHolder = (ContentViewHolder) holder;

            //station = (Station) getIntent().getSerializableExtra("station");

            final StationDevice stationDevice = mList.get(position);

            viewHolder.nameTextView.setText(stationDevice.getName());

            viewHolder.layoutContentViewHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editDeviceIndex = position;

                    Intent intent = new Intent(StationActivity.this, StationDeviceActivity.class);
                    intent.putExtra("stationName", station.getName());
                    intent.putExtra("stationDevice", stationDevice);

                    StationActivity.this.startActivityForResult(intent, 111);
                    StationActivity.this.overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class ContentViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.layout)
            RelativeLayout layoutContentViewHolder;

            @Bind(R.id.textView_name)
            TextView nameTextView;

            //@Bind(R.id.textView_desc)
            //TextView descTextView;

            ContentViewHolder(View itemView) {
                super(itemView);

                ButterKnife.bind(this, itemView);
            }
        }
    }
}
