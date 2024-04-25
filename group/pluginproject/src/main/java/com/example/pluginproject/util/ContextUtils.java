package com.example.pluginproject.util;

import android.content.Context;

public class ContextUtils {

    private static ContextUtils sUtils;

    private static Context sContext;

    private ContextUtils() {
    }

    public static ContextUtils getInstance() {
        if (sUtils == null) {
            synchronized (ContextUtils.class) {
                if (sUtils == null) {
                    sUtils = new ContextUtils();
                }
            }
        }
        return sUtils;
    }

    public void setContext(Context context) {
        sContext = context;
    }

    public Context getContext() {
        return sContext;
    }

}
