package com.example.newsbear2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.FontRequest;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.newsbear2.ui.main.SectionsPagerAdapter;

import java.security.cert.Certificate;
import java.util.List;

public class SearchActivity extends AppCompatActivity
{
    private RecyclerView trendsRecyclerView;
    private TrendsAdapter adapter;
    private List<Claim> claims;

    //public static String query = "";
    public static int maxNumOfClaims = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        setTitle("NewsBear");
        



        trendsRecyclerView = findViewById(R.id.trends_recycler_view);

        trendsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new TrendsAdapter(SearchActivity.this, claims);
        trendsRecyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        SearchView searchView = findViewById(R.id.search_view);

        searchView.setQuery("", true);
        searchView.setQueryHint("Search for a topic or person");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
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