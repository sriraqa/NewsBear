package com.example.newsbear2.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsbear2.R;
import com.example.newsbear2.Trend;
import com.example.newsbear2.TrendsAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Fragment1 extends Fragment {
    private List<Trend> trends;
    private static String TWITTER_API_URL;
    private static String GOOGLE_IMAGE_API_URL;
    private String googleFactCheckApiUrl;
    public static int maxNumOfClaims = 1;
    private RecyclerView trendsRecyclerView;
    private TrendsAdapter adapter;
    private PagerAdapter pagerAdapter;

    //newInstance constructor for creating fragment with arguments
    public static Fragment1 newInstance() {
        Fragment1 fragment1 = new Fragment1();
        return fragment1;
    }

    //store variable based on passed arguments
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TrendsAdapter(getActivity(), trends);
    }

    //Inflate the view for the fragment using XML layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Inflate the layout for the fragment
        View view = inflater.inflate(R.layout.fragment_search_1, container, false);

        trendsRecyclerView = view.findViewById(R.id.trends_recycler_view1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        trendsRecyclerView.setLayoutManager(layoutManager);
        trendsRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat curFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = curFormatter.format(d);
        String id = "id=1";

        TWITTER_API_URL = "https://api.twitter.com/1.1/trends/place.json?" + id;

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, TWITTER_API_URL, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                JSONArray trendsArray = null;

                try
                {
                    int count = 0;
                    int max = 5;
                    Trend globalTrend = new Trend();
                    JSONArray obj = new JSONArray(response.toString());
                    JSONObject obj2 = obj.getJSONObject(0);
                    trendsArray = obj2.getJSONArray("trends");

                    globalTrend.setTrendString(trendsArray.getJSONObject(0).getString("name"));
                    globalTrend.setTitle("Test");
                    globalTrend.setClaimDate("5 days ago");
                    globalTrend.setImageURL("https://reactnativecode.com/wp-content/uploads/2018/02/Default_Image_Thumbnail.png");
                    globalTrend.setImageWidth(convertDpToPixel((float) 130.0, getActivity()));
                    globalTrend.setImageHeight(convertDpToPixel((float) 120.0, getActivity()));
                    globalTrend.setRatingDescription("Test test test");
                    globalTrend.setWebsite("google.com");

                    trends.add(globalTrend);

                    adapter = new TrendsAdapter(getActivity(), trends);
                    trendsRecyclerView.setAdapter(adapter);

                    /*
                    for(int i = 0; i < max; i++)
                    {
                        String trendString = trendsArray.getJSONObject(count).getString("name");
                        globalTrend.setTitle(trendString);
                        googleFactCheckApiUrl = "https://factchecktools.googleapis.com/v1alpha1/claims:search?&query=" + trendString + "&pageSize=" + maxNumOfClaims + "&languageCode=en-US&key=" + getResources().getString(R.string.google_key);
                        RequestQueue queue2 = Volley.newRequestQueue(getActivity());
                        JsonObjectRequest jsonRequest2 = new JsonObjectRequest(Request.Method.GET, googleFactCheckApiUrl, null, response2 ->
                        {
                            JSONArray claimsArray = null;
                            String title = "No title";
                            String ratingDescription;
                            String website;
                            String claimant;
                            String claimDate;

                            try
                            {
                                JSONObject claimObj = new JSONObject(response2.toString());
                                claimsArray = claimObj.getJSONArray("claims");

                                try {
                                    JSONArray claimReviewArray = claimsArray.getJSONObject(0).getJSONArray("claimReview");
                                    //gets publisher, url, title, reviewDate, textualRating, LanguageCode

                                    try //in case these values are not inputted in the claim
                                    {
                                        claimant = claimsArray.getJSONObject(0).getString("claimant");
                                    } catch (Exception E) {
                                        claimant = "Someone";
                                    }
                                    try {
                                        title = claimReviewArray.getJSONObject(0).getString("title");
                                    } catch (Exception E) {
                                        title = claimsArray.getJSONObject(0).getString("text");
                                    }
                                    try {
                                        ratingDescription = claimReviewArray.getJSONObject(0).getJSONObject("publisher").getString("name")
                                                + " reviewed this article as ";
                                    } catch (Exception E) {
                                        ratingDescription = "Someone reviewed this article as ";
                                    }
                                    try {
                                        website = claimReviewArray.getJSONObject(0).getString("url");
                                    } catch (Exception E) {
                                        website = "google.com";
                                    }
                                    try {
                                        claimDate = claimsArray.getJSONObject(0).getString("claimDate");

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
                                            claimDate = "Date not given";
                                        }
                                    } catch (Exception E) {
                                        claimDate = "Date not given";
                                    }

                                    //sets colour based on rating
                                    String textualRating = claimReviewArray.getJSONObject(0).getString("textualRating");

                                    if (textualRating.toLowerCase().contains("false") || textualRating.toLowerCase().contains("fake") || textualRating.toLowerCase().contains("fire")
                                            || textualRating.toLowerCase().contains("unproven") || textualRating.toLowerCase().contains("misleading") || textualRating.toLowerCase().contains("not true")
                                            || textualRating.toLowerCase().contains("untrue") || textualRating.toLowerCase().contains("incorrect") || textualRating.toLowerCase().contains("pinocchio"))
                                    {
                                        ratingDescription = "f" + ratingDescription;
                                        textualRating = "\"<font color='#D0312D'>" + textualRating + "</font>\"";
                                    }
                                    else if (textualRating.toLowerCase().contains("satire") || textualRating.toLowerCase().contains("true") || textualRating.toLowerCase().contains("correct"))
                                    {
                                        ratingDescription = "t" + ratingDescription;
                                        textualRating = "\"<font color='#74B72E'>" + textualRating + "</font>\"";
                                    }
                                    else
                                    {
                                        textualRating = "\"<font color='#151E3D'>" + textualRating + "</font>\"";
                                    }
                                    if (textualRating.toLowerCase().contains("false") && textualRating.toLowerCase().contains("true"))
                                    {
                                        textualRating = "\"<font color='#151E3D'>" + textualRating + "</font>\"";
                                    }

                                    ratingDescription = ratingDescription + textualRating;

                                    globalTrend.setTrendString(trendString);
                                    globalTrend.setRatingDescription(ratingDescription);
                                    globalTrend.setWebsite(website);
                                    globalTrend.setClaimDate(claimDate);

                                    GOOGLE_IMAGE_API_URL = "https://customsearch.googleapis.com/customsearch/v1?cx=63737510a1d0d86ca&q=" + title + "&searchType=image&key=AIzaSyDc63dNSdT88mrDRO4lYr8lQA3WeA7FxhA";

                                    RequestQueue claimQueue2 = Volley.newRequestQueue(getActivity());

                                    JsonObjectRequest claimJsonRequest2 = new JsonObjectRequest(Request.Method.GET, GOOGLE_IMAGE_API_URL, null, claimResponse2 ->
                                    {
                                        String imageURL = "https://reactnativecode.com/wp-content/uploads/2018/02/Default_Image_Thumbnail.png";
                                        int imageWidth; //362 divided by width will give the multiple for dimensions
                                        int imageHeight;
                                        JSONArray imageArray = null;
                                        try
                                        {
                                            JSONObject claimObj2 = new JSONObject(claimResponse2.toString());
                                            imageArray = claimObj2.getJSONArray("items");
                                            imageURL = imageArray.getJSONObject(0).getString("link");
                                            imageWidth = imageArray.getJSONObject(0).getJSONObject("image").getInt("width");
                                            imageHeight = imageArray.getJSONObject(0).getJSONObject("image").getInt("height");
                                            Log.i("URL", imageURL);
                                            Log.i("height", Integer.toString(imageHeight));
                                            Log.i("width", Integer.toString(imageWidth));

                                            float pixels = convertDpToPixel((float) 130.0, getActivity());
                                            double multiple = pixels / imageWidth;
                                            float newImageHeight = (float) (imageHeight * multiple);

                                            globalTrend.setImageHeight(newImageHeight);
                                            globalTrend.setImageWidth(pixels);

                                        }
                                        catch(Exception E)
                                        {
                                            Log.e("Image Error", E.toString());
                                            globalTrend.setImageURL("https://reactnativecode.com/wp-content/uploads/2018/02/Default_Image_Thumbnail.png");
                                            globalTrend.setImageWidth(convertDpToPixel((float) 130.0, getActivity()));
                                            globalTrend.setImageHeight(convertDpToPixel((float) 120.0, getActivity()));
                                        }

                                        globalTrend.setImageURL(imageURL);
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            globalTrend.setImageURL("https://reactnativecode.com/wp-content/uploads/2018/02/Default_Image_Thumbnail.png");
                                            globalTrend.setImageWidth(convertDpToPixel((float) 130.0, getActivity()));
                                            globalTrend.setImageHeight(convertDpToPixel((float) 120.0, getActivity()));
                                        }
                                    });

                                    claimQueue2.add(claimJsonRequest2);

                                    if(((int) globalTrend.getImageWidth()) == 0 || ((int) globalTrend.getImageHeight()) == 0)
                                    {
                                        globalTrend.setImageURL("https://reactnativecode.com/wp-content/uploads/2018/02/Default_Image_Thumbnail.png");
                                        globalTrend.setImageWidth(convertDpToPixel((float) 130.0, getActivity()));
                                        globalTrend.setImageHeight(convertDpToPixel((float) 120.0, getActivity()));
                                    }

                                    Log.i("Width Checkpoint", Integer.toString((int) globalTrend.getImageWidth()));
                                    Log.i("Height Checkpoint", Integer.toString((int) globalTrend.getImageHeight()));

                                } catch (JSONException E)
                                {
                                    globalTrend.setImageURL("https://reactnativecode.com/wp-content/uploads/2018/02/Default_Image_Thumbnail.png");
                                    globalTrend.setImageWidth(convertDpToPixel((float) 130.0, getActivity()));
                                    globalTrend.setImageHeight(convertDpToPixel((float) 120.0, getActivity()));
                                }

                                trends.add(globalTrend);

                                trendsRecyclerView = getView().findViewById(R.id.trends_recycler_view1);

                                trendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                adapter = new TrendsAdapter(getActivity(), trends);
                                trendsRecyclerView.setAdapter(adapter);

                            } catch (JSONException e)
                            {
                                Log.d("tag", "onErrorResponse: " + e.getMessage());
                            }
                        }, new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Log.d("tag", "onErrorResponse: " + error.getMessage());
                            }
                        });

                        if((i + 1) == trendsArray.length())
                        {
                            i = max;
                        }
                        else if(globalTrend.getTitle().equals("No title"))
                        {
                            i--;
                        }
                        count++;

                        trends.add(globalTrend);

                        trendsRecyclerView = getView().findViewById(R.id.trends_recycler_view1);

                        trendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        adapter = new TrendsAdapter(getActivity(), trends);
                        trendsRecyclerView.setAdapter(adapter);
                    }

                     */
                }
                catch(Exception E)
                {
                    Log.d("ERROR","error => "+ E.toString());
                }
            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                //handle the error
                error.printStackTrace();

            }
        })
        {    //this is the part that adds the header to the request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + getResources().getString(R.string.twitter_bearer_token));

                return params;
            }
        };

        queue.add(jsonRequest);
    }


    public static float convertDpToPixel ( float dp, Context context)
    {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
