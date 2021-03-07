package com.example.newsbear2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.net.Uri;

public class GoogleFactCheckResponse extends AppCompatActivity
{
    public static String query = "";
    private static String GOOGLE_FACT_API_URL;
    private static String GOOGLE_IMAGE_API_URL;
    public static int maxNumOfClaims;
    private List<Claim> claims;
    private ClaimsAdapter adapter;
    private RecyclerView claimsRecyclerView;
    int numOfResults = 0;

    //private static String imageURL = "https://reactnativecode.com/wp-content/uploads/2018/02/Default_Image_Thumbnail.png";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_claims);

        setTitle("Results");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat curFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = curFormatter.format(d);

        Intent searchIntent = getIntent();

        if (searchIntent.hasExtra("com.example.newsbear2.QUERY"))
        {
            query = searchIntent.getStringExtra("com.example.newsbear2.QUERY");
            maxNumOfClaims = searchIntent.getIntExtra("com.example.newsbear2.MAX_NUM", 10);
        }
        else
        {
            TextView tv = findViewById(R.id.result_note);
            tv.setVisibility(View.VISIBLE);
            tv.setText("Something went wrong :( ");
        }

        GOOGLE_FACT_API_URL = "https://factchecktools.googleapis.com/v1alpha1/claims:search?&query=" + query + "&pageSize=" + maxNumOfClaims + "&languageCode=en-US&key=" + getResources().getString(R.string.google_key);
        claims = new ArrayList<>();

        extractClaims(formattedDate);
    }

    private void extractClaims(String formattedDate)
    {
        RequestQueue queue = Volley.newRequestQueue(GoogleFactCheckResponse.this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, GOOGLE_FACT_API_URL, null, response ->
        {
            JSONArray claimsArray = null;
            String title = "No title";
            String ratingDescription;
            String website;
            String description;
            String claimant;
            String claimDate;

            try
            {
                JSONObject obj = new JSONObject(response.toString());
                claimsArray = obj.getJSONArray("claims");
                int length = claimsArray.length();

                for (int i = 0; i < length; i++)
                {

                    try {
                        JSONArray claimReviewArray = claimsArray.getJSONObject(i).getJSONArray("claimReview");
                        //gets publisher, url, title, reviewDate, textualRating, LanguageCode

                        try //in case these values are not inputted in the claim
                        {
                            claimant = claimsArray.getJSONObject(i).getString("claimant");
                        } catch (Exception E) {
                            claimant = "Anonymous";
                        }
                        try {
                            title = claimReviewArray.getJSONObject(0).getString("title");
                        } catch (Exception E) {
                            title = claimsArray.getJSONObject(i).getString("text");
                        }
                        try {
                            ratingDescription = claimReviewArray.getJSONObject(0).getJSONObject("publisher").getString("name")
                                    + " reviewed this article as ";
                        } catch (Exception E) {
                            ratingDescription = "Anonymous reviewed this article as ";
                        }
                        try {
                            website = claimReviewArray.getJSONObject(0).getString("url");
                        } catch (Exception E) {
                            website = "No url";
                        }
                        try {
                            description = "Claim from " + claimant + ": " + claimsArray.getJSONObject(i).getString("text");
                        } catch (Exception E) {
                            description = "No description";
                        }
                        try {
                            claimDate = claimReviewArray.getJSONObject(0).getString("reviewDate");

                            if (Integer.parseInt(formattedDate.substring(0, 4)) - Integer.parseInt(claimDate.substring(0, 4)) == 0) {
                                if (Integer.parseInt(formattedDate.substring(5, 7)) - Integer.parseInt(claimDate.substring(5, 7)) == 0) {
                                    if (Integer.parseInt(formattedDate.substring(8)) - Integer.parseInt(claimDate.substring(8, 10)) == 0) {
                                        claimDate = "Today";
                                    } else {
                                        if (Integer.parseInt(formattedDate.substring(8)) - Integer.parseInt(claimDate.substring(8, 10)) == 1) {
                                            claimDate = Integer.parseInt(formattedDate.substring(8)) - Integer.parseInt(claimDate.substring(8, 10)) + " day ago";
                                        } else {
                                            claimDate = Integer.parseInt(formattedDate.substring(8)) - Integer.parseInt(claimDate.substring(8, 10)) + " days ago";
                                        }
                                    }
                                } else {
                                    if (Integer.parseInt(formattedDate.substring(5, 7)) - Integer.parseInt(claimDate.substring(5, 7)) == 1) {
                                        claimDate = (Integer.parseInt(formattedDate.substring(5, 7)) - Integer.parseInt(claimDate.substring(5, 7))) + " month ago";
                                    } else {
                                        claimDate = (Integer.parseInt(formattedDate.substring(5, 7)) - Integer.parseInt(claimDate.substring(5, 7))) + " months ago";
                                    }
                                }
                            } else {
                                if (Integer.parseInt(formattedDate.substring(0, 4)) - Integer.parseInt(claimDate.substring(0, 4)) == 1) {
                                    claimDate = (Integer.parseInt(formattedDate.substring(0, 4)) - Integer.parseInt(claimDate.substring(0, 4))) + " year ago";
                                } else {
                                    claimDate = (Integer.parseInt(formattedDate.substring(0, 4)) - Integer.parseInt(claimDate.substring(0, 4))) + " years ago";
                                }
                            }
                            if(claimDate.contains("-"))
                            {
                                claimDate = "Unknown date";
                            }
                        } catch (Exception E) {
                            claimDate = "Unknown date";
                        }

                        //sets colour based on rating
                        String textualRating = claimReviewArray.getJSONObject(0).getString("textualRating");

                        if (textualRating.toLowerCase().contains("false") || textualRating.toLowerCase().contains("fake") || textualRating.toLowerCase().contains("fire")
                                || textualRating.toLowerCase().contains("unproven") || textualRating.toLowerCase().contains("misleading") || textualRating.toLowerCase().contains("not true")
                                || textualRating.toLowerCase().contains("untrue") || textualRating.toLowerCase().contains("incorrect") || textualRating.toLowerCase().contains("pinocchio")) {
                            textualRating = "\"<font color='#D0312D'>" + textualRating + "</font>\"";
                        } else if (textualRating.toLowerCase().contains("satire") || textualRating.toLowerCase().contains("true") || textualRating.toLowerCase().contains("correct")) {
                            textualRating = "\"<font color='#74B72E'>" + textualRating + "</font>\"";
                        } else {
                            textualRating = "\"<font color='#151E3D'>" + textualRating + "</font>\"";
                        }
                        if (textualRating.toLowerCase().contains("false") && textualRating.toLowerCase().contains("true")) {
                            textualRating = "\"<font color='#151E3D'>" + textualRating + "</font>\"";
                        }

                        ratingDescription = ratingDescription + textualRating;

                        Claim claim = new Claim();
                        claim.setTitle(title);
                        claim.setRatingDescription(ratingDescription);
                        claim.setWebsite(website);
                        claim.setDescription(description);
                        claim.setClaimDate(claimDate);

                        GOOGLE_IMAGE_API_URL = "https://customsearch.googleapis.com/customsearch/v1?cx=63737510a1d0d86ca&q=" + title + "&searchType=image&key=AIzaSyDc63dNSdT88mrDRO4lYr8lQA3WeA7FxhA";

                        RequestQueue queue2 = Volley.newRequestQueue(GoogleFactCheckResponse.this);
                        JsonObjectRequest jsonRequest2 = new JsonObjectRequest(Request.Method.GET, GOOGLE_IMAGE_API_URL, null, response2 ->
                        {
                            String imageURL = "https://reactnativecode.com/wp-content/uploads/2018/02/Default_Image_Thumbnail.png";
                            JSONArray imageArray = null;
                            try
                            {
                                JSONObject obj2 = new JSONObject(response2.toString());
                                imageArray = obj2.getJSONArray("items");
                                imageURL = imageArray.getJSONObject(0).getString("link");
                                Log.i("URL", imageURL);
                            }
                            catch(Exception E)
                            {
                                Log.e("Image Error", E.toString());
                            }

                            claim.setImageURL(imageURL);
                        }, error -> Log.d("tag", "onErrorResponse: " + error.getMessage()));
                        queue2.add(jsonRequest2);


                        claims.add(claim);

                    } catch (JSONException E) {
                        TextView tv = findViewById(R.id.result_note);
                        tv.setVisibility(View.VISIBLE);
                        tv.setText("There are no claims for this topic :( " + "\nError: " + E);
                    }
                }

                claimsRecyclerView = findViewById(R.id.claims_recycler_view);

                claimsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new ClaimsAdapter(GoogleFactCheckResponse.this, claims);
                claimsRecyclerView.setAdapter(adapter);

                numOfResults = adapter.getItemCount();
                String text = "Showing " + numOfResults + " results for " + "\"" + query + "\"";
                setTitle(text);
            } catch (JSONException e) {
                TextView tv = findViewById(R.id.result_note);
                tv.setVisibility(View.VISIBLE);
                tv.setText("No results found " + "\nError: " + e);
            }
        }, error -> Log.d("tag", "onErrorResponse: " + error.getMessage()));

        queue.add(jsonRequest);
    }
}
