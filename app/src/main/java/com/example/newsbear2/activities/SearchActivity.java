package com.example.newsbear2.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsbear2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity
{
    ImageView speechButton;
    SearchView searchView;
    private ViewPager viewPager;

    //public static String query = "";
    public static int maxNumOfClaims = 5;
    private static final int RECOGNIZER_RESULT = 1;
    private final String TEXT_RAZOR_URL = "https://api.textrazor.com";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String id = "id=1";

        setTitle("NewsBear");

        int random = (int) (Math.random() * 4);
        String text = "\uD83D\uDCAD ";

        TextView questionTextView = findViewById(R.id.questionTextView);

        if(random == 0)
        {
            text += "What controversial event is going on right now?";
            questionTextView.setText(text);
        }
        else if(random == 1)
        {
            text += "Which celebrity are people talking about recently?";
            questionTextView.setText(text);
        }
        else if(random == 3)
        {
            text += "What's a general topic that you're interested in learning about?";
            questionTextView.setText(text);
        }
        else
        {
            text += "What's a mainstream topic that's been on your mind?";
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

        RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);
        JSONObject postData = new JSONObject();

        try
        {
            postData.put("extractors=entities,topics", "");
            postData.put("text=" + articleHeadline, "");
            Log.i("CHECKPOINT", "data check");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        //final String requestBody = jsonBody.toString();

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, TEXT_RAZOR_URL, postData, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("CHECKPOINT", "check");

                TextView tv = findViewById(R.id.textView3);
                String tvText = response.toString();
                tv.setText(tvText);

//                JSONArray topics = null;
//                JSONArray entities = null;
//                try
//                {
//                    JSONObject obj = new JSONObject(response.toString());
//
//                    topics = obj.getJSONObject("response").getJSONArray("topics");
//                    entities = obj.getJSONObject("response").getJSONArray("entities");
//
//                    String tvText = topics.getJSONObject(0).getString("label") + " " +
//                            entities.getJSONObject(0).getString("entityId");
//
//                    TextView tv = findViewById(R.id.textView3);
//                    tv.setText(tvText);
//
//                } catch (JSONException e)
//                {
//                    e.printStackTrace();
//                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // error
                Log.e("Error", "textrazor Volley error");
            }
        })
        {
//            @Override
//            public byte[] getBody()
//            {
//                try {
//                    return requestBody == null ? null : requestBody.getBytes("utf-8");
//                } catch (UnsupportedEncodingException uee) {
//                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
//                    return null;
//                }
//            }

            //this is the part that adds the data for the post request
//            @Override
//            protected Map<String,String> getParams()
//            {
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("extractors", "entities,topics");
//                params.put("text", articleHeadline);
//
//                return params;
//            }
            //this is the part that adds the header to the request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Log.i("CHECKPOINT", "header check");
                Map<String, String> headers = new HashMap<>();
                headers.put("x-textrazor-key", getResources().getString(R.string.text_razor_key));
                return headers;
            }
        };

        queue.add(postRequest);

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
                if (languageIntent.hasExtra("com.example.newsbear2.LANGUAGE2"))
                {
                    query += languageIntent.getStringExtra("com.example.newsbear2.LANGUAGE2");
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