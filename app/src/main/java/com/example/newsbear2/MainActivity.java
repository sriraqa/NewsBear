package com.example.newsbear2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity
{
    public static String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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