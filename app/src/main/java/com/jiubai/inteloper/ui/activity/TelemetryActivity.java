package com.jiubai.inteloper.ui.activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.bean.MyDate;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.presenter.TelemetryPresenterImpl;
import com.jiubai.inteloper.ui.iview.ITelemetryView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TelemetryActivity extends BaseActivity implements ITelemetryView {

    @Bind(R.id.graph)
    GraphView mGraphView;

    @Bind(R.id.textView_deviceName)
    TextView mDeviceNameTextView;

    @Bind(R.id.textView_type)
    TextView mTypeTextView;

    @Bind(R.id.textView_date)
    TextView mDateTextView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private String date;
    private int year, month, day;

    private String deviceName;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telemetry);

        ButterKnife.bind(this);

        deviceName = getIntent().getStringExtra("deviceName");
        type = getIntent().getStringExtra("type");

        initView();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelemetryActivity.this.onBackPressed();
            }
        });

        mDeviceNameTextView.setText(deviceName);
        mTypeTextView.setText(type);

        mGraphView.getViewport().setXAxisBoundsManual(true);
        mGraphView.getViewport().setMinX(0);
        mGraphView.getViewport().setMaxX(1440);

        GridLabelRenderer renderer = mGraphView.getGridLabelRenderer();
        renderer.setHorizontalLabelsColor(Color.BLACK);
        renderer.setVerticalLabelsColor(Color.BLACK);

        UtilBox.showLoading(this);

        MyDate myDate = getMyDate();
        int year = myDate.getYear();
        int month = myDate.getMonth() + 1;
        int day = myDate.getDay();

        if (month >= 10 && day >= 10) {
            date = year + "-" + month + "-" + day;
        } else if (month < 10 && day >= 10) {
            date = year + "-0" + month + "-" + day;
        } else if (month < 10 && day < 10) {
            date = year + "-0" + month + "-0" + day;
        } else if (month >= 10 && day < 10) {
            date = year + "-" + month + "-0" + day;
        }

        mDateTextView.setText(date);

        new TelemetryPresenterImpl(this, this).getTelemetryData(deviceName, type, date.replace("-", ""));
    }

    @OnClick({R.id.layout_chooseDate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_chooseDate:

                MyDate myDate = getMyDate();

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                TelemetryActivity.this.year = year;
                                TelemetryActivity.this.month = monthOfYear;
                                TelemetryActivity.this.day = dayOfMonth;

                                if (monthOfYear + 1 >= 10 && dayOfMonth >= 10) {
                                    date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                } else if (monthOfYear + 1 < 10 && dayOfMonth >= 10) {
                                    date = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                                } else if (monthOfYear + 1 < 10 && dayOfMonth < 10) {
                                    date = year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth;
                                } else if (monthOfYear + 1 >= 10 && dayOfMonth < 10) {
                                    date = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                                }

                                mDateTextView.setText(date);

                                UtilBox.showLoading(TelemetryActivity.this);

                                new TelemetryPresenterImpl(TelemetryActivity.this, TelemetryActivity.this)
                                        .getTelemetryData(deviceName, type, date.replace("-", ""));
                            }
                        }, myDate.getYear(), myDate.getMonth(), myDate.getDay());
                datePickerDialog.show();
                break;
        }
    }

    @Override
    public void onGetTelemetryDataResult(boolean result, String info, Object extras) {
        UtilBox.dismissLoading();

        if (result) {
            ArrayList<Float> values = (ArrayList<Float>) extras;

            DataPoint[] dataPoints = new DataPoint[1440];
            for (int i = 0; i < 1440; i++) {
                dataPoints[i] = new DataPoint(i, values.get(i));
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
            mGraphView.addSeries(series);
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    private MyDate getMyDate() {
        if (TextUtils.isEmpty(date)) {
            int year, month, day;

            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            return new MyDate(year, month, day, 0, 0);
        } else {
            return new MyDate(year, month, day, 0, 0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        UtilBox.returnActivity(this);
    }
}
