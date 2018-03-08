package com.example.rdu.climatechangenews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<ClimateNews>> {

    //URL from the guardian API which has news about Climate Change
    public static final String CLIMATE_NEWS_URL = "http://content.guardianapis.com/search?show-fields=byline&order-by=newest&format=json&section=environment&q=climate%20change&api-key=test";
    //Tag for log messages
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    //global adapter for the list of ClimateNews
    private ClimateNewsAdapter mAdapter;
    //constant value for the Loader ID
    private static final int CLIMATE_NEWS_LOADER_ID = 1;
    //empty textview that appears when the list is empty
    private TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView climateListView = (ListView) findViewById(R.id.list);

        mAdapter = new ClimateNewsAdapter(this, new ArrayList<ClimateNews>());

        climateListView.setAdapter(mAdapter);

        //find the empty text view and
        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        climateListView.setEmptyView(mEmptyTextView);

        //on click listener for each list item which will send a website intent for the corresponding URL.
        climateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClimateNews currentClimateNews = mAdapter.getItem(i);

                Uri climateNewsUri = Uri.parse(currentClimateNews.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, climateNewsUri);

                if (websiteIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(websiteIntent);
                }
            }
        });


        // Get a reference which will check the internet connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the current internet connection
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Check if there is a network connection and if there is, start the loader.
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(CLIMATE_NEWS_LOADER_ID, null, this);
        } else {
            //set the text on the empty text view if there is no internet connection.
            mEmptyTextView.setText(R.string.no_internet_connection);
        }
    }


    @Override
    public Loader<List<ClimateNews>> onCreateLoader(int i, Bundle bundle) {
        return new ClimateNewsLoader(this, CLIMATE_NEWS_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<ClimateNews>> loader, List<ClimateNews> climateNews) {
        //set the text to no climate news if there is no news to display
        mEmptyTextView.setText(R.string.no_climate_news);
        //clear the adapter of previous data
        mAdapter.clear();

        //if a list of valid ClimateNews exists then update the adapter to the new data
        if (climateNews != null && !climateNews.isEmpty()) {
            mAdapter.addAll(climateNews);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<ClimateNews>> loader) {
        //clear the adapter
        mAdapter.clear();
    }
}
