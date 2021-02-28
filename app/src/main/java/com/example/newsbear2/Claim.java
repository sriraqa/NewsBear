package com.example.newsbear2;

import java.util.List;

public class Claim
{
    private String title;
    private String claimant;
    private String claimDate;
//    private String[] reviewsArray;

    //ClaimReview claimReview = new ClaimReview();
    public Claim() {}
    public Claim(String title, String claimant, String claimDate, String[] reviewsArray)
    {
        this.title = title;
        this.claimant = claimant;
        this.claimDate = claimDate;
//        this.reviewsArray = reviewsArray;
    }

    public String getTitle()
    {
        return title;
    }
    public void setTitle(String text)
    {
        this.title = title;
    }
    public String getClaimant()
    {
        return claimant;
    }
    public void setClaimant(String claimant)
    {
        this.claimant = claimant;
    }
    public String getClaimDate()
    {
        return claimDate;
    }
    public void setClaimDate(String claimDate)
    {
        this.claimDate = claimDate;
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
