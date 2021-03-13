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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsbear2.R;
import com.example.newsbear2.Trend;
import com.example.newsbear2.TrendsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.monkeylearn.MonkeyLearn;
import com.monkeylearn.MonkeyLearnResponse;
import com.monkeylearn.MonkeyLearnException;
import com.monkeylearn.Tuple;

public class Fragment1 extends Fragment {
    private List<Trend> trends;
    private static String GOOGLE_NEWS_API_URL;
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
        trends = new ArrayList<>();
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
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat curFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = curFormatter.format(d);
        Trend globalTrend = new Trend();

        GOOGLE_NEWS_API_URL = "https://newsapi.org/v2/top-headlines?country=ca&apiKey=" + getResources().getString(R.string.google_news_key);
        RequestQueue queue2 = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonRequest2 = new JsonObjectRequest(Request.Method.GET, GOOGLE_NEWS_API_URL, null, response2 ->
        {
            JSONArray newsArray = null;
            String title = "No title";
            String website;
            String imageURL;
            String author;
            String claimDate;
            String keyWords;

            try {
                JSONObject newsObj = new JSONObject(response2.toString());
                newsArray = newsObj.getJSONArray("articles");

                try {
                    try //in case these values are not inputted in the claim
                    {
                        author = newsArray.getJSONObject(0).getJSONObject("source").getString("name");
                    } catch (Exception E) {
                        author = "Someone";
                    }
                    try {
                        title = newsArray.getJSONObject(0).getString("title");
                    } catch (Exception E) {
                        title = "No title";
                    }
                    try {
                        keyWords = "blank";
                    } catch (Exception E) {
                        keyWords = "";
                    }
                    try {
                        website = newsArray.getJSONObject(0).getString("url");
                    } catch (Exception E) {
                        website = "google.com";
                    }
                    try {
                        claimDate = newsArray.getJSONObject(0).getString("publishedAt");

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
                    } catch (Exception E) {
                        claimDate = "Date not given";
                    }

                    globalTrend.setTrendString(keyWords);
                    globalTrend.setRatingDescription(author);
                    globalTrend.setWebsite(website);
                    globalTrend.setClaimDate(claimDate);
                    globalTrend.setTitle(title);

                    imageURL = newsArray.getJSONObject(0).getString("urlToImage");
                    globalTrend.setImageURL(imageURL);
                    globalTrend.setImageWidth(convertDpToPixel((float) 130.0, getActivity()));
                    globalTrend.setImageHeight(convertDpToPixel((float) 130.0, getActivity()));


                    if (((int) globalTrend.getImageWidth()) == 0 || ((int) globalTrend.getImageHeight()) == 0) {
                        globalTrend.setImageURL("https://reactnativecode.com/wp-content/uploads/2018/02/Default_Image_Thumbnail.png");
                        globalTrend.setImageWidth(convertDpToPixel((float) 130.0, getActivity()));
                        globalTrend.setImageHeight(convertDpToPixel((float) 130.0, getActivity()));
                    }

                } catch (JSONException E) {
                    globalTrend.setImageURL("https://reactnativecode.com/wp-content/uploads/2018/02/Default_Image_Thumbnail.png");
                    globalTrend.setImageWidth(convertDpToPixel((float) 130.0, getActivity()));
                    globalTrend.setImageHeight(convertDpToPixel((float) 130.0, getActivity()));
                }

                trends.add(globalTrend);

                trendsRecyclerView = getView().findViewById(R.id.trends_recycler_view1);

                trendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new TrendsAdapter(getActivity(), trends);
                trendsRecyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                Log.d("tag", "onErrorResponse: " + e.getMessage());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        queue2.add(jsonRequest2);
    }

    public static float convertDpToPixel ( float dp, Context context)
    {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
