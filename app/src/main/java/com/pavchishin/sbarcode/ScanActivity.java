package com.pavchishin.sbarcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
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
    TextView infoFild;

    EditText scannerField;
    HashSet<String> listDubl;

    String scanValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scannerField = findViewById(R.id.scan_text);
        scannerField.setOnClickListener(this);

        quantityDocs = findViewById(R.id.txt_quantity_pl);
        quantityPlace = findViewById(R.id.txt_quantity_places);
        lastPlace = findViewById(R.id.txt_ostatok);
        infoFild = findViewById(R.id.txt_signal);

        Intent intent = getIntent();
        int docQuantity = intent.getIntExtra(DOC_QUANTITY, 0);
        ArrayList<String> namesPlace = intent.getStringArrayListExtra(LIST_PLACES);
        listDubl = new HashSet<>(namesPlace);
        Log.d(TAG, docQuantity + "  ");
        Log.d(TAG, listDubl + "  ");
        quantityDocs.setText(docQuantity + "");
        quantityPlace.setText(listDubl.size() + "");
        lastPlace.setText(listDubl.size() + "");
    }

    @Override
    public void onClick(View v) {
       scanValue = String.valueOf(scannerField.getText());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scannerField.setText("");
        scannerStart(listDubl, scanValue);

    }


    private void scannerStart(HashSet<String> listDublle, String scanValue) {

        for (String name : listDublle) {
            if (name.contains(scanValue.trim().substring(0, 6))){
               int count = Integer.parseInt((String) lastPlace.getText());
               count--;
                lastPlace.setText(count + "");
                infoFild.setText(scanValue + " - " + name + " - " + scanValue.length() + " " + name.length());
                infoFild.setBackgroundColor(Color.GREEN);
            } else {
                infoFild.setText(scanValue + " - " + name + " - " + scanValue.length() + " " + name.length());
                infoFild.setBackgroundColor(Color.RED);
            }
        }
    }
}
