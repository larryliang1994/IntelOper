package com.jiubai.inteloper.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.bean.Station;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.presenter.StationPresenterImpl;
import com.jiubai.inteloper.ui.iview.IStationView;
import com.jiubai.inteloper.widget.RippleView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StationEditActivity extends BaseActivity implements IStationView {

    @Bind(R.id.textView_region)
    TextView mRegionTextView;

    @Bind(R.id.textView_group)
    TextView mGroupTextView;

    @Bind(R.id.editText_station)
    EditText mStationEditText;

    @Bind(R.id.editText_ip)
    EditText mIPEditText;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.ripple_save)
    RippleView mSaveRipple;

    @Bind(R.id.ripple_delete)
    RippleView mDeleteRipple;

    @Bind(R.id.space)
    Space mSpace;

    private int requestNum = 0;
    private int totalRequestNum = 2;

    private int regionIndex = -1;
    private int groupIndex = -1;

    private Station station;
    private int optType = -1;

    private String[] regions = {};
    private String[] groups = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_station_edit);

        ButterKnife.bind(this);

        station = (Station) getIntent().getSerializableExtra("station");

        initView();

        UtilBox.showLoading(this);

        new StationPresenterImpl(this, this).getRegionList();
        new StationPresenterImpl(this, this).getGroupList();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StationEditActivity.this.onBackPressed();
            }
        });

        if (station == null) {
            mDeleteRipple.setVisibility(View.GONE);
            mSpace.setVisibility(View.GONE);
        } else {
            mStationEditText.setText(station.getName());
            mGroupTextView.setText(station.getGroup());
            mRegionTextView.setText(station.getRegion());
            mIPEditText.setText(station.getIp());
        }

        mSaveRipple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                UtilBox.toggleSoftInput(mStationEditText, false);
                UtilBox.toggleSoftInput(mIPEditText, false);

                if (regionIndex == -1) {
                    Toast.makeText(StationEditActivity.this, "请选择区域", Toast.LENGTH_SHORT).show();
                    return;
                } else if (groupIndex == -1) {
                    Toast.makeText(StationEditActivity.this, "请选择班组", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(mStationEditText.getText().toString())) {
                    Toast.makeText(StationEditActivity.this, "请填写厂站名称", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(mIPEditText.getText().toString())) {
                    Toast.makeText(StationEditActivity.this, "请填写通信IP", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (station == null) {
                    optType = StationPresenterImpl.STATION_OPT_TYPE_ADD;
                } else {
                    optType = StationPresenterImpl.STATION_OPT_TYPE_EDIT;
                }

                station = new Station(
                        mStationEditText.getText().toString(),
                        regions[regionIndex], groups[groupIndex],
                        mIPEditText.getText().toString()
                );

                UtilBox.showLoading(StationEditActivity.this);

                new StationPresenterImpl(StationEditActivity.this, StationEditActivity.this)
                        .editStationInfo(station, optType);
            }
        });

        mDeleteRipple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                UtilBox.toggleSoftInput(mStationEditText, false);
                UtilBox.toggleSoftInput(mIPEditText, false);

                UtilBox.showLoading(StationEditActivity.this);

                optType = StationPresenterImpl.STATION_OPT_TYPE_DELETE;

                new StationPresenterImpl(StationEditActivity.this, StationEditActivity.this)
                        .editStationInfo(station, optType);
            }
        });
    }

    @Override
    public void onEditStationInfoResult(boolean result, final String info, Object extras) {
        UtilBox.dismissLoading();

        if (result) {
            if (optType == StationPresenterImpl.STATION_DEVICE_OPT_TYPE_ADD) {
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(StationEditActivity.this, StationActivity.class);
                        intent.putExtra("station", station);

                        UtilBox.startActivity(StationEditActivity.this, intent, true);
                    }
                }, 1000);

                return;
            } else if (optType == StationPresenterImpl.STATION_DEVICE_OPT_TYPE_EDIT) {
                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent();
            intent.putExtra("station", station);
            intent.putExtra("optType", optType);
            setResult(RESULT_OK, intent);
            UtilBox.returnActivity(this);

        } else {
            if (!info.equals("该厂站已存在")) {
                Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
            } else {
                UtilBox.alert(StationEditActivity.this, "该厂站已存在",
                        "进入该厂站", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(StationEditActivity.this, StationActivity.class);
                                intent.putExtra("station", station);

                                UtilBox.startActivity(StationEditActivity.this, intent, true);
                            }
                        }, "关闭", null);
            }
        }
    }

    @Override
    public void onGetRegionListResult(boolean result, String info, Object extras) {
        requestNum++;

        if (requestNum == totalRequestNum) {
            UtilBox.dismissLoading();
        }

        if (result) {
            regions = (String[]) extras;

            if (station != null) {
                for (int i = 0; i < regions.length; i++) {
                    if (station.getRegion().equals(regions[i])) {
                        regionIndex = i;
                        break;
                    }
                }
            }
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetGroupListResult(boolean result, String info, Object extras) {
        requestNum++;

        if (requestNum == totalRequestNum) {
            UtilBox.dismissLoading();
        }

        if (result) {
            groups = (String[]) extras;

            if (station != null) {
                for (int i = 0; i < groups.length; i++) {
                    if (station.getGroup().equals(groups[i])) {
                        groupIndex = i;
                        break;
                    }
                }
            }
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEditStationDeviceResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onGetStationDeviceListResult(boolean result, String info, Object extras) {

    }

    @OnClick({R.id.layout_region, R.id.layout_group})
    public void onClick(View view) {
        AlertDialog.Builder builder;
        AlertDialog dialog;

        switch (view.getId()) {
            case R.id.layout_region:
                builder = new AlertDialog.Builder(this)
                        .setItems(regions, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                regionIndex = which;

                                mRegionTextView.setText(regions[which]);
                            }
                        })
                        .setCancelable(true);
                dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.show();
                break;

            case R.id.layout_group:
                builder = new AlertDialog.Builder(this)
                        .setItems(groups, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                groupIndex = which;

                                mGroupTextView.setText(groups[which]);
                            }
                        })
                        .setCancelable(true);
                dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.show();
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

    @Override
    public void onGetStationInfoResult(boolean result, String info, Object extras) {

    }
}
