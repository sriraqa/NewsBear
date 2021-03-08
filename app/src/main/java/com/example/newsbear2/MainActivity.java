package com.example.newsbear2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Handler;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

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
            Intent homeIntent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(homeIntent);
            finish();
        }, SPLASH_TIME_OUT);
    }
}