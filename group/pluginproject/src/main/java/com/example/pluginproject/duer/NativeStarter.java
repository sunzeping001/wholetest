package com.example.pluginproject.duer;

import com.example.pluginproject.util.ContextUtils;

import java.io.File;

public class NativeStarter {

    private static final String[] LIBS = {
            "liblive.so",
            "libfork_engin.so",
            "libc++_shared.so",
            "libbtvdplive.so",
            "libduer.so"
    };

    static {
//        System.loadLibrary("duer");
    }

    private static NativeStarter starter;

    private NativeStarter() {
        String libPath = ContextUtils.getInstance().getContext().getFilesDir().getAbsolutePath();
        for (String libName : LIBS) {
            try {
                System.load(libPath + File.separator + libName);
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

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

    public native String start(String logPath);

}
