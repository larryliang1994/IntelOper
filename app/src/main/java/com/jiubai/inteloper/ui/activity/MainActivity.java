package com.jiubai.inteloper.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.jiubai.inteloper.R;
import com.jiubai.inteloper.manager.AlarmManager;
import com.jiubai.inteloper.ui.fragment.HomeFragment;
import com.jiubai.inteloper.ui.fragment.UserInfoFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    @Bind(R.id.navigation)
    BottomNavigationViewEx mNavigation;

    private HomeFragment mHomeFragment = new HomeFragment();
    //private SearchFragment mSearchFragment = new SearchFragment();
    private UserInfoFragment mUserInfoFragment = new UserInfoFragment();

    private ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initView();

        initAlarm();
    }

    private void initAlarm() {
        // 开始监听实时告警
        AlarmManager alarmManager = AlarmManager.getInstance();
        alarmManager.startListen();
    }

    private void initView() {
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mNavigation.enableAnimation(false);
        mNavigation.enableItemShiftingMode(false);
        mNavigation.enableShiftingMode(false);

        initViewPager();
    }

    private void initViewPager() {
        mFragments = new ArrayList<>();

        mFragments.add(mHomeFragment);
        //mFragments.add(mSearchFragment);
        mFragments.add(mUserInfoFragment);

        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments));

        mViewPager.setOffscreenPageLimit(10);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: mNavigation.setSelectedItemId(R.id.navigation_home);    break;
                    //case 1: mNavigation.setSelectedItemId(R.id.navigation_search);    break;
                    case 1: mNavigation.setSelectedItemId(R.id.navigation_userinfo);    break;
                }
                mNavigation.setSelectedItemId(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0, true);
                    return true;
//                case R.id.navigation_search:
//                    mViewPager.setCurrentItem(1, true);
//                    return true;
                case R.id.navigation_userinfo:
                    mViewPager.setCurrentItem(1, true);
                    return true;
            }
            return false;
        }

    };

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> list;

        MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }
    }
}
