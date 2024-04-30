package com.example.pluginproject.server;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.pluginproject.R;
import com.example.pluginproject.duer.NativeStarter;
import com.example.pluginproject.util.HttpClient;

public class HttpService extends Service {

    private static final String CHANNEL_ID = "119120";
    private static final CharSequence CHANNEL_NAME = "xsgg测试";
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    private static final String TAG = "HttpService";
    private boolean isRunning = false;
    private Runnable httpCall = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "xsgg---server start---");
            while (isRunning) {
                Log.i(TAG, "xsgg---server running---");
                try {
                    Uri url = Uri.parse("https://qq.com");
                    String result = HttpClient.getInstance().get(url.toString());
                    Log.i(TAG, "start: " + result);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Runnable callNative = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "xsgg---server start---");
            while (true) {
                Log.i(TAG, "xsgg---server start while---");
                try {
                    NativeStarter.getInstance().start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    };

    private void doWork() {
        isRunning = true;
//        new Thread(httpCall).start();
        new Thread(callNative).start();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("服务启动了哈")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setAutoCancel(false);
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, notificationBuilder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        doWork();
//        startForeground(NOTIFICATION_ID, notificationBuilder.build());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(NOTIFICATION_ID);
        isRunning = false;
        Log.i(TAG, "xsgg---server stop");
        System.exit(0);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
    }
}