package com.example.newsbear2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class GoogleFactCheckResponse extends AppCompatActivity
{
    public static String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_claims);

        if (getIntent().hasExtra("com.example.newsbear2.SOMETHING")) {
            TextView tv = findViewById(R.id.result_note);
            query = getIntent().getExtras().getString("com.example.newsbear2.SOMETHING");
            String text = "Search results for " + "\"" + query + "\"";
            tv.setText(text);
        }

        final TextView responseTextView = findViewById(R.id.response_text);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://factchecktools.googleapis.com/v1alpha1/claims:search?&query=" + query + "&languageCode=en-US&key=AIzaSyDc63dNSdT88mrDRO4lYr8lQA3WeA7FxhA";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> responseTextView.setText("Response is: " + response), error -> responseTextView.setText("No Search Results!"));

        queue.add(stringRequest);
    }
}
