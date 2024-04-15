//package com.example.pluginproject.util;
//
//import android.app.usage.NetworkStats;
//import android.app.usage.NetworkStatsManager;
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.Network;
//import android.net.NetworkCapabilities;
//import android.os.Build;
//import android.os.RemoteException;
//
//import androidx.annotation.RequiresApi;
//
//public class TrafficStatsHelper {
//
//    private Context context;
//    private NetworkStatsManager networkStatsManager;
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public TrafficStatsHelper(Context context) {
//        this.context = context;
//        this.networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
//    }
//
//    // 获取应用程序的总流量使用量
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public long getApplicationTotalUsage() {
//        long totalUsage = 0;
//        try {
//            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            Network network = connectivityManager.getActiveNetwork();
//            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
//            int networkType = 0;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//                networkType = networkCapabilities.getTransportInfo().getType();
//            }
//
//            NetworkStats.Bucket bucket = networkStatsManager.querySummaryForDevice(networkType, "", 0, System.currentTimeMillis());
//            totalUsage = bucket.getRxBytes() + bucket.getTxBytes();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return totalUsage;
//    }
//}
