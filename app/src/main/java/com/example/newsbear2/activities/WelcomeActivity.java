package com.example.newsbear2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

        //explanation of the app displayed on screen
        String description = "There can be hundreds of pieces of gossip that one person can encounter in a day. " +
                "With that said, it is almost impossible to do a deep fact check on every single fact that you hear. " +
                "So, NewsBear seeks to simplify this process by providing a simple search engine for fact checking news.";
        TextView tv = findViewById(R.id.textView2);
        tv.setText(Html.fromHtml(description));

        //description of the app displayed on screen
        String subtitle = "This app provides a simple way for young adults to fact check and easily find news!";
        TextView subtitleView = findViewById(R.id.textView3);
        subtitleView.setText(subtitle);

        //button listens for click and continues to info screen
        Button continueButton = findViewById(R.id.continueButton2);
        continueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent getStartedIntent = new Intent(WelcomeActivity.this, InfoActivity.class);
                startActivity(getStartedIntent);
            }
        });
    }
}
