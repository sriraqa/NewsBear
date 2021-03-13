package com.example.newsbear2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GetStartedActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        setTitle("Get Started");

        String newsEmoji = "\uD83D\uDCF0";
        String shareEmoji = "\uD83D\uDCAC";
        String faceEmoji = "\uD83D\uDE36";

        TextView featureText = findViewById(R.id.featuresTextView);

        String text = faceEmoji + " Check the face emoji for the rating!" +
                "\n" + newsEmoji + " Open the article in your browser!" +
                "\n" + shareEmoji + " Click on the speech bubble to share!";

        featureText.setText(text);

        Button getStartedButton = findViewById(R.id.getStartedButton);

        Intent languageIntent = getIntent();

        getStartedButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent getStartedIntent = new Intent(GetStartedActivity.this, SearchActivity.class);
                if (languageIntent.hasExtra("com.example.newsbear2.LANGUAGE"))
                {
                    String language = languageIntent.getStringExtra("com.example.newsbear2.LANGUAGE");
                    getStartedIntent.putExtra("com.example.newsbear2.LANGUAGE2", language);
                }
                startActivity(getStartedIntent);
            }
        });
    }
}
