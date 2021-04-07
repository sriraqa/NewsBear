package com.example.newsbear2.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newsbear2.R;

public class SettingsActivity extends AppCompatActivity
{
    String language = "&languageCode=en-US";
    String maxNum = "5";

    public Context context = SettingsActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        setTitle("Language");

        //set the subtitle
        String subtitle = "Configure additional languages and number of article results. \n(NOTE: Configurations will be remembered until settings are opened again)";
        TextView tv = findViewById(R.id.textView6);
        tv.setText(subtitle);

        //sends values back to Search Activity
        Intent doneIntent = new Intent(SettingsActivity.this, SearchActivity.class);

        //get the language spinner from the xml
        Spinner languageDropdown = findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        String[] items = new String[]{"English only", "English & French", "English & Spanish"};
        //create an adapter to describe how the items are displayed
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        languageDropdown.setAdapter(adapter);

        //when the item is selected, add that value as extra
        languageDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) //l = row id
            {
                if(languageDropdown.getItemAtPosition(i).equals("English only"))
                {
                    language = "&languageCode=en-US";
                }
                else if(languageDropdown.getItemAtPosition(i).equals("English & French"))
                {
                    language = "&languageCode=fr-FR";
                }
                else if(languageDropdown.getItemAtPosition(i).equals("English & Spanish"))
                {
                    language = "&languageCode=es";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) //default value
            {
                language = "&languageCode=en-US";
            }
        });

        //max num spinner (same as above)
        Spinner numDropdown = findViewById(R.id.spinner2);
        String[] items2 = new String[]{"5", "10", "20", "30"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        numDropdown.setAdapter(adapter2);

        numDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) //l = row id
            {
                if(numDropdown.getItemAtPosition(i).equals("5"))
                {
                    maxNum = "5";
                }
                else if(numDropdown.getItemAtPosition(i).equals("10"))
                {
                    maxNum = "10";
                }
                else if(numDropdown.getItemAtPosition(i).equals("20"))
                {
                    maxNum = "20";
                }
                else if(numDropdown.getItemAtPosition(i).equals("30"))
                {
                    maxNum = "30";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) //default value
            {
                maxNum = "5";
            }
        });

        Button doneButton = findViewById(R.id.doneButton);

        doneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) //once user clicks done, sends back to search activity
            {
                doneIntent.putExtra("com.example.newsbear2.LANGUAGE", language);
                doneIntent.putExtra("com.example.newsbear2.MAXNUM", maxNum);
                SettingsActivity.this.startActivity(doneIntent);
            }
        });
    }
}
