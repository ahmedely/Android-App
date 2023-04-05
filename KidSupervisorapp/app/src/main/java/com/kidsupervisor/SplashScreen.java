package com.kidsupervisor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    Pref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        pref = new Pref(this);
        pref.setPrevFragment(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                    Intent intent=new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

            }
        },2000);
    }
}