package com.example.bamaproject.sar;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bamaproject.sar.Login.Login;
import com.example.bamaproject.sar.Login.Register;
import com.example.bamaproject.sar.Main.MainActivity;
import com.example.bamaproject.sar.map.MapsActivity;

public class splashscreen extends AppCompatActivity {

    private static int splashInterval = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);



        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent i = new Intent(splashscreen.this,MainActivity.class);
                startActivity(i); // menghubungkan activity splashscren ke main activity dengan intent
                //jeda selesai Splashscreen
                finish();
            }


        }, splashInterval);

    };



}

