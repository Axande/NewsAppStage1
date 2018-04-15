package com.example.andrei.newsappstage1;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Andrei on 13.04.2018.
 * <p>
 * Loader for an async task to an API
 */

public class NewsLoader extends AsyncTaskLoader<ArrayList<News>> {

    public NewsLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<News> loadInBackground() {
        String TAG = MainActivity.class.getSimpleName();            //TAG used for logs

        ArrayList<News> loadedNews = new ArrayList<>();             //an array for retrieved news
        HttpHandler sh = new HttpHandler();

        //request to URL
        String jsonString;
        try {
            jsonString = sh.makeHttpRequest(createUrl());   //connect to the API's URL
        } catch (IOException e) {                           //there was a problem retrieving data
            Log.e(TAG, "Failed to get string of data");
            return null;
        }

        if (jsonString != null) {                           //there is a string of data
            try {
                JSONObject jsonObj = new JSONObject(jsonString); //Convert the string to Json object
                JSONArray rawNews = jsonObj.getJSONObject("response").getJSONArray("results"); //get the json node with results

                //save all nodes into News objects
                for (int i = 0; i < rawNews.length(); i++) {
                    JSONObject c = rawNews.getJSONObject(i);
                    News oneEntry = new News();

                    oneEntry.setDate(formatDate(c.getString("webPublicationDate"), 1));
                    oneEntry.setTime(formatDate(c.getString("webPublicationDate"), 2));
                    oneEntry.setTitle(c.getString("webTitle"));
                    oneEntry.setWebUrl(c.getString("webUrl"));
                    oneEntry.setCategory(c.getString("sectionName"));

                    loadedNews.add(oneEntry);
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }
        return loadedNews;
    }

    /**
     * @param s    input string to be formated
     * @param part which part of the string to be returned
     * @return either date or time from a given string s
     */
    private String formatDate(String s, int part) {
        String[] parts = s.split("T");                  //the date part
        parts[1] = parts[1].substring(0, parts[1].length() - 1); //cut Z from the time part
        return parts[part - 1];
    }

    /**
     * Create an Url for API connection
     *
     * @return an URL object
     */
    private URL createUrl() {
        URL url;
        String stringUrl = "https://content.guardianapis.com/search?q=sport&section=sport&page=" + MainActivity.pageToLoad + "&page-size=30&api-key=f410ec8e-d4be-419c-b77d-dfb3d818a0d7";

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            return null;
        }
        return url;
    }
}
