package com.example.newsbear2.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsbear2.R;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity
{
    ImageView speechButton;
    SearchView searchView;
    private ViewPager viewPager;

    //public static String query = "";
    public static int maxNumOfClaims = 2;
    private static final int RECOGNIZER_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String id = "id=1";

        setTitle("NewsBear");

        int random = (int) (Math.random() * 4);
        String text;

        TextView questionTextView = findViewById(R.id.questionTextView);

        if(random == 0)
        {
            text = "What controversial event is going on right now?";
            questionTextView.setText(text);
        }
        else if(random == 1)
        {
            text = "Which celebrity are people talking about recently?";
            questionTextView.setText(text);
        }
        else if(random == 3)
        {
            text = "What's a general topic that you're interested in learning about?";
            questionTextView.setText(text);
        }
        else
        {
            text = "What's a mainstream topic that's been on your mind?";
            questionTextView.setText(text);
        }

        speechButton = findViewById(R.id.speech_button);
        searchView = findViewById(R.id.search_view);

        speechButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    //informs recognizer which model to refer (free form)
                    speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                    speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech to text search");

                    //RECOGNIZER_RESULT will be used to confirm later on
                    startActivityForResult(speechIntent, RECOGNIZER_RESULT);
                }
                catch(ActivityNotFoundException e)
                {
                    Toast.makeText(SearchActivity.this, "Your device does not support speech to text.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        searchView.setQuery("", true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            final Intent languageIntent = getIntent();
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                if (languageIntent.hasExtra("com.example.newsbear2.LANGUAGE2"))
                {
                    query += languageIntent.getStringExtra("com.example.newsbear2.LANGUAGE2");
                }

                Intent searchIntent = new Intent(SearchActivity.this, GoogleFactCheckResponse.class);
                searchIntent.putExtra("com.example.newsbear2.QUERY", query);
                searchIntent.putExtra("com.example.newsbear2.MAX_NUM", maxNumOfClaims);

                SearchActivity.this.startActivity(searchIntent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK && data != null)
        {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            searchView.setQuery(matches.get(0), true);
        }
    }
}