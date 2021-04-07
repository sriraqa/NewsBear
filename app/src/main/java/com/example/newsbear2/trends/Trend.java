package com.example.newsbear2.trends;

import java.util.List;

public class Trend
{
    //variables that will be changed for each item (set and get)
    private String title;

    //trend object
    public Trend() {}

    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
}
