package com.example.monitorapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.monitorapp.util.HttpClient;

class MainActivity extends Activity {

    private TextView show;
    private Button start;
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
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
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
