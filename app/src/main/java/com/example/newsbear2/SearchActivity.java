package com.example.newsbear2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsbear2.ui.main.Fragment1;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.FontRequest;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsbear2.ui.main.SectionsPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.cert.Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SearchActivity extends AppCompatActivity
{
    private ViewPager viewPager;
    private int tabOnePos;
    private int tabTwoPos;

    //public static String query = "";
    public static int maxNumOfClaims = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        for(int i = 0; i < tabs.getTabCount(); i++)
        {
            TabLayout.Tab tab = tabs.getTabAt(i);
            if(i == 0)
            {
                tab.setIcon(R.drawable.img_35354);
                tab.setText("Trending globally");
                tabOnePos = tab.getPosition();
            }
            else
            {
                tab.setIcon(R.drawable.adress_icon_3);
                tab.setText("Trending locally");
                tabTwoPos = tab.getPosition();
            }
        }

        String id = "id=1";

        setTitle("NewsBear");

        //listens for chenges in the activity
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
          {
              //invoked when a new page is selected
              @Override
              public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
              {
                  Fragment fragment = sectionsPagerAdapter.getItem(position);
              }

              //invoked when the current page is scrolled
              @Override
              public void onPageSelected(int position)
              {
                  Toast.makeText(SearchActivity.this, "Selected Page Position: " + position, Toast.LENGTH_SHORT). show();
                  Fragment fragment = sectionsPagerAdapter.getItem(position);
              }

              //invoked then scroll state changes (SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING)
              @Override
              public void onPageScrollStateChanged(int state)
              {

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