package com.jiubai.inteloper.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.bean.Alarm;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.ui.activity.AlarmDetailActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by larry on 19/08/2017.
 */

public class AlarmAdapter extends RecyclerView.Adapter {
    public static final int MODE_EXPAND = 0;
    public static final int MODE_BREVIARY = 1;

    private ArrayList<Alarm> alarms;
    private Context context;

    private int mode = 0;

    public AlarmAdapter(Context context, ArrayList<Alarm> list) {
        this.context = context;
        this.alarms = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alarm, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;

        final Alarm alarm = alarms.get(position);

        if (alarm.getStatus() == Alarm.STATUS_NORMAL || alarm.getStatus() == Alarm.STATUS_BACK) {
            viewHolder.statusTextView.setTextColor(Color.parseColor("#09BB07"));
        } else {
            viewHolder.statusTextView.setTextColor(Color.parseColor("#FF3B30"));
        }

        switch (alarm.getStatus()) {
            case Alarm.STATUS_NORMAL:
                viewHolder.statusTextView.setText("正常");
                break;

            case Alarm.STATUS_UP:
                viewHolder.statusTextView.setText("越上限");
                break;

            case Alarm.STATUS_DOWN:
                viewHolder.statusTextView.setText("越下限");
                break;

            case Alarm.STATUS_UP_2:
                viewHolder.statusTextView.setText("越上限2");
                break;

            case Alarm.STATUS_DOWN_2:
                viewHolder.statusTextView.setText("越下限2");
                break;

            case Alarm.STATUS_CHANGE:
                viewHolder.statusTextView.setText("动作");
                break;

            case Alarm.STATUS_BACK:
                viewHolder.statusTextView.setText("复归");
                break;
        }

        String occurTime = alarm.getOccurTime();
        if (!occurTime.contains(":")) {
            occurTime = occurTime.substring(0, 4) + "-" + occurTime.substring(4, 6) + "-" + occurTime.substring(6, 8)
                    + "  " + occurTime.substring(8, 10) + ":" + occurTime.substring(10, 12) + ":" + occurTime.substring(12);
        }

        viewHolder.timeTextView.setText(occurTime);

        viewHolder.descTextView.setText(alarm.getWarnStr());

        if (mode == MODE_EXPAND) {
            viewHolder.descTextView.setSingleLine(false);
        } else {
            viewHolder.descTextView.setSingleLine(true);

            viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AlarmDetailActivity.class);
                    intent.putExtra("alarm", alarm);
                    UtilBox.startActivity((Activity) context, intent, false);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textView_status)
        TextView statusTextView;

        @Bind(R.id.textView_time)
        TextView timeTextView;

        @Bind(R.id.textView_desc)
        TextView descTextView;

        @Bind(R.id.layout)
        LinearLayout layout;

        public ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
