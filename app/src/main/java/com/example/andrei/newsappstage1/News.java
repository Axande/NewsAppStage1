package com.example.andrei.newsappstage1;

/**
 * Created by Andrei on 13.04.2018.
 */

public class News {

    private String date = "";
    private String title = "";
    private String webUrl = "";

    public News(){

    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getWebUrl(){
        return webUrl;
    }

    public void setWebUrl(String webUrl){
        this.webUrl = webUrl;
    }

}
