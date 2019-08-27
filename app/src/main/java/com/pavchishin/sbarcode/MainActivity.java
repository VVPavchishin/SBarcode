package com.pavchishin.sbarcode;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final static String FILE_DIR = "WorkFiles";
    final static String TAG = "-----";
    final static String FILES_ARRAY = "ArrayFiles";

    private Button startButton;
    private ProgressBar progressBar;
    Handler handler;

    List<String> fileNames;
    File[] files;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        startButton = findViewById(R.id.button_start);
        startButton.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        fileNames = new ArrayList<>();

        handler = new Handler() {
            public void handleMessage(Message msg) {
                progressBar.setMax(files.length);
                progressBar.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    progressBar.setProgressTintList(ColorStateList.valueOf(Color.WHITE));
                }
                progressBar.setProgress(msg.what);
                if (msg.what == files.length){
                    goToSecondPage(files);
                    startButton.setEnabled(true);
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        startButton.setEnabled(false);
        loadStatus();
    }

    public void downloadFile() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadStatus() {
        File workDirPath = new File(Environment.getExternalStorageDirectory() + File.separator + FILE_DIR);

        if (workDirPath.exists()) {
            files = listFiles(workDirPath).toArray(new File[0]);
            Thread t = new Thread(() -> {
                for (int i = 1; i <= files.length; i++) {
                    downloadFile();
                    handler.sendEmptyMessage(i);
                }
            });
            t.start();

        } else {
            Log.d(TAG, "Folder not found!");
        }
    }
    private void goToSecondPage(File[] files) {
        int length = files.length;
        Log.d(TAG, length + "");
        for (File file : files){
            fileNames.add(file.getName());
        }
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putStringArrayListExtra(FILES_ARRAY, (ArrayList<String>) fileNames);
        startActivity(intent);
    }
    public ArrayList<File> listFiles(File dir) {
        ArrayList<File> files = new ArrayList<>();
        for (File file : dir.listFiles()) {
            if (file.isDirectory())
                files.addAll(listFiles(file));
            else
                files.add(file);
        }
        return files;
    }

}
