package com.example.pluginproject.broadcast;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.example.pluginproject.util.ContextUtils;

public class MyBroadReceiver extends BroadcastReceiver {

    private static final String ACTION = "com.example.broadcasttest.MY_BROADCAST";
    private class MyConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION.equals(action)) {
            Log.i("MyBroadReceiver", "onReceive: " + action);
            bindServicess();
        }
    }

    private void bindServicess() {
        String PKG_NAME = "com.example.pluginproject.server.HttpService";
        String ACTION = "com.xsgg.test.Service";
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.setPackage(PKG_NAME);
//        ContextUtils.getInstance().getContext().bindService(intent, );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextUtils.getInstance().getContext().startForegroundService(intent);
        }
    }
}
