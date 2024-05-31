package com.example.monitorapp;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monitorapp.download.DownloadUtil;
import com.example.monitorapp.util.HttpClient;

public class MainActivity extends AppCompatActivity {

    private TextView show;
    private Button start;
    private Button download;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        show = findViewById(R.id.show);
        start = findViewById(R.id.start);
        download = findViewById(R.id.download);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });
    }

    private void download() {
        DownloadUtil downloadUtil = new DownloadUtil();
        String fileName = "libfork_engin.so";
        Uri fileUri = Uri.parse("content://" + DownloadUtil.AUTHORITY + "/" + DownloadUtil.BASE_PATH + "/" + fileName);
        downloadUtil.downloadFile(this, fileUri, fileName);
    }

    private void start() {
        Uri url = Uri.parse("https://www.baidu.com");
        String result = HttpClient.getInstance().get(url.toString());
        Log.i(TAG, "start: " + result);
        if (show != null) {
            show.setText(result);
        }
    }
}
