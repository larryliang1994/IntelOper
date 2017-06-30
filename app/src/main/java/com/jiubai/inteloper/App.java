package com.jiubai.inteloper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jiubai.inteloper.config.Config;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;

/**
 * 程序入口
 */
public class App extends Application {
    public static SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();

        //sp = getApplicationContext().getSharedPreferences(Constants.SP_FILENAME, Context.MODE_PRIVATE);

        initService();
    }

    public void initService() {
        // 启动崩溃统计
        //CrashReport.initCrashReport(getApplicationContext(), "900016169", false);

        // 初始化网络状态
        getNetworkState();

        // 读取存储好的数据
        loadStorageData();

        // 初始化图片加载框架
        initImageLoader();
    }

    private void loadStorageData() {
        /*
        final SharedPreferences sp = getSharedPreferences(Constants.SP_FILENAME, MODE_PRIVATE);
        Config.COOKIE = sp.getString(Constants.SP_KEY_COOKIE, null);
        Config.COMPANY_NAME = sp.getString(Constants.SP_KEY_COMPANY_NAME, null);
        Config.CID = sp.getString(Constants.SP_KEY_COMPANY_ID, null);
        Config.COMPANY_BACKGROUND = sp.getString(Constants.SP_KEY_COMPANY_BACKGROUND, null);
        Config.COMPANY_CREATOR = sp.getString(Constants.SP_KEY_COMPANY_CREATOR, null);
        Config.PORTRAIT = sp.getString(Constants.SP_KEY_PORTRAIT, null);
        Config.MID = sp.getString(Constants.SP_KEY_MID, null);
        Config.NICKNAME = sp.getString(Constants.SP_KEY_NICKNAME, "昵称");
        Config.TIME = sp.getLong(Constants.SP_KEY_TIME, 0);
        if (Config.TIME == 0) {
            Config.TIME = Calendar.getInstance().getTimeInMillis();
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(Constants.SP_KEY_TIME, Config.TIME);
            editor.apply();
        }
        */
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.FIFO).build();
        L.writeLogs(false);
        ImageLoader.getInstance().init(config);

    }

    private void getNetworkState() {
        // 获取网络连接管理器对象（系统服务对象）
        ConnectivityManager cm
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // 获取网络状态
        NetworkInfo info = cm.getActiveNetworkInfo();

        Config.IS_CONNECTED = info != null && info.isAvailable();
    }
}