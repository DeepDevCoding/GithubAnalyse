package com.huabing.githubanalyse.Util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 30781 on 2017/5/25.
 */

public class HttpUtil {

    //请求网络数据
    public static void sendOkHttpRequest(String address, Callback callback)
    {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }


}
