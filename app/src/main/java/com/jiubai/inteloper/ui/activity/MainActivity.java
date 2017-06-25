package com.jiubai.inteloper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.ui.fragment.HomeFragment;
import com.jiubai.inteloper.ui.fragment.SearchFragment;
import com.jiubai.inteloper.ui.fragment.UserInfoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    @Bind(R.id.navigation)
    BottomNavigationView mNavigation;

    private HomeFragment mHomeFragment = new HomeFragment();
    private SearchFragment mSearchFragment = new SearchFragment();
    private UserInfoFragment mUserInfoFragment = new UserInfoFragment();

    private ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initViewPager();
    }

    private void initViewPager() {
        mFragments = new ArrayList<>();

        mFragments.add(mHomeFragment);
        mFragments.add(mSearchFragment);
        mFragments.add(mUserInfoFragment);

        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments));

        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: mNavigation.setSelectedItemId(R.id.navigation_home);    break;
                    case 1: mNavigation.setSelectedItemId(R.id.navigation_search);    break;
                    case 2: mNavigation.setSelectedItemId(R.id.navigation_userinfo);    break;
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
                case R.id.navigation_search:
                    mViewPager.setCurrentItem(1, true);
                    return true;
                case R.id.navigation_userinfo:
                    mViewPager.setCurrentItem(2, true);
                    return true;
            }
            return false;
        }

    };

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
