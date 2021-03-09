package com.example.newsbear2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ClaimsAdapter extends RecyclerView.Adapter<ClaimsAdapter.ViewHolder>
{
    private Context context;
    private LayoutInflater inflater;
    private List<Claim> claims;

    public ClaimsAdapter(Context parentContext, List<Claim> claims)
    {
        context = parentContext;
        this.inflater = LayoutInflater.from(context);
        this.claims = claims;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.custom_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        String newsEmoji = "\uD83D\uDCF0";
        String faceEmoji = "\uD83D\uDE36";
        String paperEmoji = "\uD83D\uDCDD";
        String calendarEmoji = "\uD83D\uDCC5";

        if(claims.get(position).getRatingDescription().startsWith("f"))
        {
            faceEmoji = "\uD83D\uDE2D ";
            claims.get(position).setRatingDescription(claims.get(position).getRatingDescription().substring(1));
        }
        else if(claims.get(position).getRatingDescription().startsWith("t"))
        {
            faceEmoji = "\uD83D\uDE0A ";
            claims.get(position).setRatingDescription(claims.get(position).getRatingDescription().substring(1));
        }

        //bind the data
        holder.claimTitle.setText(claims.get(position).getTitle());
        //holder.website.setText(claims.get(position).getWebsite());
        holder.ratingDescription.setText(Html.fromHtml(claims.get(position).getRatingDescription()));
        holder.description.setText(claims.get(position).getDescription());
        holder.claimDate.setText(claims.get(position).getClaimDate());

        Picasso.get().load(claims.get(position).getImageURL()).into(holder.articleImage);

        holder.titleEmoji.setText(newsEmoji);
        holder.ratingEmoji.setText(faceEmoji);
        holder.descriptionEmoji.setText(paperEmoji);
        holder.dateEmoji.setText(calendarEmoji);

        holder.fullArticleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(claims.get(position).getWebsite()));
                context.startActivity(browserIntent);
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT,  claims.get(position).getRatingDescription().substring(0, claims.get(position).getRatingDescription().indexOf("<"))  +
                        claims.get(position).getRatingDescription().substring(claims.get(position).getRatingDescription().indexOf(">") + 1,
                                claims.get(position).getRatingDescription().indexOf("<", claims.get(position).getRatingDescription().indexOf(">"))) +
                        "\":\n" + claims.get(position).getWebsite() + "\n\nI found this fact checked article by using \"NewsBear\" on Android!");
                shareIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(shareIntent, "Send To"));
            }
        });

        LinearLayout layout = holder.linearLayout;
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) layout.getLayoutParams();
        params.height = (int) claims.get(position).getImageHeight();
        params.width = (int) claims.get(position).getImageWidth();
        Log.i("converted height", Float.toString(claims.get(position).getImageHeight()));
        Log.i("converted width", Float.toString(claims.get(position).getImageWidth()));
        layout.setLayoutParams(params);
    }

    @Override
    public int getItemCount()
    {
        return claims.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView claimTitle, ratingDescription, description, claimDate, titleEmoji, ratingEmoji, descriptionEmoji, dateEmoji;
        ImageView articleImage;
        Button fullArticleButton;
        ImageButton share;
        LinearLayout linearLayout;

        public ViewHolder(/*@NonNull*/ View itemView)
        {
            super(itemView);

            claimTitle = itemView.findViewById(R.id.claim_title);
            //website = itemView.findViewById(R.id.claim_url);
            ratingDescription = itemView.findViewById(R.id.textual_rating);
            description = itemView.findViewById(R.id.claim_description);
            claimDate = itemView.findViewById(R.id.claim_date);
            articleImage = itemView.findViewById(R.id.claim_image);
            fullArticleButton = itemView.findViewById(R.id.full_article_button);
            share = itemView.findViewById(R.id.share_button);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            titleEmoji = itemView.findViewById(R.id.titleEmoji);
            ratingEmoji = itemView.findViewById(R.id.ratingEmoji);
            descriptionEmoji = itemView.findViewById(R.id.descriptionEmoji);
            dateEmoji = itemView.findViewById(R.id.dateEmoji);
        }
    }
}
