package com.jiubai.inteloper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.adapter.DeviceListAdapter;
import com.jiubai.inteloper.bean.Device;
import com.jiubai.inteloper.bean.DeviceListDisplay;
import com.jiubai.inteloper.bean.Station;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.config.Config;
import com.jiubai.inteloper.presenter.DevicePresenterImpl;
import com.jiubai.inteloper.presenter.StationPresenterImpl;
import com.jiubai.inteloper.ui.iview.IDeviceView;
import com.jiubai.inteloper.ui.iview.IStationView;
import com.jiubai.inteloper.widget.IndexBar;
import com.oshi.libsearchtoolbar.SearchAnimationToolbar;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DeviceListActivity extends BaseActivity implements IDeviceView, IStationView {

    @Bind(R.id.appbar)
    AppBarLayout mAppbarLayout;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.toolbar)
    SearchAnimationToolbar mToolbar;

    @Bind(R.id.indexbar)
    IndexBar mIndexBar;

    @Bind(R.id.button_addStation)
    Button mAddStationButton;

    public final static String SOURCE_MONITOR = "monitor"; // 设备监视
    public final static String SOURCE_STATION = "station"; // 设备维护
    public final static String SOURCE_LIMIT = "limit"; // 限值维护

    private List<DeviceListDisplay> mList = new ArrayList<>();

    private LinearLayoutManager layoutManager;
    private String filter = "";
    private String source = "";

    private DeviceListAdapter adapter;

    private List<DeviceListDisplay> displayList = new ArrayList<>();

    private List<Device> devices = new ArrayList<>();
    private List<Station> stations = new ArrayList<>();

    private final int REQUEST_CODE_NEW_DEVICE = 0;
    private final int REQUEST_CODE_EDIT_DEVICE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        source = getIntent().getStringExtra("source");

        ButterKnife.bind(this);

        initView();

        UtilBox.showLoading(this);
    }

    private void initView() {
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        adapter = new DeviceListAdapter(this, mList);

        adapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                String name = mList.get(position).getName();

                Intent intent = null;
                switch (source) {
                    case SOURCE_MONITOR:
                        intent = new Intent(DeviceListActivity.this, MonitorActivity.class);

                        for (Device device : devices) {
                            if (name.equals(device.getName())) {
                                intent.putExtra("device", device);
                                break;
                            }
                        }

                        UtilBox.startActivity(DeviceListActivity.this, intent, false);
                        break;

                    case SOURCE_STATION:
                        intent = new Intent(DeviceListActivity.this, StationActivity.class);

                        for (Station station : stations) {
                            if (name.equals(station.getName())) {
                                intent.putExtra("station", station);
                                break;
                            }
                        }

                        startActivityForResult(intent, 333);
                        overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
                        break;

                    case SOURCE_LIMIT:
                        intent = new Intent(DeviceListActivity.this, DefinitionActivity.class);

                        for (Device device : devices) {
                            if (name.equals(device.getName())) {
                                intent.putExtra("device", device);
                                break;
                            }
                        }

                        UtilBox.startActivity(DeviceListActivity.this, intent, false);
                        break;

                    default:
                        intent = new Intent();
                        UtilBox.startActivity(DeviceListActivity.this, intent, false);
                        break;
                }

                mToolbar.onBackPressed();
            }
        });

        mRecyclerView.setAdapter(adapter);

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

        if (SOURCE_MONITOR.equals(source) || SOURCE_LIMIT.equals(source)) {
            mToolbar.setTitle("设备列表");

            mAddStationButton.setVisibility(View.GONE);

            new DevicePresenterImpl(this, this).getDeviceList();
        } else if (SOURCE_STATION.equals(source)) {
            mToolbar.setTitle("厂站列表");

            mAddStationButton.setVisibility(View.VISIBLE);

            mAddStationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(DeviceListActivity.this, StationEditActivity.class);
                    DeviceListActivity.this.startActivityForResult(intent, REQUEST_CODE_NEW_DEVICE);

                    //Intent intent = new Intent(DeviceListActivity.this, StationActivity.class);

                    //for (Station station : stations) {
                    //    if (name.equals(station.getName())) {
                    //        intent.putExtra("station", station);
                    //        break;
                    //    }
                    //}

                    //DeviceListActivity.this.startActivityForResult(intent, 333);
                    //overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
                }
            });

            new StationPresenterImpl(this, this).getStationList();
        }
    }

    @Override
    public void onGetDeviceListResult(boolean result, String info, Object extras) {
        if (result) {
            devices = (List<Device>) extras;

            displayList = new ArrayList<>();

            for (Device device: devices) {
                displayList.add(new DeviceListDisplay(device.getName(), device.getDesc()));
            }

            // 配置拼音属性
            HanyuPinyinOutputFormat pyFormat = new HanyuPinyinOutputFormat();
            pyFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            pyFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            pyFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

            for (int i = 0; i < displayList.size(); i++) {
                DeviceListDisplay display = displayList.get(i);
                displayList.get(i).setFirstWord(PinyinHelper.toHanyuPinyinString(display.getName(),
                        pyFormat, "").substring(0, 1));
            }

            initIndexBar();
            initData();

            UtilBox.dismissLoading();
        } else {
            UtilBox.dismissLoading();

            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetStationListResult(boolean result, String info, Object extras) {
        if (result) {
            stations = (List<Station>) extras;

            displayList = new ArrayList<>();

            for (Station station: stations) {
                displayList.add(new DeviceListDisplay(station.getName(),
                        station.getRegion() + "/" + station.getGroup()));
            }

            // 配置拼音属性
            HanyuPinyinOutputFormat pyFormat = new HanyuPinyinOutputFormat();
            pyFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            pyFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            pyFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

            for (int i = 0; i < displayList.size(); i++) {
                DeviceListDisplay display = displayList.get(i);
                displayList.get(i).setFirstWord(PinyinHelper.toHanyuPinyinString(display.getName(),
                        pyFormat, "").substring(0, 1));
            }

            initIndexBar();

            initData();

            UtilBox.dismissLoading();
        } else {
            UtilBox.dismissLoading();

            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
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
        mList.addAll((List<DeviceListDisplay>) map.get("sortList"));
        Object[] keys = (Object[]) map.get("keys");
        String[] letters = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            letters[i] = keys[i].toString();
        }
        mIndexBar.setIndexs(letters);

        adapter.notifyDataSetChanged();
    }

    /**
     * 按首字母将数据排序
     *
     * @param list 需要排序的数组
     * @return 返回按首字母排序的集合（集合中插入标签项），及所有出现的首字母数组
     */
    public Map<String, Object> convertSortList(List<DeviceListDisplay> list) {
        HashMap<String, List<DeviceListDisplay>> map = new HashMap<>();
        for (DeviceListDisplay item : list) {
            String firstWord;
            if (TextUtils.isEmpty(item.getFirstWord())) {
                firstWord = "#";
            } else {
                firstWord = item.getFirstWord().toUpperCase();
            }
            if (map.containsKey(firstWord)) {
                map.get(firstWord).add(item);
            } else {
                List<DeviceListDisplay> mList = new ArrayList<>();
                mList.add(item);
                map.put(firstWord, mList);
            }
        }

        Object[] keys = map.keySet().toArray();
        Arrays.sort(keys);
        List<DeviceListDisplay> sortList = new ArrayList<>();

        for (Object key : keys) {
            DeviceListDisplay t = getIndexItem(key.toString());
            sortList.add(t);
            sortList.addAll(map.get(key.toString()));
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("sortList", sortList);
        resultMap.put("keys", keys);
        return resultMap;
    }

    private DeviceListDisplay getIndexItem(String firstWord) {
        DeviceListDisplay device = new DeviceListDisplay();
        device.setFirstWord(firstWord);
        device.setIndex(true);
        return device;
    }

    private List<DeviceListDisplay> getData() {
        List<DeviceListDisplay> filterList = new ArrayList<>();

        for (int i = 0; i < displayList.size(); i++) {
            DeviceListDisplay display = displayList.get(i);

            if (TextUtils.isEmpty(filter) || display.getName().toUpperCase().contains(filter.toUpperCase())) {
                filterList.add(display);
            }
        }

        return filterList;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Config.STATION_ADD){
            //重新获取 厂站列表
            new StationPresenterImpl(this,this).getStationList();

            UtilBox.showLoading(this);

            Config.STATION_ADD = false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_NEW_DEVICE:
            case REQUEST_CODE_EDIT_DEVICE:
                if (resultCode == RESULT_OK) {
                    //重新获取 设备列表
                    new DevicePresenterImpl(this, this).getDeviceList();

                    UtilBox.showLoading(this);
                }
                break;

            case 333://编辑和新增厂站
                if (resultCode == RESULT_OK) {
                    //重新获取 厂站列表
                    new StationPresenterImpl(this, this).getStationList();

                    UtilBox.showLoading(this);
                }
                break;

        }
    }

    @Override
    public void onGetStationInfoResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onEditStationInfoResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onGetRegionListResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onGetGroupListResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onEditStationDeviceResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onGetStationDeviceListResult(boolean result, String info, Object extras) {

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
    public void onGetDeviceTelemetryResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onGetDeviceTelecommendResult(boolean result, String info, Object extras) {

    }
}


