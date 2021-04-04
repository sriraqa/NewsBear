package com.example.newsbear2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newsbear2.R;

public class InfoActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setTitle("Information");

        String description = "According to Statistics Canada,<font color='#D0312D'> 96% of Canadians </font>who used the Internet to find information saw COVID-19 news that " +
                "they suspected was misleading, false or inaccurate.<br><br>According to the 2019 CIGI-Ipsos Global Survey, " +
                "<font color='#D0312D'>only 32% of global citizens </font>express at least some degree of confidence that any of the social media news feed algorithms they use are unbiased, in any context." +
                "<br><br>In a study done by Stanford assessing students' ability to identify fake news,<font color='#D0312D'> more than half of the students </font>didn't click on the link within the tweet before evaluating the usefulness of the data.";

        TextView tv = findViewById(R.id.textView2);
        tv.setText(Html.fromHtml(description));

//        Intent languageIntent = getIntent();
        Button continueButton = findViewById(R.id.continueButton);

        continueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent getStartedIntent = new Intent(InfoActivity.this, GetStartedActivity.class);
//                if (languageIntent.hasExtra("com.example.newsbear2.LANGUAGE"))
//                {
//                    String language = languageIntent.getStringExtra("com.example.newsbear2.LANGUAGE");
//                    getStartedIntent.putExtra("com.example.newsbear2.LANGUAGE2", language);
//                }

                startActivity(getStartedIntent);
            }
        });
    }
}
