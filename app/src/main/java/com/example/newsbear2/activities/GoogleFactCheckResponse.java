package com.example.newsbear2.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsbear2.R;
import com.example.newsbear2.claims.Claim;
import com.example.newsbear2.claims.ClaimsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GoogleFactCheckResponse extends AppCompatActivity
{
    public static String query = "";
    private static String GOOGLE_FACT_API_URL;
    private static String GOOGLE_IMAGE_API_URL;
    public static String maxNumOfClaims;
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

        //stores the query and the maximum claims if the previous intent passed extra
        if (searchIntent.hasExtra("com.example.newsbear2.QUERY"))
        {
            query = searchIntent.getStringExtra("com.example.newsbear2.QUERY");
            maxNumOfClaims = searchIntent.getStringExtra("com.example.newsbear2.MAX_NUM");
        }
        else
        {
            TextView tv = findViewById(R.id.result_note);
            tv.setVisibility(View.VISIBLE);
            String errorText = "Something went wrong :( ";
            tv.setText(errorText);
        }

        GOOGLE_FACT_API_URL = "https://factchecktools.googleapis.com/v1alpha1/claims:search?&query=" + query + "&pageSize=" + maxNumOfClaims + "&key=" + getResources().getString(R.string.google_key);
        claims = new ArrayList<>();

        extractClaims(formattedDate);
    }

    private void extractClaims(String formattedDate)
    {
        //Google Volley to call the API
        RequestQueue queue = Volley.newRequestQueue(GoogleFactCheckResponse.this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, GOOGLE_FACT_API_URL, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray claimsArray = null;
                String title = "No title";
                String ratingDescription;
                String website;
                String description;
                String claimant;
                String claimDate;

                try {
                    JSONObject obj = new JSONObject(response.toString());
                    claimsArray = obj.getJSONArray("claims");
                    int length = claimsArray.length();

                    for (int i = 0; i < length; i++) {

                        try {
                            JSONArray claimReviewArray = claimsArray.getJSONObject(i).getJSONArray("claimReview");
                            //gets publisher, url, title, reviewDate, textualRating, LanguageCode

                            try //in case these values are not inputted in the claim
                            {
                                claimant = claimsArray.getJSONObject(i).getString("claimant");
                            } catch (Exception E) {
                                claimant = "Someone";
                            }
                            try {
                                title = claimReviewArray.getJSONObject(0).getString("title");
                            } catch (Exception E) {
                                title = claimsArray.getJSONObject(i).getString("text");
                            }
                            try {
                                ratingDescription = claimReviewArray.getJSONObject(0).getJSONObject("publisher").getString("name")
                                        + " reviewed this claim as ";
                            } catch (Exception E) {
                                ratingDescription = "Someone reviewed this claim as ";
                            }
                            try {
                                website = claimReviewArray.getJSONObject(0).getString("url");
                            } catch (Exception E) {
                                website = "google.com";
                            }
                            try {
                                description = "Claim from " + claimant + ": \"" + claimsArray.getJSONObject(i).getString("text") + "\"";
                            } catch (Exception E) {
                                description = "No description";
                            }
                            try {
                                claimDate = claimsArray.getJSONObject(i).getString("claimDate");

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
                                if (claimDate.contains("-")) {
                                    claimDate = "Date not given";
                                }
                            } catch (Exception E) {
                                claimDate = "Date not given";
                            }

                            //sets colour based on rating
                            String textualRating = claimReviewArray.getJSONObject(0).getString("textualRating");

                            if (textualRating.toLowerCase().contains("false") || textualRating.toLowerCase().contains("fake") || textualRating.toLowerCase().contains("fire")
                                    || textualRating.toLowerCase().contains("unproven") || textualRating.toLowerCase().contains("misleading") || textualRating.toLowerCase().contains("not true")
                                    || textualRating.toLowerCase().contains("untrue") || textualRating.toLowerCase().contains("incorrect") || textualRating.toLowerCase().contains("pinocchio")
                                    || textualRating.toLowerCase().contains("trompeur") || textualRating.toLowerCase().contains("imprecis") || textualRating.toLowerCase().contains("faux")
                                    || textualRating.toLowerCase().contains("enganoso") || textualRating.toLowerCase().contains("falso") || textualRating.toLowerCase().contains("erroneo")
                                    || textualRating.toLowerCase().contains("distort")) {
                                ratingDescription = "f" + ratingDescription;
                                textualRating = "\"<font color='#D0312D'>" + textualRating + "</font>\"";
                            } else if (textualRating.toLowerCase().contains("satire") || textualRating.toLowerCase().contains("true") || textualRating.toLowerCase().contains("correct")
                                    || textualRating.toLowerCase().contains("vraix") || textualRating.toLowerCase().contains("cierto") || textualRating.toLowerCase().contains("pinocchio")
                                    || textualRating.toLowerCase().contains("verdadero")) {
                                ratingDescription = "t" + ratingDescription;
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
                                int imageWidth; //362 divided by width will give the multiple for dimensions
                                int imageHeight;
                                JSONArray imageArray = null;
                                try {
                                    JSONObject obj2 = new JSONObject(response2.toString());
                                    imageArray = obj2.getJSONArray("items");
                                    imageURL = imageArray.getJSONObject(0).getString("link");
                                    imageWidth = imageArray.getJSONObject(0).getJSONObject("image").getInt("width");
                                    imageHeight = imageArray.getJSONObject(0).getJSONObject("image").getInt("height");
                                    Log.i("URL", imageURL);

                                    float pixels = convertDpToPixel((float) 362.0, GoogleFactCheckResponse.this);
                                    double multiple = pixels / imageWidth;
                                    float newImageHeight = (float) (imageHeight * multiple);

                                    claim.setImageHeight(newImageHeight);
                                    claim.setImageWidth(pixels);
                                    claim.setImageURL(imageURL);

                                    claims.add(claim);

                                    claimsRecyclerView = findViewById(R.id.claims_recycler_view);

                                    claimsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    adapter = new ClaimsAdapter(GoogleFactCheckResponse.this, claims);
                                    claimsRecyclerView.setAdapter(adapter);

                                    numOfResults = adapter.getItemCount();
                                    String text = "Showing " + numOfResults + " results for " + "\"" + query + "\"";
                                    setTitle(text);
                                } catch (Exception E) {
                                    Log.e("Image Error", E.toString());
                                    claim.setImageURL("https://reactnativecode.com/wp-content/uploads/2018/02/Default_Image_Thumbnail.png");
                                    claim.setImageWidth(convertDpToPixel((float) 362.0, GoogleFactCheckResponse.this));
                                    claim.setImageHeight(convertDpToPixel((float) 286.0, GoogleFactCheckResponse.this));

                                    claims.add(claim);

                                    claimsRecyclerView = findViewById(R.id.claims_recycler_view);

                                    claimsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    adapter = new ClaimsAdapter(GoogleFactCheckResponse.this, claims);
                                    claimsRecyclerView.setAdapter(adapter);

                                    String text = "Showing no results for " + "\"" + query + "\"";
                                    setTitle(text);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Image Error", error.toString());
                                    claim.setImageURL("https://reactnativecode.com/wp-content/uploads/2018/02/Default_Image_Thumbnail.png");
                                    claim.setImageWidth(convertDpToPixel((float) 362.0, GoogleFactCheckResponse.this));
                                    claim.setImageHeight(convertDpToPixel((float) 286.0, GoogleFactCheckResponse.this));

                                    //Limit of 100 API calls for the images per day
                                    Toast.makeText(GoogleFactCheckResponse.this, "Temporary image 403 error", Toast.LENGTH_SHORT).show();

                                    claims.add(claim);

                                    claimsRecyclerView = findViewById(R.id.claims_recycler_view);

                                    claimsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    adapter = new ClaimsAdapter(GoogleFactCheckResponse.this, claims);
                                    claimsRecyclerView.setAdapter(adapter);

                                    numOfResults = adapter.getItemCount();
                                    String text = "Showing no results for " + "\"" + query + "\"";
                                    setTitle(text);
                                }
                            });

                            queue2.add(jsonRequest2);

                            if (((int) claim.getImageWidth()) == 0 || ((int) claim.getImageHeight()) == 0) {
                                claim.setImageURL("https://reactnativecode.com/wp-content/uploads/2018/02/Default_Image_Thumbnail.png");
                                claim.setImageWidth(convertDpToPixel((float) 362.0, GoogleFactCheckResponse.this));
                                claim.setImageHeight(convertDpToPixel((float) 286.0, GoogleFactCheckResponse.this));
                            }

                            Log.i("Width Checkpoint", Integer.toString((int) claim.getImageWidth()));
                            Log.i("Height Checkpoint", Integer.toString((int) claim.getImageHeight()));

                        } catch (JSONException E) {
                            TextView tv = findViewById(R.id.result_note);
                            tv.setVisibility(View.VISIBLE);
                            String errorText = "There are no claims for this topic :(";
                            tv.setText(errorText);
                        }
                    }

                } catch (JSONException e) {
                    TextView tv = findViewById(R.id.result_note);
                    tv.setVisibility(View.VISIBLE);
                    String errorText = "There are no claims for this topic :(";
                    tv.setText(errorText);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("Volly Error", error.toString());

                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Status code", String.valueOf(networkResponse.statusCode));
                }

                Toast.makeText(GoogleFactCheckResponse.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonRequest);
    }

    public static float convertDpToPixel(float dp, Context context)
    {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
