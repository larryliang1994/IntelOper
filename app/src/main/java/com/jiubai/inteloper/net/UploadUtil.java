package com.jiubai.inteloper.net;

import android.content.Context;
import android.util.Log;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.util.Map;

/**
 * Created by Larry Liang on 25/05/2017.
 */

public class UploadUtil {

    public static void upload(final Context context, final Map<String, String> params, final String deviceName,
                                       String filePath, UploadStatusDelegate listener) {
        String url = "http://dyjk.jiubaiwang.cn" + "?";
        for (String key : params.keySet()) {
            url += key + "=" + params.get(key) + "&";
        }

        try {
            new MultipartUploadRequest(context, url)
                    .addFileToUpload(filePath, "file")
                    .addParameter("device_name", deviceName)
                    .setUtf8Charset()
                    .setMaxRetries(2)
                    .setDelegate(listener)
                    .startUpload();
        } catch (Exception exc) {
            Log.e("AndroidUploadService", exc.getMessage(), exc);
        }
    }
}
