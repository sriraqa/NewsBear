package com.example.newsbear2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.TextView;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_claims);

        if (getIntent().hasExtra("com.example.newsbear2.SOMETHING")) {
            TextView tv = findViewById(R.id.result_note);
            query = getIntent().getExtras().getString("com.example.newsbear2.SOMETHING");
            String text = "Showing " + numOfResults + " results for " + "\"" + query + "\"";
            tv.setText(text);
        }

        GOOGLE_API_URL = "https://factchecktools.googleapis.com/v1alpha1/claims:search?&query=" + query + "&languageCode=en-US&key=" + getResources().getString(R.string.google_key);
        claims = new ArrayList<>();

        extractClaims();
    }

    private void extractClaims()
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
                            description = claimant + " claimed that " + claimsArray.getJSONObject(i).getString("text");
                        }
                        catch(Exception E)
                        {
                            description = "No description";
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

                        ratingDescription = ratingDescription + textualRating;

                        Claim claim = new Claim();
                        claim.setTitle(title);
                        claim.setRatingDescription(ratingDescription);
                        claim.setWebsite(website);
                        claim.setDescription(description);

                        claims.add(claim);
                    } catch (JSONException E)
                    {

                        TextView tv = findViewById(R.id.result_note);
                        tv.setText("No results found " + E);
                    }
                }
                claimsRecyclerView = findViewById(R.id.claims_recycler_view);

                claimsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new ClaimsAdapter(getApplicationContext(), claims);
                claimsRecyclerView.setAdapter(adapter);

                numOfResults = adapter.getItemCount();
                TextView tv = findViewById(R.id.result_note);
                String text = "Showing " + numOfResults + " results for " + "\"" + query + "\"";
                tv.setText(text);
            }
            catch (JSONException e)
            {
                TextView tv = findViewById(R.id.result_note);
                tv.setText("No results found " + e);
            }
        }, error -> Log.d("tag", "onErrorResponse: " + error.getMessage()));

        queue.add(jsonRequest);
    }
}
