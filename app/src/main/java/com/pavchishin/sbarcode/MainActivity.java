package com.pavchishin.sbarcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final static String FILE_DIR = "WorkFiles";
    final static String TAG = "-----";
    final static String FILES_ARRAY = "ArrayFiles";

    private Button startButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        startButton = findViewById(R.id.button_start);
        startButton.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        loadStatus();
    }

    private void loadStatus() {
        Log.d(TAG, "Load status start!");
        File workDirPath = new File(Environment.getExternalStorageDirectory() + File.separator + FILE_DIR);
        Log.d(TAG, Environment.getExternalStorageDirectory() + File.separator + FILE_DIR);
        if (workDirPath.exists()) {
            Log.d(TAG, "Folder exist! ");
            File[] files = listFiles(workDirPath).toArray(new File[0]);
                goToSecondPage(files);

        } else {
            Log.d(TAG, "Folder not found!");
        }
    }

    private void goToSecondPage(File[] files) {
        List<String> fileNames = new ArrayList<>();
        int length = files.length;
        Log.d(TAG, length + "");
        for (File file : files){
            fileNames.add(file.getName());
        }
        showProgressBar(length);

        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putStringArrayListExtra(FILES_ARRAY, (ArrayList<String>) fileNames);
        startActivity(intent);
    }

    private void showProgressBar(int length) {

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
