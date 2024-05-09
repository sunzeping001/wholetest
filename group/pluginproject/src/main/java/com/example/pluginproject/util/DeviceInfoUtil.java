package com.example.pluginproject.util;

import android.app.ActivityManager;
import android.content.Context;
import android.net.TrafficStats;
import android.os.Debug;
import android.util.Log;

import com.example.pluginproject.MainActivity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DeviceInfoUtil {

    private static final String TAG = "DeviceInfoUtil";

    /**
     * 获取所有线程数量
     *
     * @param sb
     * @return
     */
    public static int getThreadCount(StringBuffer sb) {
        // 获取当前 Java 虚拟机中所有线程的堆栈跟踪
        Map<Thread, StackTraceElement[]> allThreads = Thread.getAllStackTraces();
        // 计算线程数量
        int threadCount = allThreads.size();
        sb.append("thread size is: " + threadCount + "\n");
        return threadCount;
    }

    /**
     * 获取所有IO读写信息
     *
     * @param sb
     */
    public static void storageRW(StringBuffer sb) {
        long totalReads = 0;
        long totalWrites = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("/proc/diskstats"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.trim().split("\\s+");

                // 获取读取和写入次数
                if (fields.length >= 14) {
                    long reads = Long.parseLong(fields[3]);
                    long writes = Long.parseLong(fields[7]);
                    totalReads += reads;
                    totalWrites += writes;
                }
            }
            sb.append("Total reads: " + totalReads + "\n");
            sb.append("Total writes: " + totalWrites + "\n");
            Log.d(TAG, "Total reads: " + totalReads);
            Log.d(TAG, "Total writes: " + totalWrites);
        } catch (IOException e) {
            Log.e(TAG, "storageRW: " + e.getLocalizedMessage());
        }
    }

    /**
     * 获取内存信息
     *
     * @param context
     * @param sb
     */
    public static void getMemoryInfo(Context context, StringBuffer sb) {
        // 获取 ActivityManager 实例
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 创建 MemoryInfo 对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        // 获取当前进程的内存信息
        activityManager.getMemoryInfo(memoryInfo);
        int pid = android.os.Process.myPid();
        // 获取指定 PID 的内存信息
        Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(new int[]{pid});
        // 获取当前应用程序的总 PSS
        int totalPss = memoryInfoArray[0].getTotalPss();

        sb.append("总内存：" + memoryInfo.totalMem + "KB" + "\n");
        sb.append("剩余内存：" + memoryInfo.availMem + "KB" + "\n");
        sb.append("当前进程使用内存：" + totalPss + "KB" + "\n");
        // 打印内存信息
        Log.i(TAG, "总内存：" + memoryInfo.totalMem);
        Log.i(TAG, "剩余内存：" + memoryInfo.availMem);
        Log.i(TAG, "当前进程使用内存：" + totalPss);
    }

//    private static long lastDownloadBytes = 0;
//    private static long lastUploadBytes = 0;

    /**
     * 更新带宽数据信息
     */
    public static String updateTraffic(Context context) {
        String result = "";
        long lastDownloadBytes = 0;
        long lastUploadBytes = 0;
        try {
            if (lastDownloadBytes == 0) {
                lastDownloadBytes = TrafficStats.getUidRxBytes(context.getApplicationInfo().uid);
            }
            if (lastUploadBytes == 0) {
                lastUploadBytes = TrafficStats.getUidTxBytes(context.getApplicationInfo().uid);
            }
            Thread.sleep(1000);
            long newDownloadSpeed = TrafficStats.getUidRxBytes(context.getApplicationInfo().uid);
            long newUploadSpeed = TrafficStats.getUidTxBytes(context.getApplicationInfo().uid);

            long diffDownloadBytes = newDownloadSpeed - lastDownloadBytes;
            long diffUploadBytes = newUploadSpeed - lastUploadBytes;

            StringBuffer sb = new StringBuffer();
            // 计算带宽
//            double dl = diffDownloadBytes * 8.0 / 1024 / 1024;
//            double upl = diffUploadBytes * 8.0 / 1024 / 1024;
            // 计算网速
            double dl = diffDownloadBytes *1.0 / 1024;
            double upl = diffUploadBytes * 1.0 / 1024;
            String diffDL = String.format("%.4f", dl);
            String diffUPL = String.format("%.4f", upl);
            sb.append("Download speed: " + diffDL + " Kbps\n");
            sb.append("Upload speed: " + diffUPL + " kbps\n");

            getMemoryInfo(context, sb);
            getThreadCount(sb);
//            storageRW(sb);
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
