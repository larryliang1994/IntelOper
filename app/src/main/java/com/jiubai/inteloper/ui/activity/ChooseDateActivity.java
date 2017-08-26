package com.jiubai.inteloper.ui.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.adapter.AlarmAdapter;
import com.jiubai.inteloper.bean.Alarm;
import com.jiubai.inteloper.bean.MyDate;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.presenter.AlarmPresenterImpl;
import com.jiubai.inteloper.ui.iview.IAlarmView;
import com.jiubai.inteloper.widget.RippleView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseDateActivity extends BaseActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, IAlarmView {

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

            Intent intent = new Intent(this, AlarmActivity.class);
            intent.putExtra("alarms", alarms);
            intent.putExtra("mode", AlarmAdapter.MODE_EXPAND);
            intent.putExtra("title", "历史告警");
            UtilBox.startActivity(this, intent, false);
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.layout_startTime, R.id.layout_endTime})
    public void onClick(View view) {
        MyDate myDate;
        DatePickerDialog dateDialog;

        switch (view.getId()) {
            case R.id.layout_startTime:
                which = "start";

                myDate = getMyDate("date");
                assert myDate != null;
                dateDialog = new DatePickerDialog(
                        ChooseDateActivity.this,
                        ChooseDateActivity.this,
                        myDate.getYear(), myDate.getMonth(), myDate.getDay());

                dateDialog.show();
                break;

            case R.id.layout_endTime:
                which = "end";

                myDate = getMyDate("date");
                assert myDate != null;
                dateDialog = new DatePickerDialog(
                        ChooseDateActivity.this,
                        ChooseDateActivity.this,
                        myDate.getYear(), myDate.getMonth(), myDate.getDay());

                dateDialog.show();
                break;
        }
    }

    private MyDate getMyDate(String which) {
        if ("date".equals(which)) {
            if (this.which.equals("start") && !TextUtils.isEmpty(mStartDate)) {
                return new MyDate(mStartYear, mStartMonth - 1, mStartDay, 0, 0);
            } else if (this.which.equals("end") && !TextUtils.isEmpty(mEndDate)) {
                return new MyDate(mEndYear, mEndMonth - 1, mEndDay, 0, 0);
            } else {
                int year, month, day;

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                return new MyDate(year, month, day, 0, 0);
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
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;

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
        TimePickerDialog dialog = new TimePickerDialog(this, this, myDate.getHour(), myDate.getMinute(), true);
        dialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        UtilBox.returnActivity(this);
    }
}
