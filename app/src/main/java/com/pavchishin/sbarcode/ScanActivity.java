package com.pavchishin.sbarcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.pavchishin.sbarcode.SecondActivity.DOC_QUANTITY;
import static com.pavchishin.sbarcode.SecondActivity.LIST_PLACES;
import static com.pavchishin.sbarcode.SecondActivity.PLACE_QUANTITY;

import static com.pavchishin.sbarcode.MainActivity.TAG;
public class ScanActivity extends AppCompatActivity implements View.OnClickListener, SoundPool.OnLoadCompleteListener {

    final int MAX_STREAMS = 5;

    SoundPool sp;
    int soundGood;
    int soundBad;


    TextView quantityDocs;
    TextView quantityPlace;
    TextView lastPlace;
    TextView infoField;

    EditText scannerField;
    HashSet<String> listDouble;
    List<Button> butList;

    ImageView imageView;
    LinearLayout showLayout;

    String scanValue;
    int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        scannerField = findViewById(R.id.scan_text);
        scannerField.setOnClickListener(this);
        scannerField.getBackground().mutate().
                setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);

        soundGood = sp.load(this, R.raw.good, 1);
        soundBad = sp.load(this, R.raw.bad, 1);


        quantityDocs = findViewById(R.id.txt_quantity_pl);
        quantityPlace = findViewById(R.id.txt_quantity_places);
        lastPlace = findViewById(R.id.txt_ostatok);
        infoField = findViewById(R.id.txt_signal);

        imageView = findViewById(R.id.image_ok_not);
        showLayout = findViewById(R.id.show_layout);

        Intent intent = getIntent();
        int docQuantity = intent.getIntExtra(DOC_QUANTITY, 0);
        ArrayList<String> namesPlace = intent.getStringArrayListExtra(LIST_PLACES);
        listDouble = new HashSet<>(namesPlace);
        quantityDocs.setText(String.valueOf(docQuantity));
        quantityPlace.setText(String.valueOf(listDouble.size()));
        lastPlace.setText(String.valueOf(listDouble.size()));

        butList = new ArrayList<>();

        showOnDisplay(listDouble);
    }

    private void showOnDisplay(HashSet<String> namesPlace) {
        for (String name : namesPlace) {
            Log.d(TAG, name);
            Button button = new Button(ScanActivity.this);
            button.setText(name);
            button.setTextSize(30);
            button.setTextColor(Color.WHITE);
            button.setBackgroundColor(Color.BLACK);
            showLayout.addView(button);
            butList.add(button);

        }
    }

    @Override
    public void onClick(View v) {
       scanValue = String.valueOf(scannerField.getText());
        count = Integer.parseInt((String) lastPlace.getText());
        try {
            Thread.sleep(200);
            scannerField.setText("");
            scannerStart(listDouble, scanValue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void scannerStart(HashSet<String> listDouble, String scanValue) {

        for (String name : listDouble) {
            if (scanValue.contains(name)){
                count--;
                lastPlace.setText(String.valueOf(count));
                infoField.setText(String.format("Штрихкод найден! %s", name));
                imageView.setImageResource(R.drawable.ok_im);
                infoField.setTextColor(Color.GREEN);
                listDouble.remove(name);
                removeFromShow(name);
                sp.play(soundGood, 1, 1, 0, 0, 1);
                break;
            } else {
                infoField.setText(String.format("Штрихкод не найден! %s", scanValue));
                infoField.setTextColor(Color.RED);
                imageView.setImageResource(R.drawable.not_ok_im);
                sp.play(soundBad, 1, 1, 0, 0, 1);
            }
        }
    }

    private void removeFromShow(String name) {
        for (Button btn : butList){
            Log.d(TAG, btn.getText().toString());
            if (name.equals(btn.getText())){
                butList.remove(btn);
                showLayout.removeView(btn);
            }
        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) { }
}
