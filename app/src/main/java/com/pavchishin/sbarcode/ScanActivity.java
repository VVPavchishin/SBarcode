package com.pavchishin.sbarcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

import static com.pavchishin.sbarcode.SecondActivity.DOC_QUANTITY;
import static com.pavchishin.sbarcode.SecondActivity.LIST_PLACES;
import static com.pavchishin.sbarcode.SecondActivity.PLACE_QUANTITY;

import static com.pavchishin.sbarcode.MainActivity.TAG;
public class ScanActivity extends AppCompatActivity implements View.OnClickListener{



    TextView quantityDocs;
    TextView quantityPlace;
    TextView lastPlace;
    TextView infoField;

    EditText scannerField;
    HashSet<String> listDuble;

    String scanValue;
    int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scannerField = findViewById(R.id.scan_text);
        scannerField.setOnClickListener(this);
        scannerField.getBackground().mutate().
                setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        quantityDocs = findViewById(R.id.txt_quantity_pl);
        quantityPlace = findViewById(R.id.txt_quantity_places);
        lastPlace = findViewById(R.id.txt_ostatok);
        infoField = findViewById(R.id.txt_signal);

        Intent intent = getIntent();
        int docQuantity = intent.getIntExtra(DOC_QUANTITY, 0);
        ArrayList<String> namesPlace = intent.getStringArrayListExtra(LIST_PLACES);
        listDuble = new HashSet<>(namesPlace);
        Log.d(TAG, docQuantity + "  ");
        Log.d(TAG, listDuble + "  ");
        quantityDocs.setText(docQuantity + "");
        quantityPlace.setText(listDuble.size() + "");
        lastPlace.setText(listDuble.size() + "");
    }

    @Override
    public void onClick(View v) {
       scanValue = String.valueOf(scannerField.getText()).trim().substring(0, 8);
        count = Integer.parseInt((String) lastPlace.getText());
        try {
            Thread.sleep(200);
            scannerField.setText("");
            scannerStart(listDuble, scanValue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    private void scannerStart(HashSet<String> listDublle, String scanValue) {

        for (String name : listDublle) {
            if (name.equals(scanValue)){
                count--;
                lastPlace.setText(count + "");
                infoField.setText("Штрихкод найден! " + name);
                infoField.setTextColor(Color.GREEN);
                listDublle.remove(name);
                break;
            } else {
                infoField.setText("Штрихкод не найден!");
                infoField.setTextColor(Color.RED);
            }
        }
    }
}
