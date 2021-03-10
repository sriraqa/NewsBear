package com.example.newsbear2;

import java.util.List;

public class Trend
{
    private String title;
    private String ratingDescription;
    private String website;
    private String trendString;
    private String claimDate;
    private String imageURL;
    private float imageWidth;
    private float imageHeight;
//    private String[] reviewsArray;

    //ClaimReview claimReview = new ClaimReview();
    public Trend() {}
//    public Claim(String title, String claimant, String claimDate, String[] reviewsArray)
//    {
//        this.title = title;
//        this.website = website;
//        this.ratingDescription = ratingDescription;
//        this.description = description;
//        this.reviewsArray = reviewsArray;
//    }

    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public String getWebsite()
    {
        return website;
    }
    public void setWebsite(String website)
    {
        this.website = website;
    }
    public String getRatingDescription()
    {
        return ratingDescription;
    }
    public void setRatingDescription(String ratingDescription)
    {
        this.ratingDescription = ratingDescription;
    }
    public String getTrendString()
    {
        return trendString;
    }
    public void setTrendString(String trendString)
    {
        this.trendString = trendString;
    }
    public String getClaimDate()
    {
        return claimDate;
    }

    public void setClaimDate(String claimDate)
    {
        this.claimDate = claimDate;
    }
    public String getImageURL()
    {
        return imageURL;
    }
    public void setImageURL(String imageURL)
    {
        this.imageURL = imageURL;
    }
    public float getImageHeight()
    {
        return imageHeight;
    }
    public void setImageHeight(float imageHeight)
    {
        this.imageHeight = imageHeight;
    }
    public float getImageWidth()
    {
        return imageWidth;
    }
    public void setImageWidth(float imageWidth)
    {
        this.imageWidth = imageWidth;
    }
    //    public String[] getReviewsArray()
//    {
//        return reviewsArray;
//    }

//    public void setReviewsList(List<ClaimReview> reviewsList)
//    {
//        this.reviewsArray = reviewsArray;
//    }

//    public String getUrl()
//    {
//        return claimReview.getUrl();
//    }
//    public String getTextualRating()
//    {
//        return claimReview.getTextualRating();
//    }
}
