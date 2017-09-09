package com.jiubai.inteloper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.bean.DeviceListDisplay;
import com.jiubai.inteloper.widget.RippleView;

import java.util.List;

/**
 * Created by larry on 07/09/2017.
 */

public class DeviceListAdapter extends RecyclerView.Adapter {

    public final static int VIEW_INDEX = 0;
    public final static int VIEW_CONTENT = 1;

    private OnItemClickListener onItemClickListener;

    private Context mContext;
    private List<DeviceListDisplay> mList;

    public DeviceListAdapter(Context context, List<DeviceListDisplay> List) {
        this.mContext = context;
        this.mList = List;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_INDEX) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_index, parent, false);
            IndexViewHolder holder = new IndexViewHolder(view);
            holder.tvIndex = view.findViewById(R.id.tv_index);
            return holder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_device, parent, false);
            ContentViewHolder holder = new ContentViewHolder(view);
            holder.rippleView = view.findViewById(R.id.ripple_name);
            holder.tvName = view.findViewById(R.id.tv_name);
            holder.tvDesc = view.findViewById(R.id.tv_desc);
            holder.ivDivider = view.findViewById(R.id.iv_divider);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == VIEW_INDEX) {
            ((IndexViewHolder) holder).tvIndex.setText(mList.get(position).getFirstWord());
        } else {
            ((ContentViewHolder) holder).tvName.setText(mList.get(position).getName());
            ((ContentViewHolder) holder).tvDesc.setText(mList.get(position).getDesc());

            if (getItemViewType(position + 1) == VIEW_INDEX) {
                ((ContentViewHolder) holder).ivDivider.setVisibility(View.INVISIBLE);
            } else {
                ((ContentViewHolder) holder).ivDivider.setVisibility(View.VISIBLE);
            }

            ((ContentViewHolder) holder).rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position);
                    }
                }
            });
        }
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mList.size() || mList.get(position).isIndex()) {
            return VIEW_INDEX;
        } else {
            return VIEW_CONTENT;
        }
    }

    private class IndexViewHolder extends RecyclerView.ViewHolder {
        TextView tvIndex;

        IndexViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class ContentViewHolder extends RecyclerView.ViewHolder {
        RippleView rippleView;
        TextView tvName;
        TextView tvDesc;
        ImageView ivDivider;

        ContentViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
