package com.example.newsbear2;

import java.util.List;

public class Claim
{
    private String title;
    private String ratingDescription;
    private String website;
    private String description;
//    private String[] reviewsArray;

    //ClaimReview claimReview = new ClaimReview();
    public Claim() {}
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
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
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