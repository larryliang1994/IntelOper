package com.jiubai.inteloper.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jiubai.inteloper.R;
import com.jiubai.inteloper.bean.MyDate;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.presenter.TelemetryPresenterImpl;
import com.jiubai.inteloper.ui.iview.ITelemetryView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TelemetryActivity extends BaseActivity implements ITelemetryView {

    @Bind(R.id.textView_deviceName)
    TextView mDeviceNameTextView;

    @Bind(R.id.textView_type)
    TextView mTypeTextView;

    @Bind(R.id.textView_date)
    TextView mDateTextView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.chart)
    LineChart mChart;

    @Bind(R.id.textView_selected)
    TextView mSelectedTextView;

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
        mChart.getDescription().setEnabled(false);

        mChart.setDrawGridBackground(false);

        mChart.setNoDataText("暂无遥测数据");

        mChart.animateX(3000);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setDoubleTapToZoomEnabled(true);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                int value = (int)entry.getX();
                int hour = value / 60;
                int minute = value % 60;

                String time;

                if (minute < 10) {
                    time =  hour + ":0" + minute;
                } else {
                    time =  hour + ":" + minute;
                }

                DecimalFormat decimalFormat=new DecimalFormat("0.00");

                mSelectedTextView.setText("时间 " + time + "  值 " + decimalFormat.format(entry.getY()));
                //Toast.makeText(TelemetryActivity.this, entry.getX() + ", " + entry.getY(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

//        YAxis yAxis = mChart.getAxisLeft();
//        yAxis.setSpaceBottom(0.01f);
//        yAxis.setSpaceTop(0.01f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setLabelRotationAngle(-45);
        xAxis.setLabelCount(5, true);
        xAxis.setAxisMaximum(1439f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                int value = (int)v;
                int hour = value / 60;
                int minute = value % 60;

                if (minute < 10) {
                    return hour + ":0" + minute;
                } else {
                    return hour + ":" + minute;
                }
            }
        });
        //xAxis.setTextColor(Color.parseColor("#eeeeee"));

        mChart.getAxisRight().setEnabled(false);

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelemetryActivity.this.onBackPressed();
            }
        });

        mDeviceNameTextView.setText(deviceName);
        mTypeTextView.setText(type);

        UtilBox.showLoading(this);

        MyDate myDate = getMyDate();
        year = myDate.getYear();
        month = myDate.getMonth();
        day = myDate.getDay();

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

                DatePickerPopWin pickerPopWin = new DatePickerPopWin
                        .Builder(this, new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int monthOfYear, int dayOfMonth, String dateDesc) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear - 1, dayOfMonth);

                        if (calendar.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
                            Toast.makeText(TelemetryActivity.this, "所选时间不能大于当前时间", Toast.LENGTH_SHORT).show();

                            return;
                        }

                        TelemetryActivity.this.year = year;
                        TelemetryActivity.this.month = monthOfYear;
                        TelemetryActivity.this.day = dayOfMonth;

                        if (monthOfYear >= 10 && dayOfMonth >= 10) {
                            date = year + "-" + monthOfYear + "-" + dayOfMonth;
                        } else if (monthOfYear < 10 && dayOfMonth >= 10) {
                            date = year + "-0" + monthOfYear + "-" + dayOfMonth;
                        } else if (monthOfYear < 10 && dayOfMonth < 10) {
                            date = year + "-0" + monthOfYear + "-0" + dayOfMonth;
                        } else if (monthOfYear >= 10 && dayOfMonth < 10) {
                            date = year + "-" + monthOfYear + "-0" + dayOfMonth;
                        }

                        mDateTextView.setText(date);

                        UtilBox.showLoading(TelemetryActivity.this);

                        new TelemetryPresenterImpl(TelemetryActivity.this, TelemetryActivity.this)
                                .getTelemetryData(deviceName, type, date.replace("-", ""));
                    }
                })
                        .textConfirm("确定") //text of confirm button
                        .textCancel("关闭") //text of cancel button
                        .btnTextSize(16) // button text size
                        .viewTextSize(25) // pick view text size
                        .colorCancel(Color.parseColor("#999999")) //color of cancel button
                        .colorConfirm(Color.parseColor("#1676B3"))//color of confirm button
                        .minYear(1990) //min year in loop
                        .maxYear(2550) // max year in loop
                        //.showDayMonthYear(true) // shows like dd mm yyyy (default is false)
                        .dateChose(myDate.getYear() + "-" +  myDate.getMonth() + "-" +  myDate.getDay()) // date chose when init popwindow
                        .build();

                pickerPopWin.showPopWin(this);

                break;
        }
    }

    @Override
    public void onGetTelemetryDataResult(boolean result, String info, Object extras) {
        UtilBox.dismissLoading();

        if (result) {
            ArrayList<Float> values = (ArrayList<Float>) extras;

            if (values.size() < 1440) {
                if (mChart.getData() != null && mChart.getLineData() != null) {
                    mChart.clearValues();
                }

                Toast.makeText(this, info, Toast.LENGTH_SHORT).show();

                return;
            }

            if (values.size() <= 1440) {
                List<Entry> entries = new ArrayList<>();

                int totalNum = 1440;

                Calendar calendar = Calendar.getInstance();

                if (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) + 1 == month && calendar.get(Calendar.DAY_OF_MONTH) == day) {
                    totalNum = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE) - 2;

                    if (totalNum < 0) {
                        totalNum = 0;
                    }
                }

                for (int i = 0; i < totalNum; i++) {
                    entries.add(new Entry(i, values.get(i)));
                }

                LineDataSet dataSet = new LineDataSet(entries, "遥测");
                dataSet.setDrawCircles(false);
                dataSet.setColor(Color.parseColor("#1676B3"));
                dataSet.setHighlightEnabled(true); // allow highlighting for DataSet

                // set this to false to disable the drawing of highlight indicator (lines)
                dataSet.setDrawHighlightIndicators(true);
                dataSet.setHighLightColor(Color.RED);

                LineData lineData = new LineData(dataSet);
                mChart.setData(lineData);
                mChart.invalidate();
            } else {
                List<Entry> entriesA = new ArrayList<>();
                List<Entry> entriesB = new ArrayList<>();
                List<Entry> entriesC = new ArrayList<>();

                int totalNum = 1440;

                Calendar calendar = Calendar.getInstance();

                if (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) + 1 == month && calendar.get(Calendar.DAY_OF_MONTH) == day) {
                    totalNum = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE) - 2;

                    if (totalNum < 0) {
                        totalNum = 0;
                    }
                }

                for (int i = 0; i < totalNum; i++) {
                    entriesA.add(new Entry(i, values.get(i)));
                }

                for (int i = 0; i < totalNum; i++) {
                    entriesB.add(new Entry(i, values.get(i + 1440)));
                }

                for (int i = 0; i < totalNum; i++) {
                    entriesC.add(new Entry(i, values.get(i + 2880)));
                }

                LineDataSet dataSetA = new LineDataSet(entriesA, "A相");
                dataSetA.setDrawCircles(false);
                dataSetA.setColor(Color.RED);
                dataSetA.setHighlightEnabled(true); // allow highlighting for DataSet
                dataSetA.setDrawHighlightIndicators(true);
                dataSetA.setHighLightColor(Color.RED);

                LineDataSet dataSetB = new LineDataSet(entriesB, "B相");
                dataSetB.setDrawCircles(false);
                dataSetB.setColor(Color.BLUE);
                dataSetB.setHighlightEnabled(true); // allow highlighting for DataSet
                dataSetB.setDrawHighlightIndicators(true);
                dataSetB.setHighLightColor(Color.BLUE);

                LineDataSet dataSetC = new LineDataSet(entriesC, "C相");
                dataSetC.setDrawCircles(false);
                dataSetC.setColor(Color.GREEN);
                dataSetC.setHighlightEnabled(true); // allow highlighting for DataSet
                dataSetC.setDrawHighlightIndicators(true);
                dataSetC.setHighLightColor(Color.GREEN);

                ArrayList<ILineDataSet> sets = new ArrayList<>();
                sets.add(dataSetA);
                sets.add(dataSetB);
                sets.add(dataSetC);

                LineData lineData = new LineData(sets);
                mChart.setData(lineData);
                mChart.invalidate();
            }

        } else {
            if (mChart.getData() != null && mChart.getLineData() != null) {
                mChart.clearValues();
            }

            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    private MyDate getMyDate() {
//        if (TextUtils.isEmpty(date)) {
            int year, month, day;

            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            return new MyDate(year, month + 1, day, 0, 0);
//        } else {
//            return new MyDate(year, month, day, 0, 0);
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        UtilBox.returnActivity(this);
    }
}
