package com.example.newsbear2.claims;

import android.content.ActivityNotFoundException;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsbear2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ClaimsAdapter extends RecyclerView.Adapter<ClaimsAdapter.ViewHolder>
{
    //variables used for adapting the claim
    private final Context context;
    private final LayoutInflater inflater;
    private final List<Claim> claims;

    //emojis in unicode
    private String newsEmoji = "\uD83D\uDCF0";
    private String clipEmoji = "\uD83D\uDCCE";
    private String faceEmoji = "\uD83D\uDE36";
    private String paperEmoji = "\uD83D\uDCDD";
    private String calendarEmoji = "\uD83D\uDCC5";

    public ClaimsAdapter(Context parentContext, List<Claim> claims) //sets contents from GoogleFactCheckResponse.class
    {
        context = parentContext;
        this.inflater = LayoutInflater.from(context);
        this.claims = claims;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) //inflate the view
    {
        View view = inflater.inflate(R.layout.custom_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) //binds the views to given values
    {
        //setting size of layout that holds the image
        LinearLayout layout = holder.linearLayout;
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) layout.getLayoutParams();
        params.height = (int) claims.get(position).getImageHeight();
        params.width = (int) claims.get(position).getImageWidth();
        Log.i("converted height", Float.toString(claims.get(position).getImageHeight()));
        Log.i("converted width", Float.toString(claims.get(position).getImageWidth()));
        layout.setLayoutParams(params);
        //gets image URL and loads it into the image view
        String imageURL = claims.get(position).getImageURL();
        Log.i("CHECK", imageURL);
        Picasso.get().load(imageURL).into(holder.articleImage);

        //checks the flag at the beginning and sets crying or happy emoji (otherwise neutral emoji face)
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
        holder.ratingDescription.setText(Html.fromHtml(claims.get(position).getRatingDescription()));
        holder.description.setText(claims.get(position).getDescription());
        holder.claimDate.setText(claims.get(position).getClaimDate());
        //set emojis
        holder.titleEmoji.setText(clipEmoji);
        holder.ratingEmoji.setText(faceEmoji);
        holder.descriptionEmoji.setText(paperEmoji);
        holder.dateEmoji.setText(calendarEmoji);

        //listens for full article button to be clicked and leads to website in browser
        String fullArticleText = newsEmoji + " Read the full article";
        holder.fullArticleButton.setText(fullArticleText);
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

        //listens for share button to be clicked and gives options to the user of other apps
        holder.share.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                //text displayed when shared
                shareIntent.putExtra(Intent.EXTRA_TEXT,  claims.get(position).getRatingDescription().substring(0, claims.get(position).getRatingDescription().indexOf("<"))  +
                        claims.get(position).getRatingDescription().substring(claims.get(position).getRatingDescription().indexOf(">") + 1,
                                claims.get(position).getRatingDescription().indexOf("<", claims.get(position).getRatingDescription().indexOf(">"))) +
                        "\": " + claims.get(position).getWebsite() + "\n\nFind more of these fact checked articles by using \"NewsBear\" on Android!");
                shareIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(shareIntent, "Send To"));
            }
        });

        //listens for report button to be clicked and gives options to report to the NewsBear email
        holder.report.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //email recipient, subject, and body
                String mailto = "mailto:newsbear2021@gmail.com" +
                        "?cc=" +
                        "&subject=" + Uri.encode("Reporting an Article from NewsBear") +
                        "&body=" + Uri.encode("I would like to report an article with the title \"" + claims.get(position).getTitle()) + "\"" +
                        "\n\n[Please explain the reason why you wish to report and try to be as specific as possible. Thank you for your feedback.]";
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mailto));
                try //in case the user does not have an email app on their phone
                {
                    context.startActivity(emailIntent);
                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(context, "Error in opening email app", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() //returns number of claims
    {
        int count;
        if(claims != null && !claims.isEmpty())
        {
            count= claims.size();
        }
        else
        {
            count = 0;
        }
        return count;
    }


    public class ViewHolder extends RecyclerView.ViewHolder //holds views from xml so that they can be bound
    {
        TextView claimTitle, ratingDescription, description, claimDate, titleEmoji, ratingEmoji, descriptionEmoji, dateEmoji;
        ImageView articleImage;
        Button fullArticleButton;
        ImageButton share, report;
        LinearLayout linearLayout;

        public ViewHolder(/*@NonNull*/ View itemView)
        {
            super(itemView);

            claimTitle = itemView.findViewById(R.id.claim_title);
            ratingDescription = itemView.findViewById(R.id.textual_rating);
            description = itemView.findViewById(R.id.claim_description);
            claimDate = itemView.findViewById(R.id.claim_date);
            articleImage = itemView.findViewById(R.id.claim_image);
            fullArticleButton = itemView.findViewById(R.id.full_article_button);
            share = itemView.findViewById(R.id.share_button);
            report = itemView.findViewById(R.id.report_button);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            titleEmoji = itemView.findViewById(R.id.titleEmoji);
            ratingEmoji = itemView.findViewById(R.id.ratingEmoji);
            descriptionEmoji = itemView.findViewById(R.id.descriptionEmoji);
            dateEmoji = itemView.findViewById(R.id.dateEmoji);
        }
    }
}
