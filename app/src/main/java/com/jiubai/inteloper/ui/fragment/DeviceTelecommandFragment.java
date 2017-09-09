package com.jiubai.inteloper.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.badoo.mobile.util.WeakHandler;
import com.jiubai.inteloper.R;
import com.jiubai.inteloper.bean.Device;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.presenter.DevicePresenterImpl;
import com.jiubai.inteloper.ui.activity.MonitorActivity;
import com.jiubai.inteloper.ui.iview.IDeviceView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by larry on 18/08/2017.
 */

public class DeviceTelecommandFragment extends Fragment implements IDeviceView {
    @Bind(R.id.textView_name)
    TextView mNameTextView;

    @Bind(R.id.textView_1)
    TextView mTextView1;

    @Bind(R.id.textView_2)
    TextView mTextView2;

    @Bind(R.id.textView_3)
    TextView mTextView3;

    @Bind(R.id.textView_4)
    TextView mTextView4;

    @Bind(R.id.textView_5)
    TextView mTextView5;

    @Bind(R.id.textView_6)
    TextView mTextView6;

    @Bind(R.id.textView_7)
    TextView mTextView7;

    @Bind(R.id.textView_8)
    TextView mTextView8;

    @Bind(R.id.textView_9)
    TextView mTextView9;

    @Bind(R.id.textView_10)
    TextView mTextView10;

    @Bind(R.id.textView_11)
    TextView mTextView11;

    @Bind(R.id.textView_12)
    TextView mTextView12;

    @Bind(R.id.textView_13)
    TextView mTextView13;

    @Bind(R.id.textView_time)
    TextView mTimeTextView;

    @Bind(R.id.textView_desc)
    TextView mDescTextView;

    private Device device;

    private WeakHandler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_telecommend, container, false);

        ButterKnife.bind(this, view);

        initView(view);

        return view;
    }

    private void initView(View view) {

        device = (Device) getArguments().getSerializable("device");

        mNameTextView.setText(device.getName());
        mDescTextView.setText(device.getDesc());

        if (TextUtils.isEmpty(device.getName()) || TextUtils.isEmpty(device.getDesc())) {
            return;
        }

        handler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message.what == 1) {
                    new DevicePresenterImpl(getActivity(), DeviceTelecommandFragment.this).getDeviceTelecommend(device.getName());

                    handler.sendEmptyMessageDelayed(1, 5000);
                } else if (message.what == 0) {

                }

                return false;
            }
        });

        handler.sendEmptyMessage(1);

//        new DevicePresenterImpl(getActivity(), this).getDeviceTelemetry(deviceName);
    }

    @Override
    public void onGetDeviceTelecommendResult(boolean result, String info, Object extras) {
        MonitorActivity activity = ((MonitorActivity)getActivity());

        if (activity == null) {
            return;
        }

        activity.currentInitNum++;

        if (activity.currentInitNum == activity.initNum) {
            UtilBox.dismissLoading();
        }

        if (result) {
            String time = UtilBox.getDateToString(Calendar.getInstance().getTimeInMillis(), UtilBox.YEAR_DATE_TIME);

            mTimeTextView.setText(time);

            ArrayList<Integer> values = (ArrayList<Integer>) extras;

            if (values.get(1) != 31) {
                mTextView1.setTextColor(Color.parseColor("#909090"));
                mTextView1.setBackgroundResource(R.drawable.round_text_white);
            } else if (values.get(0) == 0) {
                mTextView1.setTextColor(Color.parseColor("#ffffff"));
                mTextView1.setBackgroundResource(R.drawable.round_text_grey);
            } else if (values.get(0) == 1) {
                mTextView1.setTextColor(Color.parseColor("#ffffff"));
                mTextView1.setBackgroundResource(R.drawable.round_text_red);
            }

            if (values.get(3) != 31) {
                mTextView2.setTextColor(Color.parseColor("#909090"));
                mTextView2.setBackgroundResource(R.drawable.round_text_white);
            } else if (values.get(2) == 0) {
                mTextView2.setTextColor(Color.parseColor("#ffffff"));
                mTextView2.setBackgroundResource(R.drawable.round_text_grey);
            } else if (values.get(2) == 1) {
                mTextView2.setTextColor(Color.parseColor("#ffffff"));
                mTextView2.setBackgroundResource(R.drawable.round_text_red);
            }

            if (values.get(5) != 31) {
                mTextView3.setTextColor(Color.parseColor("#909090"));
                mTextView3.setBackgroundResource(R.drawable.round_text_white);
            } else if (values.get(4) == 0) {
                mTextView3.setTextColor(Color.parseColor("#ffffff"));
                mTextView3.setBackgroundResource(R.drawable.round_text_grey);
            } else if (values.get(4) == 1) {
                mTextView3.setTextColor(Color.parseColor("#ffffff"));
                mTextView3.setBackgroundResource(R.drawable.round_text_red);
            }

            if (values.get(7) != 31) {
                mTextView4.setTextColor(Color.parseColor("#909090"));
                mTextView4.setBackgroundResource(R.drawable.round_text_white);
            } else if (values.get(6) == 0) {
                mTextView4.setTextColor(Color.parseColor("#ffffff"));
                mTextView4.setBackgroundResource(R.drawable.round_text_grey);
            } else if (values.get(6) == 1) {
                mTextView4.setTextColor(Color.parseColor("#ffffff"));
                mTextView4.setBackgroundResource(R.drawable.round_text_red);
            }

            if (values.get(9) != 31) {
                mTextView5.setTextColor(Color.parseColor("#909090"));
                mTextView5.setBackgroundResource(R.drawable.round_text_white);
            } else if (values.get(8) == 0) {
                mTextView5.setTextColor(Color.parseColor("#ffffff"));
                mTextView5.setBackgroundResource(R.drawable.round_text_grey);
            } else if (values.get(8) == 1) {
                mTextView5.setTextColor(Color.parseColor("#ffffff"));
                mTextView5.setBackgroundResource(R.drawable.round_text_red);
            }

            if (values.get(11) != 31) {
                mTextView6.setTextColor(Color.parseColor("#909090"));
                mTextView6.setBackgroundResource(R.drawable.round_text_white);
            } else if (values.get(10) == 0) {
                mTextView6.setTextColor(Color.parseColor("#ffffff"));
                mTextView6.setBackgroundResource(R.drawable.round_text_grey);
            } else if (values.get(10) == 1) {
                mTextView6.setTextColor(Color.parseColor("#ffffff"));
                mTextView6.setBackgroundResource(R.drawable.round_text_red);
            }

            if (values.get(13) != 31) {
                mTextView7.setTextColor(Color.parseColor("#909090"));
                mTextView7.setBackgroundResource(R.drawable.round_text_white);
            } else if (values.get(12) == 0) {
                mTextView7.setTextColor(Color.parseColor("#ffffff"));
                mTextView7.setBackgroundResource(R.drawable.round_text_grey);
            } else if (values.get(12) == 1) {
                mTextView7.setTextColor(Color.parseColor("#ffffff"));
                mTextView7.setBackgroundResource(R.drawable.round_text_red);
            }

            if (values.get(15) != 31) {
                mTextView8.setTextColor(Color.parseColor("#909090"));
                mTextView8.setBackgroundResource(R.drawable.round_text_white);
            } else if (values.get(14) == 0) {
                mTextView8.setTextColor(Color.parseColor("#ffffff"));
                mTextView8.setBackgroundResource(R.drawable.round_text_grey);
            } else if (values.get(14) == 1) {
                mTextView8.setTextColor(Color.parseColor("#ffffff"));
                mTextView8.setBackgroundResource(R.drawable.round_text_red);
            }

            if (values.get(17) != 31) {
                mTextView9.setTextColor(Color.parseColor("#909090"));
                mTextView9.setBackgroundResource(R.drawable.round_text_white);
            } else if (values.get(16) == 0) {
                mTextView9.setTextColor(Color.parseColor("#ffffff"));
                mTextView9.setBackgroundResource(R.drawable.round_text_grey);
            } else if (values.get(16) == 1) {
                mTextView9.setTextColor(Color.parseColor("#ffffff"));
                mTextView9.setBackgroundResource(R.drawable.round_text_red);
            }

            if (values.get(19) != 31) {
                mTextView10.setTextColor(Color.parseColor("#909090"));
                mTextView10.setBackgroundResource(R.drawable.round_text_white);
            } else if (values.get(18) == 0) {
                mTextView10.setTextColor(Color.parseColor("#ffffff"));
                mTextView10.setBackgroundResource(R.drawable.round_text_grey);
            } else if (values.get(18) == 1) {
                mTextView10.setTextColor(Color.parseColor("#ffffff"));
                mTextView10.setBackgroundResource(R.drawable.round_text_red);
            }

            if (values.get(21) != 31) {
                mTextView11.setTextColor(Color.parseColor("#909090"));
                mTextView11.setBackgroundResource(R.drawable.round_text_white);
            } else if (values.get(20) == 0) {
                mTextView11.setTextColor(Color.parseColor("#ffffff"));
                mTextView11.setBackgroundResource(R.drawable.round_text_grey);
            } else if (values.get(20) == 1) {
                mTextView11.setTextColor(Color.parseColor("#ffffff"));
                mTextView11.setBackgroundResource(R.drawable.round_text_red);
            }

            if (values.get(23) != 31) {
                mTextView12.setTextColor(Color.parseColor("#909090"));
                mTextView12.setBackgroundResource(R.drawable.round_text_white);
            } else if (values.get(22) == 0) {
                mTextView12.setTextColor(Color.parseColor("#ffffff"));
                mTextView12.setBackgroundResource(R.drawable.round_text_grey);
            } else if (values.get(22) == 1) {
                mTextView12.setTextColor(Color.parseColor("#ffffff"));
                mTextView12.setBackgroundResource(R.drawable.round_text_red);
            }

            if (values.get(25) != 31) {
                mTextView13.setTextColor(Color.parseColor("#909090"));
                mTextView13.setBackgroundResource(R.drawable.round_text_white);
            } else if (values.get(24) == 0) {
                mTextView13.setTextColor(Color.parseColor("#ffffff"));
                mTextView13.setBackgroundResource(R.drawable.round_text_grey);
            } else if (values.get(24) == 1) {
                mTextView13.setTextColor(Color.parseColor("#ffffff"));
                mTextView13.setBackgroundResource(R.drawable.round_text_red);
            }
        } else {
            Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetDeviceTelemetryResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onDestroy() {
        if (handler != null) {
            handler.sendEmptyMessage(0);
            handler.removeMessages(1);

            handler = null;
        }

        super.onDestroy();
    }

    @Override
    public void onGetDeviceInfoResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onEditDeviceInfoResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onAddNewDeviceResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onGetDeviceListResult(boolean result, String info, Object extras) {

    }
}
