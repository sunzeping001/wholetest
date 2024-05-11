package com.example.pluginproject;

import static com.example.pluginproject.util.Constans.LOGCATINTERVALTIME;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.pluginproject.duer.NativeStarter;
import com.example.pluginproject.server.HttpService;
import com.example.pluginproject.util.Constans;
import com.example.pluginproject.util.ContextUtils;
import com.example.pluginproject.util.DeviceInfoUtil;
import com.example.pluginproject.util.FileUtils;
import com.example.pluginproject.util.HttpClient;
import com.example.pluginproject.util.ProcessUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView showNet;
    private TextView showLog;
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
    private StringBuffer info = new StringBuffer();
    // 防止内存泄露，需要释放掉
//    List<Disposable> disposableList = new ArrayList<>();
    Map<String, Disposable> disposableMap = new HashMap<>();
    private static final String COPY_FUNC = "copy";
    private static final String COLLECT_FUNC = "collect";
    private static final String LOG_FUNC = "log";
    private static final String NATIVE_FUNC = "native";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        copyBinary();
        startCollect();
    }

    private void startCollect() {
        isShow_net = true;
        isShow_logcat = false;
        collect();
        getLogcat();
    }

    private void copyBinary() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                FileUtils.copyAssertsFiles(MainActivity.this, "fork_engin", "fork_engin");
                e.onNext("success");
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                if (d != null) {
//                    disposableList.add(d);
                    disposableMap.put(COPY_FUNC, d);
                }
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "copy binary file " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "copy binary file " + e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void initView() {
        showNet = findViewById(R.id.showNet);
        showLog = findViewById(R.id.showLog);
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
                Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> e) throws Exception {
                                String result = NativeStarter.getInstance().start(ContextUtils.getInstance().getContext().getCacheDir().getAbsolutePath());
                                e.onNext(result);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposableMap.put(NATIVE_FUNC, d);
                            }

                            @Override
                            public void onNext(String s) {
                                showLog.setText(s);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String result = NativeStarter.getInstance().start();
//                        MainActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
////                                show.setText(result);
//                            }
//                        });
//                    }
//                });
//                thread.setName("xsgg_startNative_thread");
//                thread.start();
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
        Observable.interval(0, LOGCATINTERVALTIME, TimeUnit.MILLISECONDS)
                .map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Exception {
                        StringBuffer sb = new StringBuffer();
                        try {
                            ProcessUtil.getLogcatInfo(sb);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return sb.toString();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
//                        disposableList.add(d);
                        disposableMap.put(LOG_FUNC, d);
                    }

                    @Override
                    public void onNext(String s) {
                        showLog.setText(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "collect error: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void stopAllProcess() {
        Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        ProcessUtil.stopAllProcess(processList);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                    }
                });
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ProcessUtil.stopAllProcess(processList);
//            }
//        });
//        thread.setName("xsgg_stopAllProcess_thread");
//        thread.start();
    }

    private void checkAllProcess() {
        int parentProcessId = android.os.Process.myPid();
        allProcess(parentProcessId);
    }

    private void allProcess(int pid) {
        Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        ProcessUtil.getAllProcessInfo(pid);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                    }
                });
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ProcessUtil.getAllProcessInfo(pid);
//            }
//        });
//        thread.setName("xsgg_allProcess_thread");
//        thread.start();
    }

    /**
     * 启动一个二进制程序
     * 二进制程序在本目录下
     */
    private void runBinary() {
        Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        String fileName = MainActivity.this.getFilesDir().getAbsolutePath() + File.separator +
                                "fork_engin";
//                ProcessUtil.runBinary(ContextUtils.getInstance().getContext(), fileName, "fork");
                        ProcessUtil.runBinary(ContextUtils.getInstance().getContext(), fileName);
                        e.onNext("success");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "run binary " + s);
                    }
                });
    }

    private void collect() {
        Observable.interval(0, Constans.NETCOLLECTINTERVALTIME, TimeUnit.MILLISECONDS)
                .map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Exception {
                        String result = DeviceInfoUtil.updateTraffic(ContextUtils.getInstance().getContext());
                        return result;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableMap.put(COLLECT_FUNC, d);
                    }

                    @Override
                    public void onNext(String s) {
                        showNet.setText(s);
                        if (!isShow_net) {
                            Log.i(TAG, "collect stop ");
                            disposableMap.get(COLLECT_FUNC).dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "collect error: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposableMap.size() > 0) {
            Iterator<Map.Entry<String, Disposable>> it = disposableMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Disposable> entry = it.next();
                String key = entry.getKey();
                Disposable disposable = entry.getValue();
                disposable.dispose();
            }
            disposableMap.clear();
        }
    }
}