package com.example.newsbear2.claims;

public class Claim
{
    //list of variables that will be changed for each item (set and get for each one)
    private String title;
    private String ratingDescription;
    private String website;
    private String description;
    private String claimDate;
    private String imageURL;
    private float imageWidth;
    private float imageHeight;

    //object claim
    public Claim() {}

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
}
