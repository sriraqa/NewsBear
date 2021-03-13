package com.example.newsbear2;

import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.SearchView;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity
{
    private ViewPager viewPager;

    //public static String query = "";
    public static int maxNumOfClaims = 2;

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

        SearchView searchView = findViewById(R.id.search_view);

        searchView.setQuery("", true);
        searchView.setQueryHint("Search for a controversial topic");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            Intent languageIntent = getIntent();
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
}