package com.jiubai.inteloper.presenter;

import android.os.Handler;
import android.text.TextUtils;

import com.jiubai.inteloper.bean.Device;
import com.jiubai.inteloper.config.Config;
import com.jiubai.inteloper.ui.iview.IDeviceView;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry Liang on 03/05/2017.
 */

public class DevicePresenterImpl implements IDevicePresenter {

    private IDeviceView mIDeviceView;

    public DevicePresenterImpl(IDeviceView iDeviceView) {
        mIDeviceView = iDeviceView;
    }

    @Override
    public void GetDeviceList() {
        List<Device> list = new ArrayList<>();

        list.add(new Device("加内特", "f"));
        list.add(new Device("韦德", "f"));
        list.add(new Device("詹姆斯", "f"));
        list.add(new Device("安东尼", "a"));
        list.add(new Device("科比", "f"));
        list.add(new Device("乔丹", "f"));
        list.add(new Device("奥尼尔", "f"));
        list.add(new Device("麦格雷迪", "f"));
        list.add(new Device("艾弗森", "f"));
        list.add(new Device("哈达威", "f"));
        list.add(new Device("纳什", "f"));
        list.add(new Device("弗朗西斯", "f"));
        list.add(new Device("姚明", "f"));
        list.add(new Device("库里", "f"));
        list.add(new Device("邓肯", "f"));
        list.add(new Device("吉诺比利", "f"));
        list.add(new Device("帕克", "f"));
        list.add(new Device("杜兰特", "f"));
        list.add(new Device("韦伯", "f"));
        list.add(new Device("威斯布鲁克", "f"));
        list.add(new Device("霍华德", "f"));
        list.add(new Device("保罗", "f"));
        list.add(new Device("罗斯", "f"));
        list.add(new Device("加索尔", "f"));
        list.add(new Device("隆多", "f"));
        list.add(new Device("诺维斯基", "f"));
        list.add(new Device("格里芬", "f"));
        list.add(new Device("波什", "f"));
        list.add(new Device("伊戈达拉", "f"));

        // 配置拼音属性
        HanyuPinyinOutputFormat pyFormat = new HanyuPinyinOutputFormat();
        pyFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        pyFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        pyFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

        for (int i = 0; i < list.size(); i++) {
            Device device = list.get(i);
            list.get(i).setFirstWord(PinyinHelper.toHanyuPinyinString(device.getName(),
                    pyFormat, "").substring(0, 1));
        }

        Config.Devices = list;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mIDeviceView.onGetDeviceListResult(true, "");
            }
        }, 500);
    }
}
