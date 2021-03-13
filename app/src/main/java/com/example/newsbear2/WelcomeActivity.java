package com.example.newsbear2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setTitle("Welcome");

        String bearEmoji = "\uD83D\uDC3B ";

        String buttonString = bearEmoji + " Get started!";

        Button continueButton = findViewById(R.id.getStartedButton);

        continueButton.setText(buttonString);

        continueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent welcomeIntent = new Intent(WelcomeActivity.this, LanguageActivity.class);
                startActivity(welcomeIntent);
            }
        });
    }
}
