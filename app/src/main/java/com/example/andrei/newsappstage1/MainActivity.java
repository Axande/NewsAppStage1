package com.example.andrei.newsappstage1;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>> {

    public static int pageToLoad = 0;   //which page to be requested from API
    private int currentDisplay = 1; //1 = help, 200 = list of items, 404 = error

    public ArrayList<News> allNewsList = new ArrayList<>();
    private int lastItemOnPage = 0;       //index to the element to be in focus after an update of the array

    private ConnectivityManager cm;         //object to manage connectivity
    private NetworkInfo activeNetwork;

    //all layouts that change visibility
    private LinearLayout layout_error;
    private LinearLayout layout_info;
    private ListView layout_list;
    private TextView layout_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find layouts
        layout_info = findViewById(R.id.info_wrapper);
        layout_error = findViewById(R.id.error_wrapper);
        layout_list = findViewById(R.id.news_feed);
        layout_loading = findViewById(R.id.loading_bar);

        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        //set clickers for the load and refresh images
        ImageView loadMore = findViewById(R.id.btn_load_more);
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentDisplay != 200) updateScreen(200, "");
                //add a new page at the end of already existing list of pages
                MainActivity.pageToLoad++;
                lastItemOnPage = allNewsList.size() - 1;

                activeNetwork = cm.getActiveNetworkInfo();
                //check if there is an internet connection or not
                if (!(activeNetwork != null && activeNetwork.isConnectedOrConnecting())) {
                    updateScreen(404, "No internet access! :(");
                } else {
                    //connect with the loader. use just one id because we do not
                    // want more connections to request same data multiple times
                    getSupportLoaderManager().initLoader(1, null, MainActivity.this).forceLoad();
                }
            }
        });
        ImageView refresh = findViewById(R.id.btn_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentDisplay != 200) updateScreen(200, "");
                //clear all pages and load again from page 1
                MainActivity.pageToLoad = 1;
                lastItemOnPage = 0;
                allNewsList.clear();

                activeNetwork = cm.getActiveNetworkInfo();
                //check if there is an internet connection or not
                if (!(activeNetwork != null && activeNetwork.isConnectedOrConnecting())) {
                    updateScreen(404, "No internet access! :(");
                } else {
                    getSupportLoaderManager().initLoader(1, null, MainActivity.this).forceLoad();
                }
            }
        });
    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int id, Bundle args) {
        triggerLoadingBar();
        return new NewsLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> data) {
        triggerLoadingBar();
        if (data.size() == 0) {
            //an error occurred
            updateScreen(404, "Could not correctly parse data. :(");
        } else {
            allNewsList.addAll(data);
            updateListOfItems();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
    }

    /**
     * update the list displayed on the main screen and focus on the lastItemOnPage element
     */
    private void updateListOfItems() {
        if (allNewsList != null) {
            NewsAdapter newsAdapter = new NewsAdapter(MainActivity.this, allNewsList);
            ListView newsFeed = findViewById(R.id.news_feed);
            newsFeed.setAdapter(newsAdapter);

            newsFeed.setSelectionFromTop(lastItemOnPage, 0);

            //add a click listener for the list of items and launch the intent
            newsFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                    News item = (News) parent.getItemAtPosition(position);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(item.getWebUrl()));
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * open/close the loading bar
     */
    private void triggerLoadingBar() {
        if (layout_loading.getVisibility() == View.GONE)
            layout_loading.setVisibility(View.VISIBLE);
        else
            layout_loading.setVisibility(View.GONE);
    }

    private void updateScreen(int val, String message) {
        currentDisplay = val;

        //close all tabs
        layout_info.setVisibility(View.GONE);
        layout_error.setVisibility(View.GONE);
        layout_list.setVisibility(View.GONE);

        //write message to error tab in case it is present
        if (!message.equals("")) {
            TextView tw = findViewById(R.id.error_text);
            tw.setText(message);
        }

        //open the right tab
        if (val == 404) layout_error.setVisibility(View.VISIBLE);
        else if (val == 200) layout_list.setVisibility(View.VISIBLE);
        else layout_info.setVisibility(View.VISIBLE);
    }
}
