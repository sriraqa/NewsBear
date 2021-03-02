package com.example.newsbear2;

import android.app.ActionBar;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoogleFactCheckResponse extends AppCompatActivity
{
    public static String query = "";
    private static String GOOGLE_API_URL;
    List<Claim> claims;
    ClaimsAdapter adapter;
    RecyclerView claimsRecyclerView;
    int numOfResults = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat curFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = curFormatter.format(d);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_claims);

        if (getIntent().hasExtra("com.example.newsbear2.SOMETHING")) {
            query = getIntent().getExtras().getString("com.example.newsbear2.SOMETHING");
        }

        GOOGLE_API_URL = "https://factchecktools.googleapis.com/v1alpha1/claims:search?&query=" + query + "&languageCode=en-US&key=" + getResources().getString(R.string.google_key);
        claims = new ArrayList<>();

        extractClaims(formattedDate);
    }

    private void extractClaims(String formattedDate)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, GOOGLE_API_URL, null, response ->
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
                    try
                    {
                        JSONArray claimReviewArray = claimsArray.getJSONObject(i).getJSONArray("claimReview");
                        //gets publisher, url, title, reviewDate, textualRating, LanguageCode

                        try //in case these values are not inputted in the claim
                        {
                            claimant = claimsArray.getJSONObject(i).getString("claimant");
                        }
                        catch(Exception E)
                        {
                            claimant = "Anonymous";
                        }
                        try
                        {
                            title = claimReviewArray.getJSONObject(0).getString("title");
                        }
                        catch(Exception E)
                        {
                            title = claimsArray.getJSONObject(i).getString("text");
                        }
                        try
                        {
                            ratingDescription = claimReviewArray.getJSONObject(0).getJSONObject("publisher").getString("name")
                                    + " reviewed this article as ";
                        }
                        catch(Exception E)
                        {
                            ratingDescription = "Anonymous reviewed this article as ";
                        }
                        try
                        {
                            website = claimReviewArray.getJSONObject(0).getString("url");
                        }
                        catch(Exception E)
                        {
                            website = "No url";
                        }
                        try
                        {
                            description = "Claim from " + claimant + ": " + claimsArray.getJSONObject(i).getString("text");
                        }
                        catch(Exception E)
                        {
                            description = "No description";
                        }
                        try
                        {
                            claimDate = claimReviewArray.getJSONObject(0).getString("reviewDate");

                            if(Integer.parseInt(formattedDate.substring(0, 4)) - Integer.parseInt(claimDate.substring(0, 4)) == 0)
                            {
                                if(Integer.parseInt(formattedDate.substring(5, 7)) - Integer.parseInt(claimDate.substring(5, 7)) == 0)
                                {
                                    if(Integer.parseInt(formattedDate.substring(8)) - Integer.parseInt(claimDate.substring(8, 10)) == 0)
                                    {
                                        claimDate = "Today";
                                    }
                                    else
                                    {
                                        if(Integer.parseInt(formattedDate.substring(8)) - Integer.parseInt(claimDate.substring(8, 10)) == 1)
                                        {
                                            claimDate = Integer.parseInt(formattedDate.substring(8)) - Integer.parseInt(claimDate.substring(8, 10)) + " day ago";
                                        }
                                        else
                                        {
                                            claimDate = Integer.parseInt(formattedDate.substring(8)) - Integer.parseInt(claimDate.substring(8, 10)) + " days ago";
                                        }
                                    }
                                }
                                else
                                {
                                    if(Integer.parseInt(formattedDate.substring(5, 7)) - Integer.parseInt(claimDate.substring(5, 7)) == 1)
                                    {
                                        claimDate = (Integer.parseInt(formattedDate.substring(5, 7)) - Integer.parseInt(claimDate.substring(5, 7))) + " month ago";
                                    }
                                    else
                                    {
                                        claimDate = (Integer.parseInt(formattedDate.substring(5, 7)) - Integer.parseInt(claimDate.substring(5, 7))) + " months ago";
                                    }
                                }
                            }
                            else
                            {
                                if(Integer.parseInt(formattedDate.substring(0, 4)) - Integer.parseInt(claimDate.substring(0, 4)) == 1)
                                {
                                    claimDate = (Integer.parseInt(formattedDate.substring(0, 4)) - Integer.parseInt(claimDate.substring(0, 4))) + " year ago";
                                }
                                else
                                {
                                    claimDate = (Integer.parseInt(formattedDate.substring(0, 4)) - Integer.parseInt(claimDate.substring(0, 4))) + " years ago";
                                }
                            }
                        }
                        catch(Exception E)
                        {
                            claimDate = "Unknown date";
                        }

                        //sets colour based on rating
                        String textualRating = claimReviewArray.getJSONObject(0).getString("textualRating");

                        if(textualRating.toLowerCase().contains("false") || textualRating.toLowerCase().contains("fake") || textualRating.toLowerCase().contains("fire")
                            || textualRating.toLowerCase().contains("unproven") || textualRating.toLowerCase().contains("misleading"))
                        {
                            textualRating = "<font color='#D0312D'>" + textualRating + "</font>";
                        }
                        else if(textualRating.toLowerCase().contains("satire") || textualRating.toLowerCase().contains("true"))
                        {
                            textualRating = "<font color='#74B72E'>" + textualRating + "</font>";
                        }
                        else
                        {
                            textualRating = "<font color='#151E3D'>" + textualRating + "</font>";
                        }
                        if(textualRating.toLowerCase().contains("false") && textualRating.toLowerCase().contains("true"))
                        {
                            textualRating = "<font color='#151E3D'>" + textualRating + "</font>";
                        }

                        ratingDescription = ratingDescription + textualRating;

                        Claim claim = new Claim();
                        claim.setTitle(title);
                        claim.setRatingDescription(ratingDescription);
                        claim.setWebsite(website);
                        claim.setDescription(description);
                        claim.setClaimDate(claimDate);

                        claims.add(claim);
                    } catch (JSONException E)
                    {
                        TextView tv = findViewById(R.id.result_note);
                        tv.setVisibility(View.VISIBLE);
                        tv.setText("There are no claims for this topic :( " + "\nError: " + E);
                    }
                }
                claimsRecyclerView = findViewById(R.id.claims_recycler_view);

                claimsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new ClaimsAdapter(getApplicationContext(), claims);
                claimsRecyclerView.setAdapter(adapter);

                numOfResults = adapter.getItemCount();
                String text = "Showing " + numOfResults + " results for " + "\"" + query + "\"";
                setTitle(text);
            }
            catch (JSONException e)
            {
                TextView tv = findViewById(R.id.result_note);
                tv.setVisibility(View.VISIBLE);
                tv.setText("No results found " + "\nError: " + e);
            }
        }, error -> Log.d("tag", "onErrorResponse: " + error.getMessage()));

        queue.add(jsonRequest);
    }
}
