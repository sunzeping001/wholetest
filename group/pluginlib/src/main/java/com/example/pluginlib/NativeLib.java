package com.example.pluginlib;

public class NativeLib {

    // Used to load the 'pluginlib' library on application startup.
    static {
        System.loadLibrary("pluginlib");
    }

    /**
     * A native method that is implemented by the 'pluginlib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}