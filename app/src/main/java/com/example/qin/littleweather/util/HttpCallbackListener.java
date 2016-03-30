package com.example.qin.littleweather.util;

/**
 * Created by qin on 2016/3/30.
 */
public interface HttpCallbackListener {
    void onFinish(String responce);
    void onError(Exception e);
}
