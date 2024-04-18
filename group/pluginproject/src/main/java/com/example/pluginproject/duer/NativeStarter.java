package com.example.pluginproject.duer;

public class NativeStarter {

    static {
        System.loadLibrary("duer");
    }

    private static NativeStarter starter;

    private NativeStarter() {}

    public static NativeStarter getInstance() {
        if (starter == null) {
            synchronized (NativeStarter.class) {
                if (starter == null) {
                    starter = new NativeStarter();
                }
            }
        }
        return starter;
    }

    public native String start();

}
