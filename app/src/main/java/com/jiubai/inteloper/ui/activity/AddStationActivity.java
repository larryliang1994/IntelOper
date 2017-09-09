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

public class AddStationActivity extends BaseActivity implements IStationView {

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

    @Bind(R.id.ripple_query)
    RippleView mQueryRipple;

    private int requestNum = 0;
    private int totalRequestNum = 2;

    private int regionIndex = -1;
    private int groupIndex = -1;

    private Station station;

    private String[] regions = {"这是一个区", "这是另一个区", "这又是一个区", "这还是一个区"};
    private String[] groups = {"这是一个班组", "这是另一个班组", "这又是一个班组", "这还是一个班组"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_station_add);

        ButterKnife.bind(this);

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
                AddStationActivity.this.onBackPressed();
            }
        });

        mQueryRipple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                if (regionIndex == -1) {
                    Toast.makeText(AddStationActivity.this, "请选择区域", Toast.LENGTH_SHORT).show();
                    return;
                } else if (groupIndex == -1) {
                    Toast.makeText(AddStationActivity.this, "请选择班组", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(mStationEditText.getText().toString())) {
                    Toast.makeText(AddStationActivity.this, "请填写厂站名称", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(mIPEditText.getText().toString())) {
                    Toast.makeText(AddStationActivity.this, "请填写通信IP", Toast.LENGTH_SHORT).show();
                    return;
                }

                station = new Station(
                        mStationEditText.getText().toString(),
                        regions[regionIndex], groups[groupIndex],
                        mIPEditText.getText().toString()
                );

                UtilBox.showLoading(AddStationActivity.this);

                new StationPresenterImpl(AddStationActivity.this, AddStationActivity.this)
                        .addStation(station);
            }
        });
    }

    @Override
    public void onAddStationResult(boolean result, final String info, Object extras) {
        UtilBox.dismissLoading();

        if (result) {
            Toast.makeText(this, "添加厂站成功", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(AddStationActivity.this, StationActivity.class);
                    intent.putExtra("station", station);

                    UtilBox.startActivity(AddStationActivity.this, intent, true);
                }
            }, 1000);
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
//            UtilBox.alert(AddStationActivity.this, "该厂站已存在",
//                    "进入该厂站", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Intent intent = new Intent(AddStationActivity.this, StationActivity.class);
//                            intent.putExtra("station", station);
//
//                            UtilBox.startActivity(AddStationActivity.this, intent, true);
//                        }
//                    }, "关闭", null);
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
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onEditStationInfoResult(boolean result, String info, Object extras) {

    }
}
