package com.mts2792.fishnews;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "RecyclerViewExample";

    private List<FeedItem> feedsList;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter adapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        // Swipe Refresh Layout
//        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                onPostCreate();
//            }
//        });
//        //Make call to AsyncTask
//        new AsyncFetch().execute();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new DownloadTask();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // Google
        String url = "https://newsapi.org/v1/articles?source=google-news&sortBy=top&apiKey=d0a08a7a9d654403b3fc71e58b6a1615";

        // Reddit
//        String url = "https://newsapi.org/v1/articles?source=reddit-r-all&sortBy=latest&apiKey=d0a08a7a9d654403b3fc71e58b6a1615";

        //Latest TechCrunch
//        String url = "https://newsapi.org/v1/articles?source=techcrunch&sortBy=latest&apiKey=d0a08a7a9d654403b3fc71e58b6a1615";

        // Top TechCrunch articles
//        String url = "https://newsapi.org/v1/articles?source=techcrunch&apiKey=d0a08a7a9d654403b3fc71e58b6a1615";
        new DownloadTask().execute(url);
    }

    public class DownloadTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            final Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);

            progressBar.setVisibility(View.GONE);

            if (result == 1) {
                adapter = new MyRecyclerViewAdapter(MainActivity.this, feedsList);
                mRecyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(FeedItem item) {
                        Log.d(item.getUrl(), "URL");
//                        Toast.makeText(MainActivity.this, item.getUrl(), Toast.LENGTH_LONG).show();
                        String getUrl = item.getUrl();
                        Intent intent = new Intent(MainActivity.this, webview.class);
                        intent.setData(Uri.parse(getUrl));
                        startActivity(intent);
                    }
                });

            } else {
                Toast.makeText(MainActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray articles = response.optJSONArray("articles");
            feedsList = new ArrayList<>();
            for (int i = 0; i < articles.length(); i++) {
                JSONObject post = articles.optJSONObject(i);
                FeedItem item = new FeedItem();
                item.setTitle(post.getString("title"));
                item.setAuthor(post.optString("author"));
                item.setImage(post.optString("urlToImage"));
                item.setDescription(post.getString("description"));
                item.setUrl(post.getString("url"));
//                item.setThumbnail(post.optString("thumbnail"));
                feedsList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}