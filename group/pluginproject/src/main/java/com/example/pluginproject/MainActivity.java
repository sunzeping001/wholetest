package com.example.pluginproject;

import android.app.ActivityManager;
import android.content.Context;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.StatFs;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pluginproject.util.HttpClient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView show;
    private Button start;
    private static final String TAG = "MainActivity";

    private int getThreadCount(StringBuffer sb) {
        // 获取当前 Java 虚拟机中所有线程的堆栈跟踪
        Map<Thread, StackTraceElement[]> allThreads = Thread.getAllStackTraces();
        // 计算线程数量
        int threadCount = allThreads.size();
        sb.append("thread size is: " + threadCount + "\n");
        return threadCount;
    }

    private void storageRW(StringBuffer sb) {
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
            Log.i(TAG, "Total reads: " + totalReads);
            Log.i(TAG, "Total writes: " + totalWrites);
        } catch (IOException e) {
            Log.e(TAG, "storageRW: " + e.getLocalizedMessage());
        }
    }

    private void printMemoryInfo(Context context, StringBuffer sb) {
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

    private void updateTraffic() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                long lastDownloadBytes = 0;
                long lastUploadBytes = 0;
                while (true) {
                    try {
//                        long totalRxBytes = TrafficStats.getTotalRxBytes();
//                        long totalTxBytes = TrafficStats.getTotalTxBytes();
                        if (lastDownloadBytes == 0) {
                            lastDownloadBytes = TrafficStats.getUidRxBytes(getApplicationInfo().uid);
                        }
                        if (lastUploadBytes == 0) {
                            lastUploadBytes = TrafficStats.getUidTxBytes(getApplicationInfo().uid);
                        }
                        Thread.sleep(1000);
                        long newDownloadSpeed = TrafficStats.getUidRxBytes(getApplicationInfo().uid);
                        long newUploadSpeed = TrafficStats.getUidTxBytes(getApplicationInfo().uid);

                        long diffDownloadBytes = newDownloadSpeed - lastDownloadBytes;
                        long diffUploadBytes = newUploadSpeed - lastUploadBytes;

                        lastDownloadBytes = newDownloadSpeed;
                        lastUploadBytes = newUploadSpeed;

                        StringBuffer sb = new StringBuffer();
                        double dl = diffDownloadBytes * 8.0 / 1024 / 1024;
                        double upl = diffUploadBytes * 8.0 / 1024 / 1024;
                        String diffDL = String.format("%.4f", dl);
                        String diffUPL = String.format("%.4f", upl);
                        sb.append("Download speed: " + diffDL + " Mbps\n");
                        sb.append("Upload speed: " + diffUPL + " Mbps\n");

                        printMemoryInfo(MainActivity.this, sb);
                        getThreadCount(sb);
                        storageRW(sb);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                show.setText(sb.toString());
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        show = findViewById(R.id.show);
        start = findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
                collect();
            }
        });
    }

    private void collect() {
        updateTraffic();
    }

    private void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Uri url = Uri.parse("https://qq.com");
                        String result = HttpClient.getInstance().get(url.toString());
                        Log.i(TAG, "start: " + result);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
//                MainActivity.this.runOnUiThread(new Runnable() {
//                    @RequiresApi(api = Build.VERSION_CODES.N)
//                    @Override
//                    public void run() {
//                        if (show != null) {
//                            Spanned spannedHtml = Html.fromHtml(result, Html.FROM_HTML_MODE_LEGACY);
//                            show.setText(spannedHtml);
//                        }
//                    }
//                });
            }
        }).start();
    }
}