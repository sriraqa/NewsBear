package com.example.newsbear2;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewer extends AppCompatActivity
{
    private String url = "google.com";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_viewer);

        Intent webIntent = getIntent();
        if (webIntent.hasExtra("com.example.newsbear2.URL"))
        {
            url = webIntent.getStringExtra("com.example.newsbear2.URL");
        }

        WebView webView = findViewById(R.id.web_view);
        webView.loadUrl(url);
    }
}
