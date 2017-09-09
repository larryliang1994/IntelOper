package com.jiubai.inteloper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.bruce.pickerview.popwindow.TimePickerPopWin;
import com.jiubai.inteloper.R;
import com.jiubai.inteloper.adapter.AlarmAdapter;
import com.jiubai.inteloper.bean.Alarm;
import com.jiubai.inteloper.bean.MyDate;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.config.Config;
import com.jiubai.inteloper.presenter.AlarmPresenterImpl;
import com.jiubai.inteloper.ui.iview.IAlarmView;
import com.jiubai.inteloper.widget.RippleView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseDateActivity extends BaseActivity implements IAlarmView,
        DatePickerPopWin.OnDatePickedListener, TimePickerPopWin.OnTimePickListener {

    @Bind(R.id.textView_startTime)
    TextView mStartTimeTextView;

    @Bind(R.id.textView_endTime)
    TextView mEndTimeTextView;

    @Bind(R.id.ripple_query)
    RippleView mQueryRipple;

    @Bind(R.id.editText_keyword)
    EditText mKeywordEditText;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private String mStartDate = null, mEndDate = null;
    private int mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute;
    private int mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute;

    private String which = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseDateActivity.this.onBackPressed();
            }
        });

        mQueryRipple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (TextUtils.isEmpty(mStartDate)) {
                    Toast.makeText(ChooseDateActivity.this, "请选择开始时间", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(mEndDate)) {
                    Toast.makeText(ChooseDateActivity.this, "请选择结束时间", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Long.valueOf(mStartDate.replace("-", "").replace(":", "").replace(" ", "")) >=
                        Long.valueOf(mEndDate.replace("-", "").replace(":", "").replace(" ", ""))) {
                    Toast.makeText(ChooseDateActivity.this, "开始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
                    return;
                }

                UtilBox.showLoading(ChooseDateActivity.this);

                if (TextUtils.isEmpty(mKeywordEditText.getText().toString())) {
                    new AlarmPresenterImpl(ChooseDateActivity.this, ChooseDateActivity.this).getAlarmHistory(mStartDate, mEndDate, "");
                } else {
                    new AlarmPresenterImpl(ChooseDateActivity.this, ChooseDateActivity.this).getAlarmHistory(mStartDate, mEndDate, mKeywordEditText.getText().toString());
                }
            }
        });
    }

    @Override
    public void onGetAlarmHistoryResult(boolean result, String info, Object extras) {
        UtilBox.dismissLoading();

        if (result) {
            ArrayList<Alarm> alarms = (ArrayList<Alarm>) extras;

            Config.HistoryAlarms = null;
            Config.HistoryAlarms = new ArrayList<>();
            for (int i = 0; i < alarms.size(); i++) {
                Config.HistoryAlarms.add(0, alarms.get(i));
            }

            Intent intent = new Intent(this, AlarmActivity.class);
            intent.putExtra("mode", AlarmAdapter.MODE_EXPAND);
            intent.putExtra("title", "历史告警");
            UtilBox.startActivity(this, intent, false);
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.layout_startTime, R.id.layout_endTime})
    public void onClick(View view) {
        UtilBox.toggleSoftInput(mKeywordEditText, false);

        MyDate myDate;

        DatePickerPopWin pickerPopWin;

        switch (view.getId()) {
            case R.id.layout_startTime:
                which = "start";

                myDate = getMyDate("date");
                assert myDate != null;

                pickerPopWin = new DatePickerPopWin
                        .Builder(this, this)
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

            case R.id.layout_endTime:
                which = "end";

                myDate = getMyDate("date");

                assert myDate != null;

                pickerPopWin = new DatePickerPopWin
                        .Builder(this, this)
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

    private MyDate getMyDate(String which) {
        if ("date".equals(which)) {
            if (this.which.equals("start") && !TextUtils.isEmpty(mStartDate)) {
                return new MyDate(mStartYear, mStartMonth, mStartDay, 0, 0);
            } else if (this.which.equals("end") && !TextUtils.isEmpty(mEndDate)) {
                return new MyDate(mEndYear, mEndMonth, mEndDay, 0, 0);
            } else {
                int year, month, day;

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                return new MyDate(year, month + 1, day, 0, 0);
            }
        } else if ("time".equals(which)) {
            if (this.which.equals("start")
                    && !TextUtils.isEmpty(mStartDate)
                    && mStartHour != 0) {
                return new MyDate(0, 0, 0, mStartHour, mStartMinute);
            } else if (this.which.equals("end")
                    && !TextUtils.isEmpty(mEndDate)
                    && mEndHour != 0) {
                return new MyDate(0, 0, 0, mEndHour, mEndMinute);
            } else {
                int hour, minute;

                final Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

                return new MyDate(0, 0, 0, hour, minute);
            }
        } else {
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        Config.HistoryAlarms = null;

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        UtilBox.returnActivity(this);
    }

    @Override
    public void onDatePickCompleted(int year, int monthOfYear, int dayOfMonth, String dateDesc) {

        if (which.equals("start")) {
            if (monthOfYear >= 10 && dayOfMonth >= 10) {
                mStartDate = year + "-" + monthOfYear + "-" + dayOfMonth;
            } else if (monthOfYear < 10 && dayOfMonth >= 10) {
                mStartDate = year + "-0" + monthOfYear + "-" + dayOfMonth;
            } else if (monthOfYear < 10 && dayOfMonth < 10) {
                mStartDate = year + "-0" + monthOfYear + "-0" + dayOfMonth;
            } else if (monthOfYear >= 10 && dayOfMonth < 10) {
                mStartDate = year + "-" + monthOfYear + "-0" + dayOfMonth;
            }

            mStartYear = year;
            mStartMonth = monthOfYear;
            mStartDay = dayOfMonth;
        } else {
            if (monthOfYear >= 10 && dayOfMonth >= 10) {
                mEndDate = year + "-" + monthOfYear + "-" + dayOfMonth;
            } else if (monthOfYear < 10 && dayOfMonth >= 10) {
                mEndDate = year + "-0" + monthOfYear + "-" + dayOfMonth;
            } else if (monthOfYear < 10 && dayOfMonth < 10) {
                mEndDate = year + "-0" + monthOfYear + "-0" + dayOfMonth;
            } else if (monthOfYear >= 10 && dayOfMonth < 10) {
                mEndDate = year + "-" + monthOfYear + "-0" + dayOfMonth;
            }

            mEndYear = year;
            mEndMonth = monthOfYear;
            mEndDay = dayOfMonth;
        }

        MyDate myDate = getMyDate("time");
        assert myDate != null;

        TimePickerPopWin timePickerPopWin = new TimePickerPopWin
                .Builder(this, this)
                .textConfirm("确定")
                .textCancel("关闭")
                .btnTextSize(16)
                .viewTextSize(25)
                .colorCancel(Color.parseColor("#999999"))
                .colorConfirm(Color.parseColor("#1676B3"))
                .build();
        timePickerPopWin.showPopWin(this);
    }

    @Override
    public void onTimePickCompleted(int hourOfDay, int minute, String AM_PM, String time) {

        if ("PM".equals(AM_PM)) {
            hourOfDay += 12;
        }

        if (which.equals("start")) {
            if (minute >= 10 && hourOfDay >= 10) {
                mStartDate += "  " + hourOfDay + ":" + minute;
            } else if (minute < 10 && hourOfDay >= 10) {
                mStartDate += "  " + hourOfDay + ":0" + minute;
            } else if (minute < 10 && hourOfDay < 10) {
                mStartDate += "  0" + hourOfDay + ":0" + minute;
            } else if (minute >= 10 && hourOfDay < 10) {
                mStartDate += "  0" + hourOfDay + ":" + minute;
            }

            mStartTimeTextView.setText(mStartDate);

            mStartHour = hourOfDay;
            mStartMinute = minute;
        } else {
            if (minute >= 10 && hourOfDay >= 10) {
                mEndDate += "  " + hourOfDay + ":" + minute;
            } else if (minute < 10 && hourOfDay >= 10) {
                mEndDate += "  " + hourOfDay + ":0" + minute;
            } else if (minute < 10 && hourOfDay < 10) {
                mEndDate += "  0" + hourOfDay + ":0" + minute;
            } else if (minute >= 10 && hourOfDay < 10) {
                mEndDate += "  0" + hourOfDay + ":" + minute;
            }

            mEndTimeTextView.setText(mEndDate);

            mEndHour = hourOfDay;
            mEndMinute = minute;
        }
    }
}
