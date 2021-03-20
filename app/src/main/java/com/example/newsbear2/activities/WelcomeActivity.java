package com.example.newsbear2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newsbear2.R;

public class WelcomeActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setTitle("Welcome");

        Button continueButton = findViewById(R.id.continueButton);

        continueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent welcomeIntent = new Intent(WelcomeActivity.this, LanguageActivity.class);
                startActivity(welcomeIntent);
            }
        });

        Button skipButton = findViewById(R.id.skipButton);

        skipButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent getStartedIntent = new Intent(WelcomeActivity.this, SearchActivity.class);
                String language = "&languageCode=en-US";
                getStartedIntent.putExtra("com.example.newsbear2.LANGUAGE2", language);
                startActivity(getStartedIntent);
            }
        });
    }
}
