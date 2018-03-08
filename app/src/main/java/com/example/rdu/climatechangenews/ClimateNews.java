package com.example.rdu.climatechangenews;

/**
 * Created by Rdu on 13.10.2017.
 */

public class ClimateNews {

    //website of the current climate news
    private String mUrl;

    //title of the news article
    private String mTitle;

    //author of the news article
    private String mAuthor;

    //section of the news article
    private String mSection;

    //date of the news article
    private String mDate;

    //time of the news article
    private String mTime;

    //constructor with the parameters explained above
    public ClimateNews(String title, String author, String section, String date, String time, String url) {
        mTitle = title;
        mAuthor = author;
        mSection = section;
        mDate = date;
        mTime = time;
        mUrl = url;
    }

    //return the title
    public String getTitle() {
        return mTitle;
    }

    //return the author
    public String getAuthor() {
        return mAuthor;
    }

    //return the section
    public String getSection() {
        return mSection;
    }

    //return date and time of the news article
    public String getDate() {
        return mDate;
    }

    //return time of the news article
    public String getTime() {
        return mTime;
    }

    //return the url
    public String getUrl() {
        return mUrl;
    }
}
