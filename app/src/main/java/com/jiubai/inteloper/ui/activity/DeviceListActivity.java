package com.jiubai.inteloper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.bean.Device;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.presenter.DevicePresenterImpl;
import com.jiubai.inteloper.ui.iview.IDeviceView;
import com.jiubai.inteloper.widget.IndexBar;
import com.jiubai.inteloper.widget.RippleView;
import com.oshi.libsearchtoolbar.SearchAnimationToolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DeviceListActivity extends BaseActivity implements IDeviceView {

    @Bind(R.id.appbar)
    AppBarLayout mAppbarLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.toolbar)
    SearchAnimationToolbar mToolbar;

    @Bind(R.id.indexbar)
    IndexBar mIndexBar;

    @Bind(R.id.button_addDevice)
    Button mAddDeviceButton;

    private MainAdapter mAdapter;
    private List<Device> mList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private String filter = "";
    private String source = "";

    private List<Device> deviceList = new ArrayList<>();

    private final int REQUEST_CODE_NEW_DEVICE = 0;
    private final int REQUEST_CODE_EDIT_DEVICE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        source = getIntent().getStringExtra("source");

        ButterKnife.bind(this);

        initView();

        new DevicePresenterImpl(this, this).getDeviceList();

        UtilBox.showLoading(this);
    }

    private void initView() {
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MainAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);

        if (source.equals("monitor")) {
            mAddDeviceButton.setVisibility(View.GONE);
        } else if (source.equals("definition")) {
            mAddDeviceButton.setVisibility(View.VISIBLE);

            mAddDeviceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DeviceListActivity.this, DefinitionActivity.class);
                    DeviceListActivity.this.startActivityForResult(intent, REQUEST_CODE_NEW_DEVICE);
                }
            });
        }

        mToolbar.setSupportActionBar(this);
        mToolbar.setSearchTextColor(Color.BLACK);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_white);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setOnSearchQueryChangedListener(new SearchAnimationToolbar.OnSearchQueryChangedListener() {
            @Override
            public void onSearchCollapsed() {
                filter = "";
                initData();
            }

            @Override
            public void onSearchQueryChanged(String query) {
                filter = query;
                initData();
            }

            @Override
            public void onSearchExpanded() {
            }

            @Override
            public void onSearchSubmitted(String query) {
                UtilBox.toggleSoftInput(mToolbar, false);
            }
        });
    }

    @Override
    public void onGetDeviceListResult(boolean result, String info, Object extras) {
        UtilBox.dismissLoading();

        if (result) {
            deviceList = (List<Device>) extras;

            initIndexBar();
            initData();
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.action_search) {
            mToolbar.onSearchIconClick();
            return true;
        } else if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        boolean handledByToolbar = mToolbar.onBackPressed();

        if (!handledByToolbar) {
            super.onBackPressed();
        }
    }

    /**
     * 初始化快速索引栏
     */
    private void initIndexBar() {
        TextView tvToast = (TextView) findViewById(R.id.tv_toast);
        mIndexBar.setSelectedIndexTextView(tvToast);
        mIndexBar.setOnIndexChangedListener(new IndexBar.OnIndexChangedListener() {
            @Override
            public void onIndexChanged(String index) {
                for (int i = 0; i < mList.size(); i++) {
                    String firstWord = mList.get(i).getFirstWord();
                    if (index.compareTo(firstWord) <= 0) {
                        // 滚动列表到指定的位置
                        layoutManager.scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });
    }

    /**
     * 加载数据
     */
    @SuppressWarnings("unchecked")
    private void initData() {
        Map<String, Object> map = convertSortList(getData());
        mList.clear();
        mList.addAll((List<Device>) map.get("sortList"));
        Object[] keys = (Object[]) map.get("keys");
        String[] letters = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            letters[i] = keys[i].toString();
        }
        mIndexBar.setIndexs(letters);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 按首字母将数据排序
     *
     * @param list 需要排序的数组
     * @return 返回按首字母排序的集合（集合中插入标签项），及所有出现的首字母数组
     */
    public Map<String, Object> convertSortList(List<Device> list) {
        HashMap<String, List<Device>> map = new HashMap<>();
        for (Device item : list) {
            String firstWord;
            if (TextUtils.isEmpty(item.getFirstWord())) {
                firstWord = "#";
            } else {
                firstWord = item.getFirstWord().toUpperCase();
            }
            if (map.containsKey(firstWord)) {
                map.get(firstWord).add(item);
            } else {
                List<Device> mList = new ArrayList<>();
                mList.add(item);
                map.put(firstWord, mList);
            }
        }

        Object[] keys = map.keySet().toArray();
        Arrays.sort(keys);
        List<Device> sortList = new ArrayList<>();

        for (Object key : keys) {
            Device t = getIndexItem(key.toString());
            sortList.add(t);
            sortList.addAll(map.get(key.toString()));
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("sortList", sortList);
        resultMap.put("keys", keys);
        return resultMap;
    }

    private Device getIndexItem(String firstWord) {
        Device device = new Device();
        device.setFirstWord(firstWord);
        device.setIndex(true);
        return device;
    }

    private List<Device> getData() {
        List<Device> filterList = new ArrayList<>();

        for (int i = 0; i < deviceList.size(); i++) {
            Device device = deviceList.get(i);

            if (TextUtils.isEmpty(filter) || device.getName().contains(filter)) {
                filterList.add(device);
            }
        }

        return filterList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_NEW_DEVICE:
            case REQUEST_CODE_EDIT_DEVICE:
                if (resultCode == RESULT_OK) {
                    new DevicePresenterImpl(this, this).getDeviceList();

                    UtilBox.showLoading(this);
                }
                break;
        }
    }

    class MainAdapter extends RecyclerView.Adapter {

        public final static int VIEW_INDEX = 0;
        public final static int VIEW_CONTENT = 1;

        private Context mContext;
        private List<Device> mList;

        public MainAdapter(Context context, List<Device> List) {
            this.mContext = context;
            this.mList = List;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_INDEX) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_index, parent, false);
                IndexViewHolder holder = new IndexViewHolder(view);
                holder.tvIndex = (TextView) view.findViewById(R.id.tv_index);
                return holder;
            } else {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_device, parent, false);
                ContentViewHolder holder = new ContentViewHolder(view);
                holder.rippleView = (RippleView) view.findViewById(R.id.ripple_name);
                holder.tvName = (TextView) view.findViewById(R.id.tv_name);
                holder.ivDivider = (ImageView) view.findViewById(R.id.iv_divider);
                return holder;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (getItemViewType(position) == VIEW_INDEX) {
                ((IndexViewHolder) holder).tvIndex.setText(mList.get(position).getFirstWord());
            } else {
                ((ContentViewHolder) holder).tvName.setText(mList.get(position).getName());

                if (getItemViewType(position + 1) == VIEW_INDEX) {
                    ((ContentViewHolder) holder).ivDivider.setVisibility(View.INVISIBLE);
                } else {
                    ((ContentViewHolder) holder).ivDivider.setVisibility(View.VISIBLE);
                }

                ((ContentViewHolder) holder).rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        if (source.equals("monitor")) {
                            Intent intent = new Intent(DeviceListActivity.this, MonitorActivity.class);
                            intent.putExtra("deviceName", mList.get(position).getName());
                            UtilBox.startActivity(DeviceListActivity.this, intent, false);
                        } else if (source.equals("definition")) {
                            Intent intent = new Intent(DeviceListActivity.this, DefinitionActivity.class);
                            intent.putExtra("deviceName", mList.get(position).getName());
                            DeviceListActivity.this.startActivityForResult(intent, REQUEST_CODE_EDIT_DEVICE);
                        }

                        mToolbar.onBackPressed();
                    }
                });
            }
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
            ImageView ivDivider;

            ContentViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}


