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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_claims);

        if (getIntent().hasExtra("com.example.newsbear2.SOMETHING")) {
            TextView tv = findViewById(R.id.result_note);
            query = getIntent().getExtras().getString("com.example.newsbear2.SOMETHING");
            String text = "Search results for " + "\"" + query + "\"";
            tv.setText(text);
        }

        GOOGLE_API_URL = "https://factchecktools.googleapis.com/v1alpha1/claims:search?&query=" + query + "&languageCode=en-US&key=AIzaSyDc63dNSdT88mrDRO4lYr8lQA3WeA7FxhA";
        claims = new ArrayList<>();
        claimsRecyclerView = findViewById(R.id.claims_recycler_view);

        extractClaims();
    }

    private void extractClaims()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, GOOGLE_API_URL, null, response -> {
            try {
                JSONObject obj = new JSONObject(response.toString());
                //JSONArray claimsArray = obj.getJSONArray("claims");
                TextView test = findViewById(R.id.test);

                test.setText(obj.toString());
               // test.setText((CharSequence) claimsArray.getJSONObject(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            JSONArray claimArray = null;
//            int length = claimArray.length();
//            TextView test = findViewById(R.id.test);
            //test.setText(claimObject.toString());

//            test.setText(length);
            //                for (int i = 0; i < response.length(); i++)
//                {
//                    try
//                    {
//                        JSONObject claimObject = response.getJSONObject(i);
//                        TextView test = findViewById(R.id.test);
//                        test.setText(claimObject.toString());

//                        Claim claim = new Claim();
//                        claim.setTitle(claimObject.getString("text"));
//                        claim.setClaimant(claimObject.getString("claimant"));
//                        claim.setClaimDate(claimObject.getString("claimDate"));

//                        claims.add(claim);
//                    } catch (JSONException E)
//                    {
//                        E.printStackTrace();
//                    }
//                }

            claimsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapter = new ClaimsAdapter(getApplicationContext(), claims);
            claimsRecyclerView.setAdapter(adapter);
        }, error -> Log.d("tag", "onErrorResponse: " + error.getMessage()));

        queue.add(jsonRequest);


//        StringRequest stringRequest = new StringRequest(Request.Method.GET, GOOGLE_API_URL,
//                response -> responseTextView.setText("Response is: " + response), error -> responseTextView.setText("No Search Results!"));
//
//        queue.add(stringRequest);
    }
}
