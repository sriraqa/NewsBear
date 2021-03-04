package com.example.newsbear2;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.newsbear2.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends AppCompatActivity
{
    //public static String query = "";
    public static int maxNumOfClaims = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("NewsBear");

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_item, menu);

        MenuItem search = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) search.getActionView();

        searchView.setQuery("", true);
        searchView.setQueryHint("Search for topics...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Intent searchIntent = new Intent(HomeActivity.this, GoogleFactCheckResponse.class);
                searchIntent.putExtra("com.example.newsbear2.QUERY", query);
                searchIntent.putExtra("com.example.newsbear2.MAX_NUM", maxNumOfClaims);

                HomeActivity.this.startActivity(searchIntent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}