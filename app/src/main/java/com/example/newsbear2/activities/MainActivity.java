package com.example.newsbear2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.content.Intent;
import android.os.Bundle;

import com.example.newsbear2.R;

public class MainActivity extends AppCompatActivity
{
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("NewsBear");

        new Handler().postDelayed(() -> {
            Intent homeIntent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(homeIntent);
            finish();
        }, SPLASH_TIME_OUT);
    }
}