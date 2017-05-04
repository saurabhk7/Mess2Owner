package com.example.saurabh.mess2owner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    Button scanbtn,testBtn;
    ListView listView;
    TextView result;
    ArrayAdapter adapter;

    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanbtn = (Button) findViewById(R.id.scanbtn);

        result = (TextView) findViewById(R.id.result);
        listView=(ListView)findViewById(R.id.ScanlistView);
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.costum_listview);
        listView.setAdapter(adapter);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }

        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if(data != null){
                final Barcode barcode = data.getParcelableExtra("barcode");
                result.post(new Runnable() {
                    @Override
                    public void run() {

                        //*****************************************************
                        result.setText(barcode.displayValue);  //barcode.displayValue (gives the String value of
                                                               //             scanned data result)
                        String text = barcode.displayValue;
                        adapter.add(text);
                        adapter.notifyDataSetChanged();


                        //TODO: send request to databse from here the User Id will be there in barcode.DisplayValue
                        //TODO: Take it in a String and call the check if user valid method 


                        //*****************************************************
                    }
                });
            }
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
