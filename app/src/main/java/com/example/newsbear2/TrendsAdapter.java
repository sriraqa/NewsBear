package com.example.newsbear2;

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

import com.squareup.picasso.Picasso;

import java.util.List;

public class TrendsAdapter extends RecyclerView.Adapter<TrendsAdapter.ViewHolder>
{
    private Context context;
    private LayoutInflater inflater;
    private List<Trend> trends;
    private String newsEmoji = "\uD83D\uDCF0";
    private String clipEmoji = "\uD83D\uDCCE";
    private String faceEmoji = "\uD83D\uDE36";
    private String calendarEmoji = "\uD83D\uDCC5";


    public TrendsAdapter(Context parentContext, List<Trend> trends)
    {
        Log.i("Checkpoint", "trends adapter checkpoint"); //did not check
        context = parentContext;
        this.inflater = LayoutInflater.from(context);
        this.trends = trends;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.custom_trend_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendsAdapter.ViewHolder holder, int position)
    {
        LinearLayout layout = holder.linearLayout;
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) layout.getLayoutParams();
        params.height = (int) trends.get(position).getImageHeight();
        params.width = (int) trends.get(position).getImageWidth();
        Log.i("converted height", Float.toString(trends.get(position).getImageHeight()));
        Log.i("converted width", Float.toString(trends.get(position).getImageWidth()));
        layout.setLayoutParams(params);

        if(trends.get(position).getRatingDescription().startsWith("f"))
        {
            faceEmoji = "\uD83D\uDE2D ";
            trends.get(position).setRatingDescription(trends.get(position).getRatingDescription().substring(1));
        }
        else if(trends.get(position).getRatingDescription().startsWith("t"))
        {
            faceEmoji = "\uD83D\uDE0A ";
            trends.get(position).setRatingDescription(trends.get(position).getRatingDescription().substring(1));
        }

        holder.trendName.setText(trends.get(position).getTrendString());
        holder.claimTitle.setText(trends.get(position).getTitle());
        //holder.website.setText(claims.get(position).getWebsite());
        holder.ratingDescription.setText(Html.fromHtml(trends.get(position).getRatingDescription()));
        holder.claimDate.setText(trends.get(position).getClaimDate());
        Picasso.get().load(trends.get(position).getImageURL()).into(holder.articleImage);

        holder.titleEmoji.setText(clipEmoji);
        holder.ratingEmoji.setText(faceEmoji);
        holder.dateEmoji.setText(calendarEmoji);

        holder.fullArticleButton.setText(newsEmoji + " Read the full article");

        holder.fullArticleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(trends.get(position).getWebsite()));
                context.startActivity(browserIntent);
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT,  trends.get(position).getRatingDescription().substring(0, trends.get(position).getRatingDescription().indexOf("<"))  +
                        trends.get(position).getRatingDescription().substring(trends.get(position).getRatingDescription().indexOf(">") + 1,
                                trends.get(position).getRatingDescription().indexOf("<", trends.get(position).getRatingDescription().indexOf(">"))) +
                        ":\n" + trends.get(position).getWebsite() + "\n\nI found this fact checked article by using \"NewsBear\" on Android!");
                shareIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(shareIntent, "Send To"));
            }
        });
    }

    @Override
    public int getItemCount()
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

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView claimTitle, ratingDescription, claimDate, dateEmoji, ratingEmoji, titleEmoji, trendName;
        ImageView articleImage;
        Button fullArticleButton;
        ImageButton share;
        LinearLayout linearLayout;

        public ViewHolder(/*@NonNull*/ View itemView)
        {
            super(itemView);

            claimTitle = itemView.findViewById(R.id.title_text_view);
            //website = itemView.findViewById(R.id.claim_url);
            ratingDescription = itemView.findViewById(R.id.review_text_view);
            claimDate = itemView.findViewById(R.id.date_text_view);
            articleImage = itemView.findViewById(R.id.image_view);
            fullArticleButton = itemView.findViewById(R.id.button);
            share = itemView.findViewById(R.id.share_image_button);
            titleEmoji = itemView.findViewById(R.id.compactTitleEmoji);
            ratingEmoji = itemView.findViewById(R.id.compactReviewEmoji);
            dateEmoji = itemView.findViewById(R.id.compactDateEmoji);
            trendName = itemView.findViewById(R.id.trendName);
            linearLayout = itemView.findViewById(R.id.linearLayout2);
        }
    }
}