package com.example.pluginproject.entity;

public class BaseRequest<DATA> {

    private int code;
    private DATA data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DATA getData() {
        return data;
    }

    public void setData(DATA data) {
        this.data = data;
    }
}
