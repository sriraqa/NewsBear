package com.example.newsbear2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newsbear2.R;

public class LanguageActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        setTitle("Language");

        Button englishButton = findViewById(R.id.englishButton);
        Button frenchButton = findViewById(R.id.frenchButton);
        Button spanishButton = findViewById(R.id.spanishButton);

        englishButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent welcomeIntent = new Intent(LanguageActivity.this, GetStartedActivity.class);
                welcomeIntent.putExtra("com.example.newsbear2.LANGUAGE", "&languageCode=en-US");
                startActivity(welcomeIntent);
            }
        });
        frenchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent welcomeIntent = new Intent(LanguageActivity.this, GetStartedActivity.class);
                welcomeIntent.putExtra("com.example.newsbear2.LANGUAGE", "&languageCode=fr-FR");
                startActivity(welcomeIntent);
            }
        });
        spanishButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent welcomeIntent = new Intent(LanguageActivity.this, GetStartedActivity.class);
                welcomeIntent.putExtra("com.example.newsbear2.LANGUAGE", "&languageCode=es");
                startActivity(welcomeIntent);
            }
        });
    }
}
