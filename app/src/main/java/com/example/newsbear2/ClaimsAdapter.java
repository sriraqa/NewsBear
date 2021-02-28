package com.example.newsbear2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ClaimsAdapter extends RecyclerView.Adapter<ClaimsAdapter.ViewHolder>
{
    LayoutInflater inflater;
    List<Claim> claims;

    public ClaimsAdapter(Context context, List<Claim> claims)
    {
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
        //bind the data
        holder.claimTitle.setText(claims.get(position).getTitle());
        holder.claimant.setText(claims.get(position).getClaimant());
        holder.claimDate.setText(claims.get(position).getClaimDate());
        //holder.textualRating.setText(claims.get(position).getText());
    }

    @Override
    public int getItemCount()
    {
        return claims.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView claimTitle, claimDate, claimant;
//        ImageView articleImage;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            claimTitle = itemView.findViewById(R.id.claim_title);
            claimDate = itemView.findViewById(R.id.claim_description);
            claimant = itemView.findViewById(R.id.claim_url);
            //textualRating = itemView.findViewById(R.id.textual_rating);
//            articleImage = itemView.findViewById(R.id.claim_image);
        }
    }
}
