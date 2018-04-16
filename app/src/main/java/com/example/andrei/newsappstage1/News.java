package com.example.andrei.newsappstage1;

import android.widget.TextView;

/**
 * Created by Andrei on 13.04.2018.
 * <p>
 * A class to store all News details
 */

public class News {

    private String date = "";
    private String time = "";
    private String title = "";
    private String webUrl = "";
    private String category = "";
    private String author = "";

    public News() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
