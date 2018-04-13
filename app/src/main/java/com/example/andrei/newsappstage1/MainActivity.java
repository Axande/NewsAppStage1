package com.example.andrei.newsappstage1;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ArrayList<News> allNewsList = new ArrayList<>();
    SwipeRefreshLayout swipeLayout;

    private String TAG = MainActivity.class.getSimpleName();

    //Details for Querry to content.guardianapis.com
    private String apiKey = "f410ec8e-d4be-419c-b77d-dfb3d818a0d7";
    private String querrySection = "sport";
    private String querry = "sport";
    private int querryPageSize = 30;
    private int querryPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeLayout = findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allNewsList.clear();
                querryPage = 1;
                new GetNews().execute();
                swipeLayout.setRefreshing(false);
            }
        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        new GetNews().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }








    private class GetNews extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            //request to URL
            String jsonString = "";
            try {
                jsonString = sh.makeHttpRequest(createUrl());
            } catch (IOException e) {
                //TODO: Handle no internet connection error
                Log.e(TAG, "Failed to get string of data");
                return null;
            }
            Log.e(TAG, "Retrieved data:" + jsonString);
            Log.e(TAG, "Url" + createUrl());
            if (jsonString != null) {
                try {
                    //Convert the string of data to Json object
                    JSONObject jsonObj = new JSONObject(jsonString);
                    //get the json node
                    JSONArray rawNews = jsonObj.getJSONObject("response").getJSONArray("results");

                    for(int i = 0; i < rawNews.length(); i++){
                        JSONObject c = rawNews.getJSONObject(i);
                        News oneEntry = new News();

                        oneEntry.setDate(c.getString("webPublicationDate"));
                        oneEntry.setTitle(c.getString("webTitle"));
                        oneEntry.setWebUrl(c.getString("webUrl"));

                        allNewsList.add(oneEntry);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server.",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        private URL createUrl() {
            URL url;
            String stringUrl = "";

            stringUrl = "https://content.guardianapis.com/search?q=" + querry + "&section=" + querrySection
                    + "&page=" + querryPage + "&page-size="+ querryPageSize+"&api-key=" + apiKey;

            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                return null;
            }
            return url;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(allNewsList != null) {
                NewsAdapter newsAdapter = new NewsAdapter(MainActivity.this, allNewsList);
                ListView newsFeed = findViewById(R.id.news_feed);
                newsFeed.setAdapter(newsAdapter);

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
            else{
                //TODO: There are no news
            }



//            ListAdapter adapter = new SimpleAdapter(MainActivity.this, pokemonList,
//                    R.layout.list_item, new String[]{"name", "id", "candy"},
//                    new int[]{R.id.name, R.id.id, R.id.candy});
//            list_view.setAdapter(adapter);
        }
    }

}
