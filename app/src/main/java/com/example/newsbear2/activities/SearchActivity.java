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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsbear2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.newsbear2.Trend;
import com.example.newsbear2.TrendsAdapter;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.TextRazor;
import com.textrazor.annotations.AnalyzedText;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private String trendingHeadlines;

    private int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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

        try
        {
            //check so that only calls News API if Activity process is stopped
            savedInstanceState.getInt("check");
            Log.i("CHECK", "skipped first time");
        }
        catch(Exception e)
        {
            NewsApiClient newsApiClient = new NewsApiClient(getResources().getString(R.string.google_news_key));

            newsApiClient.getTopHeadlines(
                    new TopHeadlinesRequest.Builder()
                            .q("news")
                            .language("en")
                            .pageSize(15)
                            .build(),
                    new NewsApiClient.ArticlesResponseCallback()
                    {
                        @Override
                        public void onSuccess(ArticleResponse response)
                        {
                            trendingHeadlines = "";

                            for(int i = 0; i < 15; i++)
                            {
                                trendingHeadlines += (response.getArticles().get(i).getTitle() + " ");
                            }

                            Log.i("STRING", trendingHeadlines);

                            //curl -X POST -H "x-textrazor-key: KEY"
                            // -d "extractors=entities,topics" -d "text=Spain's stricken Bankia expects to sell off its vast portfolio of industrial
                            //holdings that includes a stake in the parent company of British Airways and Iberia." https://api.textrazor.com

                            trendsEntities = new ArrayList<>();

                            String finalTrendingHeadlines = trendingHeadlines;

                            Log.i("trending headlines", finalTrendingHeadlines);

                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run()
                                {
                                    try
                                    {
                                        TextRazor client = new TextRazor(getResources().getString(R.string.text_razor_key));

                                        client.addExtractor("entities");

                                        AnalyzedText response = null;

                                        try
                                        {
                                            response = client.analyze(finalTrendingHeadlines);
                                            String[] trendsList = new String[20];

                                            try
                                            {
                                                for(int i = 0; i < 20; i++)
                                                {
                                                    Trend trend = new Trend();
                                                    trendsList[i] = response.getResponse().getEntities().get(i).getEntityId();
                                                    trend.setTitle(trendsList[i]);
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
                        }

                        @Override
                        public void onFailure(Throwable throwable)
                        {
                            Log.i("news error", throwable.getMessage());
                        }
                    }
            );
        }

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

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("stringResponse", trendingHeadlines);
        outState.putInt("check", check);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        //does not call News API again when the orientation, language, or text size of the phone are changed
        //(only have limited calls to News API)

        super.onRestoreInstanceState(savedInstanceState);
        trendingHeadlines = savedInstanceState.getString("stringResponse");

        trendsEntities = new ArrayList<>();

        Log.i("trending headlines", trendingHeadlines);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    TextRazor client = new TextRazor(getResources().getString(R.string.text_razor_key));

                    client.addExtractor("entities");

                    AnalyzedText response = null;

                    try
                    {
                        response = client.analyze(trendingHeadlines);
                        String[] trendsList = new String[20];

                        try
                        {
                            for(int i = 0; i < 20; i++)
                            {
                                Trend trend = new Trend();
                                trendsList[i] = response.getResponse().getEntities().get(i).getEntityId();

                                int count = 0;

                                for(int j = 0; j < trendsList.length; j++)
                                {
                                    if(trendsList[i].equals(trendsList[j]))
                                    {
                                        count++;
                                    }
                                }
                                if(count == 1)
                                {
                                    trend.setTitle(trendsList[i]);
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
                        }
                        catch(IndexOutOfBoundsException | NullPointerException e)
                        {
                            Log.i("index out of bounds", "List emptied");
                        }

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
    }
}