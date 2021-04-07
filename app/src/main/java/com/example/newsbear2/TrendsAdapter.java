package com.example.newsbear2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsbear2.activities.GoogleFactCheckResponse;
import com.example.newsbear2.activities.SearchActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrendsAdapter extends RecyclerView.Adapter<TrendsAdapter.ViewHolder>
{
    //adapter variables needed to change contents of recycler view
    private Context context;
    private LayoutInflater inflater;
    private List<Trend> trends;

    public TrendsAdapter(Context parentContext, List<Trend> trends) //gets contents from GoogleFactCheckResponse.class
    {
        context = parentContext;
        this.inflater = LayoutInflater.from(context);
        this.trends = trends;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) //inflates the view
    {
        View view = inflater.inflate(R.layout.custom_trend_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendsAdapter.ViewHolder holder, int position) //binds views to items in trends
    {
        holder.suggestion.setText(trends.get(position).getTitle());

        holder.imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent searchIntent = new Intent(context, SearchActivity.class);
                searchIntent.putExtra("com.example.newsbear2.QUERY", trends.get(position).getTitle());

                context.startActivity(searchIntent);
            }
        });
    }

    @Override
    public int getItemCount() //returns the number of items
    {
        int count;
        if(trends != null && !trends.isEmpty())
        {
            count= trends.size();
        }
        else
        {
            count = 0;
        }
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder //holds views from xml so that they can be bound
    {
        TextView suggestion;
        ImageView imageView;

        public ViewHolder(View itemView)
        {
            super(itemView);

            suggestion = itemView.findViewById(R.id.title_text_view);
            imageView = itemView.findViewById(R.id.imageView6);
        }
    }
}
