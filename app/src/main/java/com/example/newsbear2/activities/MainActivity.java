package com.example.newsbear2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.content.Intent;
import android.os.Bundle;

import com.example.newsbear2.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
{
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("NewsBear");

        //displays splash screen for 3 seconds
        new Handler().postDelayed(() ->
        {
            //only displays the welcome the first time user opens the app
            if(readString(this, "startedWelcome") == null)
            {
                writeString(this, "startedWelcome", "true");
                Intent homeIntent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(homeIntent);
            }
            else
            {
                Intent homeIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(homeIntent);
            }
            finish();
        }, SPLASH_TIME_OUT);
    }

    //stores String in app storage
    public static void writeString(Context context, final String KEY, String property)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences("Start Welcome", context.MODE_PRIVATE).edit();
        editor.putString(KEY, property);
        editor.apply();
    }

    //reads String from app storage
    public static String readString(Context context, final String KEY)
    {
        return context.getSharedPreferences("Start Welcome", context.MODE_PRIVATE).getString(KEY, null);
    }
}