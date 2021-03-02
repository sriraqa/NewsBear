package com.example.newsbear2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends AppCompatActivity
{
    public static String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        EditText userSearch = findViewById(R.id.userSearch);
        ImageButton searchButton = findViewById(R.id.searchButton); //use findViewById to find any object by their ID
        //Creating a variable by type ImageButton

        searchButton.setOnClickListener(v ->
        {
            query = userSearch.getText().toString();

            Intent startIntent = new Intent(getApplicationContext(), GoogleFactCheckResponse.class);
            startIntent.putExtra("com.example.newsbear2.SOMETHING", query);

            startActivity(startIntent);
        });
    }

}