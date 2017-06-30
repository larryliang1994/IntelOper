package com.jiubai.inteloper.net;

import com.jiubai.inteloper.config.Constants;

import hprose.client.HproseHttpClient;

/**
 * Created by larry on 28/06/2017.
 */

public class RequestUtil {
    private static HproseHttpClient client = null;

    public static HproseHttpClient getClient() {
        if (client == null) {
            client = new HproseHttpClient(Constants.SERVER_URL + ":" + Constants.PORT);
            client.setTimeout(10000);
        }

        return client;
    }

    private RequestUtil() {

    }
}
