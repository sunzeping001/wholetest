package com.example.pluginproject.interf;

public interface RequestCallBack {
    void onSuccess(int code, String result);
    void onFail(int code, String msg);
}
