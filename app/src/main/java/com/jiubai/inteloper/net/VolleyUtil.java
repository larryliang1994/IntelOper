package com.jiubai.inteloper.net;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jiubai.inteloper.config.Config;
import com.jiubai.inteloper.config.Constants;
import com.jiubai.inteloper.config.Urls;

import java.util.HashMap;
import java.util.Map;

/**
 * Volley框架
 */
public class VolleyUtil {
    public static RequestQueue requestQueue = null;

    public static void initRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    /**
     * 进行带有Cookie的网络请求
     *
     * @param url             请求参数
     * @param key             请求参数的键
     * @param value           请求参数的值
     * @param successCallback 通信成功回调
     * @param errorCallback   通信失败回调
     */
    public static void requestWithCookie(final String url, final String[] key, final String[] value,
                                         Response.Listener<String> successCallback,
                                         Response.ErrorListener errorCallback) {
        // 构建Post请求对象
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Urls.SERVER_URL + "/ajax.php?a=" + url,
                successCallback, errorCallback) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Cookie", "memberCookie=" + Config.COOKIE);

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (key != null) {
                    Map<String, String> params = new HashMap<>();
                    for (int i = 0; i < key.length; i++) {
                        params.put(key[i], value[i]);
                    }
                    return params;
                } else {
                    return super.getParams();
                }
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.REQUEST_TIMEOUT, 1, 1.0f));

        // 加入请求队列
        requestQueue.add(stringRequest);
    }
}