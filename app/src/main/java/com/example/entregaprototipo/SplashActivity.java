package com.example.entregaprototipo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.entregaprototipo.LoginRegister.ActivityLogin;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Toast.makeText(SplashActivity.this, R.string.first_time_welcoming, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(SplashActivity.this, ActivityLogin.class);
        Handler handler = new Handler();

        new Thread(() -> {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            startActivity(i);

        }).start();


    }
}