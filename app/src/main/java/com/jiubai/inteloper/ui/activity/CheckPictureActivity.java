package com.jiubai.inteloper.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.common.UtilBox;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 查看图片
 */
public class CheckPictureActivity extends BaseActivity {

    @Bind(R.id.vp_checkPicture)
    ViewPager mViewPager;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private ArrayList<String> pictureList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pictureList = getIntent().getStringArrayListExtra("pictureList");

        setContentView(R.layout.activity_checkpicture);

        ButterKnife.bind(this);

        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        initToolbar();

        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.setCurrentItem(0, true);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.scale_stay, R.anim.scale_stay);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            onBackPressed();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pictureList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            final RelativeLayout layout = new RelativeLayout(CheckPictureActivity.this);

            final RelativeLayout.LayoutParams photoRlp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            photoRlp.addRule(RelativeLayout.CENTER_IN_PARENT);

            final ProgressBar progressBar = new ProgressBar(CheckPictureActivity.this);
            final PhotoView photoView = new PhotoView(container.getContext());

            if (!pictureList.get(position).contains("http")) {
                progressBar.setVisibility(View.GONE);
                String imgUrl = ImageDownloader.Scheme.FILE.wrap(pictureList.get(position));
                ImageLoader.getInstance().displayImage(imgUrl, photoView);
            } else {
                ImageLoader.getInstance().displayImage(
                        pictureList.get(position),
                        photoView,
                        new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {
                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {
                                progressBar.setVisibility(View.GONE);
                                UtilBox.showSnackbar(CheckPictureActivity.this, "图片加载出错");
                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {
                            }
                        });
            }

            // 单击退出
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float v, float v1) {
                    onBackPressed();
                }
            });

            layout.addView(photoView, photoRlp);

            RelativeLayout.LayoutParams progressRlp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            progressRlp.addRule(RelativeLayout.CENTER_IN_PARENT);
            layout.addView(progressBar, progressRlp);

            container.addView(layout,
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}