package com.example.monitorapp.interf;

public interface RequestCallBack {
    void onSuccess(int code, String result);
    void onFail(int code, String msg);
}
