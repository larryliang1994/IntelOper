package com.jiubai.inteloper.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.bean.Device;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.ui.fragment.DeviceTelecommandFragment;
import com.jiubai.inteloper.ui.fragment.DeviceTelemetryFragment;
import com.jiubai.inteloper.ui.fragment.DeviceWireFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MonitorActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.tabs)
    TabLayout mTabLayout;

    @Bind(R.id.container)
    ViewPager mViewPager;

    public int currentInitNum = 0;
    public int initNum = 2;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        ButterKnife.bind(this);

        device = (Device) getIntent().getSerializableExtra("device");

        initView();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonitorActivity.this.onBackPressed();
            }
        });

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        UtilBox.returnActivity(this);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("device", device);

            if (position == 0) {
                DeviceTelemetryFragment deviceTelemetryFragment = new DeviceTelemetryFragment();
                deviceTelemetryFragment.setArguments(bundle);

                return deviceTelemetryFragment;
            } else if (position == 1) {
                DeviceTelecommandFragment deviceTelecommandFragment = new DeviceTelecommandFragment();
                deviceTelecommandFragment.setArguments(bundle);

                return deviceTelecommandFragment;
            } else {
                DeviceWireFragment deviceWireFragment = new DeviceWireFragment();
                deviceWireFragment.setArguments(bundle);

                return deviceWireFragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return "遥测";
                case 1: return "遥信";
                case 2: return "接线";
            }
            return null;
        }
    }
}
