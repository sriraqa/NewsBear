package com.example.newsbear2.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsbear2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.newsbear2.trends.Trend;
import com.example.newsbear2.trends.TrendsAdapter;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.TextRazor;
import com.textrazor.annotations.AnalyzedText;

public class SearchActivity extends AppCompatActivity
{
    //layout variables
    ImageView speechButton;
    SearchView searchView;
    ImageView settingButton;

    //variables for APIs
    public static String maxNumOfClaims = "5";
    public static String language = "&languageCode=en-US";
    private static final int RECOGNIZER_RESULT = 1;

    //variables for setting trends
    private List<Trend> trendsEntities;
    private TrendsAdapter adapter;
    private RecyclerView trendsRecyclerView1;
    private String trendingHeadlines;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setTitle("NewsBear");

        //gets random num for question
        int random = (int) (Math.random() * 4);
        String text;
        TextView questionTextView = findViewById(R.id.questionTextView);

        Intent doneIntent = getIntent();

        //checks if settings were changed in SettingsActivity.java. If not, looks for previously set settings.
        //If there were no previously set settings, goes to default
        if(doneIntent.hasExtra("com.example.newsbear2.LANGUAGE")) //language setting
        {
            language = doneIntent.getStringExtra("com.example.newsbear2.LANGUAGE");
            writeString(SearchActivity.this, "language", language);
            writeString(SearchActivity.this, "check", "1");
            Log.i("CHECK", "language first time" + language);
        }
        else
        {
            try
            {
                if(readString(SearchActivity.this, "check").equals("1"))
                {
                    language = readString(SearchActivity.this, "language");
                    Log.i("CHECK", "language remembered" + language);
                }
                else
                {
                    writeString(SearchActivity.this, "language", "&languageCode=en-US");
                    language = readString(SearchActivity.this, "language");
                    Log.i("CHECK", "language default");
                }
            }
            catch(Exception e)
            {
                writeString(SearchActivity.this, "language", "&languageCode=en-US");
                language = readString(SearchActivity.this, "language");
                Log.i("CHECK", "language default");
            }
        }
        if(doneIntent.hasExtra("com.example.newsbear2.MAXNUM")) //max num of articles setting
        {
            maxNumOfClaims = doneIntent.getStringExtra("com.example.newsbear2.MAXNUM");
            writeString(SearchActivity.this, "maxNum", maxNumOfClaims);
            writeString(SearchActivity.this, "check", "1");
            Log.i("CHECK", "max num first time" + maxNumOfClaims);
        }
        else
        {
            try
            {
                if(readString(SearchActivity.this, "check").equals("1"))
                {
                    maxNumOfClaims = readString(SearchActivity.this, "maxNum");
                    Log.i("CHECK", "max num remembered" + maxNumOfClaims);
                }
                else
                {
                    writeString(SearchActivity.this, "maxNum", "5");
                    maxNumOfClaims = readString(SearchActivity.this, "maxNum");
                    Log.i("CHECK", "max num default");
                }
            }
            catch(Exception e)
            {
                writeString(SearchActivity.this, "maxNum", "5");
                maxNumOfClaims = readString(SearchActivity.this, "maxNum");
                Log.i("CHECK", "max num default");
            }
        }

        //checks if query was received from TrendsAdapter.java
        if(doneIntent.hasExtra("com.example.newsbear2.QUERY"))
        {
            String trendingQuery = doneIntent.getStringExtra("com.example.newsbear2.QUERY");

            //makes sure that query does not have & or # since it causes error
            while(trendingQuery.contains("&"))
            {
                trendingQuery = trendingQuery.substring(0, trendingQuery.indexOf("&")) + " " + trendingQuery.substring(trendingQuery.indexOf("&") + 1);
            }
            while(trendingQuery.contains("#"))
            {
                trendingQuery = trendingQuery.substring(0, trendingQuery.indexOf("#")) + " " + trendingQuery.substring(trendingQuery.indexOf("#") + 1);
            }

            trendingQuery += language; //adds language to query so API call is correct

            Intent searchIntent = new Intent(SearchActivity.this, GoogleFactCheckResponse.class); //go to results
            searchIntent.putExtra("com.example.newsbear2.QUERY", trendingQuery);
            searchIntent.putExtra("com.example.newsbear2.MAX_NUM", maxNumOfClaims);

            SearchActivity.this.startActivity(searchIntent);
        }

        //displays random question
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

        //get current date
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat curFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = curFormatter.format(d);

        //old date that was stored
        String oldDate = readString(this, "oldDate");

        int difference;

        //checks if the new date is more than 24 hours from the old date
        if(Integer.parseInt(formattedDate.substring(0, 4)) - Integer.parseInt(oldDate.substring(0, 4)) == 0)
        {
            if(Integer.parseInt(formattedDate.substring(5, 7)) - Integer.parseInt(oldDate.substring(5, 7)) == 0)
            {
                if(Integer.parseInt(formattedDate.substring(8)) - Integer.parseInt(oldDate.substring(8)) == 0)
                {
                    difference = 0;
                }
                else
                {
                    difference = 1;
                }
            }
            else
            {
                difference = 1;
            }
        }
        else
        {
            difference = 1;
        }

        //if date is more than 24 hours, re-call the News API. Otherwise, just call TextRazor API
        //must be done due to limited number of calls to News API (also difficult to store TextRazor results)
        if(difference == 1)
        {
            writeString(this, "oldDate", formattedDate);

            //using News API implementation in gradle, calls API
            NewsApiClient newsApiClient = new NewsApiClient(getResources().getString(R.string.google_news_key));

            newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder() //specifications for call
                        .category("general")
                        .language("en")
                        .pageSize(15)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback()
                {
                    //checks if call was success or failure (error displayed as one from the Google News API documentation)
                    @Override
                    public void onSuccess(ArticleResponse response)
                    {
                        trendingHeadlines = "";

                        for (int i = 0; i < 5; i++)
                        {
                            trendingHeadlines += (response.getArticles().get(i).getDescription() + " ");
                        }

                        Log.i("STRING", trendingHeadlines);

                        writeString(SearchActivity.this, "trending headlines", trendingHeadlines);

                        trendsEntities = new ArrayList<>();

                        String finalTrendingHeadlines = trendingHeadlines; //final list of headlines

                        Log.i("trending headlines", finalTrendingHeadlines);

                        //separate thread to run TextRazor API in background
                        Thread thread = new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try //in case of any errors (listed in TextRazor documentation), will print in log and still run smoothly
                                {
                                    //using TextRazor implementation in gradle, calls API
                                    TextRazor client = new TextRazor(getResources().getString(R.string.text_razor_key));

                                    client.addExtractor("entities");

                                    AnalyzedText response = null;

                                    try //tries to get a response, otherwise prints out error in log
                                    {
                                        response = client.analyze(finalTrendingHeadlines);
                                        String[] trendsList = new String[20];

                                        try //continues until list is emptied (in case there aren't 20 keywords returned)
                                        {
                                            for (int i = 0; i < 20; i++) //loops through 20 keywords
                                            {
                                                Trend trend = new Trend();
                                                //puts each keyword in an array
                                                trendsList[i] = response.getResponse().getEntities().get(i).getEntityId();

                                                int count = 0; //used to count in case of repeats

                                                for(int j = 0; j < trendsList.length; j++) //checks for repeated words
                                                {
                                                    if(trendsList[i].equals(trendsList[j]))
                                                    {
                                                        count++;
                                                    }
                                                }
                                                if(count == 1) //if there are no repeats
                                                {
                                                    try //checks if the keyword is just a number (does not include if it is)
                                                    {
                                                        Integer.parseInt(trendsList[i]);
                                                    }
                                                    catch(Exception e)
                                                    {
                                                        trend.setTitle(trendsList[i]);
                                                        trendsEntities.add(trend);

                                                        //goes back to main thread to add to UI
                                                        runOnUiThread(new Runnable()
                                                        {
                                                            @Override
                                                            public void run()
                                                            {
                                                                //setting item to recycler view
                                                                trendsRecyclerView1 = findViewById(R.id.trends_recycler_view);

                                                                trendsRecyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                                                adapter = new TrendsAdapter(SearchActivity.this, trendsEntities);
                                                                trendsRecyclerView1.setAdapter(adapter);
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }
                                        catch (IndexOutOfBoundsException | NullPointerException e)
                                        {
                                            Log.i("index out of bounds", "List emptied");
                                        }
                                    }
                                    catch (NetworkException | AnalysisException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                                catch (Exception e)
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
        else
        {
            //same as above, but without calling News API
            trendsEntities = new ArrayList<>();

            trendingHeadlines = readString(this, "trending headlines");

            Log.i("trending headlines", trendingHeadlines);

            Thread thread = new Thread(new Runnable()
            {
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
                                        try
                                        {
                                            Integer.parseInt(trendsList[i]);
                                        }
                                        catch(Exception e)
                                        {
                                            trend.setTitle(trendsList[i]);
                                            trendsEntities.add(trend);

                                            runOnUiThread(new Runnable()
                                            {
                                                @Override
                                                public void run() {
                                                    trendsRecyclerView1 = findViewById(R.id.trends_recycler_view);

                                                    trendsRecyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                                    adapter = new TrendsAdapter(SearchActivity.this, trendsEntities);
                                                    trendsRecyclerView1.setAdapter(adapter);
                                                }
                                            });
                                        }
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

        //linking to xml file
        speechButton = findViewById(R.id.speech_button);
        searchView = findViewById(R.id.search_view);

        //listens for speech to text
        speechButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try //checks if device can do speech to text (usually works if already have app with speech to text)
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

        //listens for query to be searched
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                while(query.contains("&")) //makes sure that query does not have & or # since it causes error
                {
                    query = query.substring(0, query.indexOf("&")) + " " + query.substring(query.indexOf("&") + 1);
                }
                while(query.contains("#"))
                {
                    query = query.substring(0, query.indexOf("#")) + " " + query.substring(query.indexOf("#") + 1);
                }

                query += language;

                Intent searchIntent = new Intent(SearchActivity.this, GoogleFactCheckResponse.class); //go to results
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

        settingButton = findViewById(R.id.settingButton);

        //listens for settings to be clicked and goes to settings activity if it is
        settingButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent settingIntent = new Intent(SearchActivity.this, SettingsActivity.class);
                settingIntent.putExtra("com.example.newsbear2.SET_LANGUAGE", language);
                settingIntent.putExtra("com.example.newsbear2.SET_MAX_NUM", maxNumOfClaims);
                SearchActivity.this.startActivity(settingIntent);
            }
        });
    }

    //listens for user's speech using speech to text API and searches once done
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

    //stores String in app storage
    public static void writeString(Context context, final String KEY, String property)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences("Refresh Topics", context.MODE_PRIVATE).edit();
        editor.putString(KEY, property);
        editor.apply();
    }

    //reads String from app storage
    public static String readString(Context context, final String KEY)
    {
        return context.getSharedPreferences("Refresh Topics", context.MODE_PRIVATE).getString(KEY, "0000-00-00");
    }
}