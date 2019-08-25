package com.pavchishin.sbarcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.pavchishin.sbarcode.MainActivity.FILES_ARRAY;
import static com.pavchishin.sbarcode.MainActivity.FILE_DIR;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    final static String DOC_QUANTITY = "QuantityDoc";
    final static String PLACE_QUANTITY = "QuantityPlace";
    final static String LIST_PLACES = "NamePlace";

    Button btnShow;
    Button btnScan;
    LinearLayout buttonLayout;
    LinearLayout placeLayout;

    ArrayList<String> listFileNames;
    ArrayList<String> listPlaceNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        btnShow = findViewById(R.id.button_show);
        btnShow.setOnClickListener(this);

        btnScan = findViewById(R.id.button_scan);
        btnScan.setOnClickListener(this);
        btnScan.setVisibility(View.INVISIBLE);

        buttonLayout = findViewById(R.id.layout_button);
        placeLayout = findViewById(R.id.layout_places);

        Intent intent = getIntent();
        listFileNames = intent.getStringArrayListExtra(FILES_ARRAY);

        listPlaceNames = new ArrayList<>();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_show:
                for (String files : listFileNames) {
                    readFromSD(files);
                    btnShow.setVisibility(View.INVISIBLE);
                    btnScan.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.button_scan:
                Intent intent = new Intent(SecondActivity.this, ScanActivity.class);
                int numDocs = buttonLayout.getChildCount();
                //int numPlace = placeLayout.getChildCount();
                intent.putExtra(DOC_QUANTITY, numDocs);
                //intent.putExtra(PLACE_QUANTITY, numPlace);
                intent.putStringArrayListExtra(LIST_PLACES, listPlaceNames);
                startActivity(intent);
                break;
        }
    }

    private void readFromSD(String fileName) {
        new Thread(() -> {
            Set<String> placeList = new HashSet<>();
            try {
                File workDirPath = new File(Environment.getExternalStorageDirectory() + File.separator + FILE_DIR);
                InputStream stream = new FileInputStream(workDirPath.toString() + File.separator + fileName);
                XSSFWorkbook workbook = new XSSFWorkbook(stream);
                XSSFSheet sheet = workbook.getSheetAt(0);
                XSSFRow row;
                String cellValue;

                for (int rowIndex = 19; rowIndex < sheet.getLastRowNum(); rowIndex++){
                    row = sheet.getRow(rowIndex);
                    if (row != null) {
                        Cell cell = row.getCell(7);
                        cellValue = cell.getStringCellValue();
                        if (cellValue != null &&  cellValue.length() != 0) {
                            placeList.add(cellValue);
                            //Log.d("tag", cellValue);
                        }
                    }
                }

                for (String list : placeList) {
                    runOnUiThread(() -> {
                        if(!listPlaceNames.contains(list)) {
                            listPlaceNames.add(list);
                            Button button = new Button(SecondActivity.this);
                            button.setText(list);
                            placeLayout.addView(button);
                        }

                    });
                }

                String val = String.valueOf(workbook.getSheetAt(0).getRow(15).getCell(4));
                //Log.d("tag", val);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Button button = new Button(SecondActivity.this);
                        button.setText(val);
                        buttonLayout.addView(button);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }
}
