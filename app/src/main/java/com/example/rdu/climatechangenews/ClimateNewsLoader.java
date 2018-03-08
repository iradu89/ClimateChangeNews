package com.example.rdu.climatechangenews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Rdu on 16.10.2017.
 */

public class ClimateNewsLoader extends AsyncTaskLoader<List<ClimateNews>> {
    //log tag used for testing
    private static final String LOG_TAG = ClimateNewsLoader.class.getName();

    private String mUrl;

    public ClimateNewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<ClimateNews> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<ClimateNews> climateNews = QueryUtils.fetchClimateNews((mUrl));
        return climateNews;
    }
}
