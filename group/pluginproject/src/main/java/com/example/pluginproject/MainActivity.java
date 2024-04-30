package com.example.pluginproject;

import static com.example.pluginproject.util.Constans.LOGCATINTERVALTIME;

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
    private Button btn_start;
    private Button btn_startServer;
    private Button btn_stopServer;
    private Button btn_nativeStart;
    private Button btn_runBinary;
    private Button btn_getAllProcess;
    private Button btn_stopProcess;
    private Button btn_logcat;
    private boolean isShow_net = false;
    private boolean isShow_logcat = false;
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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtils.copyAssertsFiles(MainActivity.this, "fork_engin", "fork_engin");
            }
        });
        thread.setName("xsgg_copyBinary_thread");
        thread.start();
    }

    private void initView() {
        show = findViewById(R.id.show);
        btn_start = findViewById(R.id.start);
        btn_startServer = findViewById(R.id.startServer);
        btn_stopServer = findViewById(R.id.stopServer);
        btn_nativeStart = findViewById(R.id.nativeStart);
        btn_runBinary = findViewById(R.id.runBinary);
        btn_getAllProcess = findViewById(R.id.getAllProcess);
        btn_stopProcess = findViewById(R.id.stopProcess);
        btn_logcat = findViewById(R.id.logcat);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                start();
                isShow_net = true;
                isShow_logcat = false;
                collect();
            }
        });
        btn_startServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HttpService.class);
                ContextCompat.startForegroundService(MainActivity.this, intent);
            }
        });
        btn_stopServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HttpService.class);
                stopService(intent);
            }
        });
        btn_nativeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = NativeStarter.getInstance().start();
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                show.setText(result);
                            }
                        });
                    }
                });
                thread.setName("xsgg_startNative_thread");
                thread.start();
            }
        });
        btn_runBinary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runBinary();
            }
        });
        btn_getAllProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAllProcess();
            }
        });
        btn_stopProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAllProcess();
            }
        });
        btn_logcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow_logcat = true;
                isShow_net = false;
                getLogcat();
            }
        });
    }

    private void getLogcat() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isShow_logcat) {
                    try {
                        StringBuffer sb = new StringBuffer();
                        ProcessUtil.getLogcatInfo(sb);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                show.setText(sb.toString());
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    try {
                        Thread.sleep(LOGCATINTERVALTIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        thread.setName("logcat_thread");
        thread.start();
    }

    private void stopAllProcess() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ProcessUtil.stopAllProcess(processList);
            }
        });
        thread.setName("xsgg_stopAllProcess_thread");
        thread.start();
    }

    private void checkAllProcess() {
        int parentProcessId = android.os.Process.myPid();
        allProcess(parentProcessId);
    }

    private void allProcess(int pid) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ProcessUtil.getAllProcessInfo(pid);
            }
        });
        thread.setName("xsgg_allProcess_thread");
        thread.start();
    }

    /**
     * 启动一个二进制程序
     * 二进制程序在本目录下
     */
    private void runBinary() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String fileName = MainActivity.this.getFilesDir().getAbsolutePath() + File.separator + "fork_engin";
//                ProcessUtil.runBinary(ContextUtils.getInstance().getContext(), fileName, "fork");
                ProcessUtil.runBinary(ContextUtils.getInstance().getContext(), fileName);
            }
        });
        thread.setName("xsgg_runBinary_thread");
        thread.start();
    }

    private void collect() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isShow_net) {
                    String result = DeviceInfoUtil.updateTraffic(ContextUtils.getInstance().getContext());
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            show.setText(result);
                        }
                    });
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });
        thread.setName("xsgg_collect_thread");
        thread.start();
    }

    private void start() {
        Thread thread = new Thread(new Runnable() {
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
        });
        thread.setName("xsgg_start_query_qq_thread");
        thread.start();
    }
}