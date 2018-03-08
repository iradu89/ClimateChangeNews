package com.example.rdu.climatechangenews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.rdu.climatechangenews.MainActivity.LOG_TAG;

/**
 * Created by Rdu on 13.10.2017.
 */

public final class QueryUtils {

    private static final String DATE_SEPARATOR = "T";

    //constructor never to be used
    private QueryUtils() {
    }

    //Returns a list of ClimateNews objects from the JSON parsing
    private static List<ClimateNews> extractClimateNewsFromJson(String climateNewsJson) {
        if (TextUtils.isEmpty(climateNewsJson)) {
            return null;
        }

        List<ClimateNews> climateNewses = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(climateNewsJson);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray climateNewsArray = response.getJSONArray("results");

            for (int i = 0; i < climateNewsArray.length(); i++) {
                JSONObject currentClimateNews = climateNewsArray.getJSONObject(i);
                //get the news title
                String title = currentClimateNews.getString("webTitle");

                //get the section name
                String section;
                if (currentClimateNews.has("sectionName")) {
                    section = currentClimateNews.getString("sectionName");
                } else {
                    section = "N/A";
                }

                //get the date and time
                String dateAndTime;
                if (currentClimateNews.has("webPublicationDate")) {
                    dateAndTime = currentClimateNews.getString("webPublicationDate");
                } else {
                    dateAndTime = "N/A";
                }

                //Split the date and time into separate strings.
                String date;
                String time;
                if (dateAndTime.contains(DATE_SEPARATOR)) {
                    String[] parts = dateAndTime.split(DATE_SEPARATOR);
                    date = parts[0];
                    time = parts[1];
                    //remove the last letter at the end of the TIME string so it only displays the digits.
                    time = time.substring(0, time.length() - 1);
                } else {
                    date = "";
                    time = "Date and time unspecified";
                }

                //get the author
                JSONObject authorNameField = currentClimateNews.getJSONObject("fields");
                String author;
                if (authorNameField.has("byline")) {
                    author = authorNameField.getString("byline");
                } else {
                    author = "N/A";
                }

                //get the web URL
                String url = currentClimateNews.getString("webUrl");

                ClimateNews climateNews = new ClimateNews(title, author, section, date, time, url);
                climateNewses.add(climateNews);

            }


        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the climate News JSON results", e);
        }

        return climateNewses;
    }

    //Query the Guardian data and return a list of ClimateNews objects.
    public static List<ClimateNews> fetchClimateNews(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        List<ClimateNews> climateNews = extractClimateNewsFromJson(jsonResponse);

        return climateNews;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";


        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the climate News JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
