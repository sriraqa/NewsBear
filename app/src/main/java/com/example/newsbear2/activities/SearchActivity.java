package com.example.newsbear2.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsbear2.R;

import java.util.ArrayList;
import java.util.List;

import com.example.newsbear2.Trend;
import com.example.newsbear2.TrendsAdapter;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.TextRazor;
import com.textrazor.annotations.AnalyzedText;

public class SearchActivity extends AppCompatActivity
{
    ImageView speechButton;
    SearchView searchView;
    private ViewPager viewPager;

    //public static String query = "";
    public static int maxNumOfClaims = 5;
    private static final int RECOGNIZER_RESULT = 1;
    private final String TEXT_RAZOR_URL = "https://api.textrazor.com";

    private List<Trend> trendsEntities;
    private TrendsAdapter adapter;
    private RecyclerView trendsRecyclerView1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String id = "id=1";

        setTitle("NewsBear");

        int random = (int) (Math.random() * 4);
        String text;

        TextView questionTextView = findViewById(R.id.questionTextView);

        if(random == 0)
        {
            text = "What controversial event is going on right now?";
            questionTextView.setText(text);
        }
        else if(random == 1)
        {
            text = "Which celebrity have you heard about recently?";
            questionTextView.setText(text);
        }
        else if(random == 3)
        {
            text = "What's a general topic that you're interested in?";
            questionTextView.setText(text);
        }
        else
        {
            text = "What mainstream topic has been on your mind?";
            questionTextView.setText(text);
        }

        String articleHeadline = "Spain's stricken Bankia expects to sell off its vast portfolio of " +
                "industrial holdings that includes a stake in the parent company of British Airways and Iberia.";

//      (get bearer token and refresh token each time)
//      curl -X POST -A "User agent" -d "grant_type=refresh_token&refresh_token=357005052207-WZj9af4XvdYWVM9RhnoRIzuM7YILZA" --user "UKAjETNsx7H55w:PpuXQt5YBhJrVlrqNcOHUSYv4-NV1Q" https://www.reddit.com/api/v1/access_token
//      (call the subreddit and get "title" from "data")
//      curl -H "Authorization: bearer 357005052207-apyzhZkRU0FIC7E2ZWbkrZ9V4pO-wA" -A "UKAjETNsx7H55w/0.1 by Saqiao" --get "https://oauth.reddit.com/r/news/top/.json?t=day&count=10"

//        RequestQueue redditQueue = Volley.newRequestQueue(SearchActivity.this);
//        JsonObjectRequest redditJsonRequest = new JsonObjectRequest(Request.Method.POST, "https://www.reddit.com/api/v1/access_token", null, response ->
//        {
//            JSONArray topics = null;
//            JSONArray entities = null;
//            try
//            {
//                JSONObject obj = new JSONObject(response.toString());
//            }
//            catch (JSONException e)
//            {
//                e.printStackTrace();
//            }
//        }, error ->
//        {
//            Log.d("ERROR","error => "+error.toString());
//        })
//        {
//            //this is the part that adds the data for the post request
//            @Override
//            protected Map<String,String> getParams()
//            {
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("extractors", "entities,topics");
//                params.put("text", articleHeadline);
//
//                return params;
//            }
//            //this is the part that adds the header to the request
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError
//            {
//                Map<String, String> params = new HashMap<>();
//                params.put("x-textrazor-key", getResources().getString(R.string.text_razor_key));
//                return params;
//            }
//        };
//
//        redditQueue.add(redditJsonRequest);

        //curl -X POST -H "x-textrazor-key: c6cb312b88076c6ccbdf122ee33b43281ed985497f72d6debfe76283"
        // -d "extractors=entities,topics" -d "text=Spain's stricken Bankia expects to sell off its vast portfolio of industrial
        //holdings that includes a stake in the parent company of British Airways and Iberia." https://api.textrazor.com

        trendsEntities = new ArrayList<>();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    TextRazor client = new TextRazor(getResources().getString(R.string.text_razor_key));

                    client.addExtractor("topics");
                    client.addExtractor("entities");

                    AnalyzedText response = null;

                    try
                    {
                        response = client.analyze("LONDON what is the time Canada space BBC hip hop ballet");

                        String tvText = "";

                        try
                        {
                            for(int i = 0; i < 20; i++)
                            {
                                Trend trend = new Trend();
                                trend.setTitle(response.getResponse().getEntities().get(i).getEntityId());
                                trendsEntities.add(trend);

                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        trendsRecyclerView1 = findViewById(R.id.trends_recycler_view);

                                        trendsRecyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                        adapter = new TrendsAdapter(SearchActivity.this, trendsEntities);
                                        trendsRecyclerView1.setAdapter(adapter);
                                    }
                                });
                            }
                        }
                        catch(IndexOutOfBoundsException | NullPointerException e)
                        {
                            Log.i("index out of bounds", "List emptied");
                        }

//                        TextView tv = findViewById(R.id.textView3);
//                        tv.setText(tvText);
                    } catch (NetworkException | AnalysisException e)
                    {
                        e.printStackTrace();
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        speechButton = findViewById(R.id.speech_button);
        searchView = findViewById(R.id.search_view);

        speechButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    //informs recognizer which model to refer (free form)
                    speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                    speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech to text search");

                    //RECOGNIZER_RESULT will be used to confirm later on
                    startActivityForResult(speechIntent, RECOGNIZER_RESULT);
                }
                catch(ActivityNotFoundException e)
                {
                    Toast.makeText(SearchActivity.this, "Your device does not support speech to text.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        searchView.setQuery("", true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            final Intent languageIntent = getIntent();
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                while(query.contains("&"))
                {
                    query = query.substring(0, query.indexOf("&")) + " " + query.substring(query.indexOf("&") + 1);
                }
                while(query.contains("#"))
                {
                    query = query.substring(0, query.indexOf("#")) + " " + query.substring(query.indexOf("#") + 1);
                }
                if (languageIntent.hasExtra("com.example.newsbear2.LANGUAGE3"))
                {
                    query += languageIntent.getStringExtra("com.example.newsbear2.LANGUAGE3");
                }

                Intent searchIntent = new Intent(SearchActivity.this, GoogleFactCheckResponse.class);
                searchIntent.putExtra("com.example.newsbear2.QUERY", query);
                searchIntent.putExtra("com.example.newsbear2.MAX_NUM", maxNumOfClaims);

                SearchActivity.this.startActivity(searchIntent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK && data != null)
        {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            searchView.setQuery(matches.get(0), true);
        }
    }
}