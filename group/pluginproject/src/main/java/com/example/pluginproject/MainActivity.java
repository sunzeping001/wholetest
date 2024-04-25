package com.example.pluginproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.pluginproject.duer.NativeStarter;
import com.example.pluginproject.server.HttpService;
import com.example.pluginproject.util.ContextUtils;
import com.example.pluginproject.util.DeviceInfoUtil;
import com.example.pluginproject.util.FileUtils;
import com.example.pluginproject.util.HttpClient;
import com.example.pluginproject.util.ProcessUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView show;
    private Button start;
    private Button startServer;
    private Button stopServer;
    private Button nativeStart;
    private Button runBinary;
    private Button getAllProcess;
    private Button stopProcess;
    private List<Integer> processList = new ArrayList<>();

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        copyBinary();
    }

    private void copyBinary() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtils.copyAssertsFiles(MainActivity.this, "fork_engin", "fork_engin");
            }
        }).start();
    }

    private void initView() {
        show = findViewById(R.id.show);
        start = findViewById(R.id.start);
        startServer = findViewById(R.id.startServer);
        stopServer = findViewById(R.id.stopServer);
        nativeStart = findViewById(R.id.nativeStart);
        runBinary = findViewById(R.id.runBinary);
        getAllProcess = findViewById(R.id.getAllProcess);
        stopProcess = findViewById(R.id.stopProcess);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                start();
                collect();
            }
        });
        startServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HttpService.class);
                ContextCompat.startForegroundService(MainActivity.this, intent);
            }
        });
        stopServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HttpService.class);
                stopService(intent);
            }
        });
        nativeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer result = new StringBuffer();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        result.append(NativeStarter.getInstance().start());
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                show.setText(result);
                            }
                        });
                    }
                }).start();
            }
        });
        runBinary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runBinary();
            }
        });
        getAllProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAllProcess();
            }
        });
        stopProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAllProcess();
            }
        });
    }

    private void stopAllProcess() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ProcessUtil.stopAllProcess(processList);
            }
        }).start();
    }

    private void checkAllProcess() {
        int parentProcessId = android.os.Process.myPid();
        allProcess(parentProcessId);
    }

    private void allProcess(int pid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ProcessUtil.getAllProcessInfo(pid, true);
            }
        }).start();
    }

    /**
     * 启动一个二进制程序
     * 二进制程序在本目录下
     */
    private void runBinary() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String fileName = MainActivity.this.getFilesDir().getAbsolutePath() + File.separator + "fork_engin";
                ProcessUtil.runBinary(ContextUtils.getInstance().getContext(), fileName);
            }
        }).start();
    }

    private void collect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    String result = DeviceInfoUtil.updateTraffic(ContextUtils.getInstance().getContext());
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            show.setText(result);
                        }
                    });
                }
            }
        }).start();
    }

    private void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Uri url = Uri.parse("https://www.qq.com");
                        String result = HttpClient.getInstance().get(url.toString());
//                        Log.i(TAG, "start: " + result);
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