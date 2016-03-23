package com.example.android.movieapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchReviewsTask extends AsyncTask<String, Void, List<Review>> {

    private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

    @Override
    protected List<Review> doInBackground(String... params) {


        String keyQuery = params[0];
        String movieId = params[1];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String reviewsJsonStr = null;

        try {
            final String MOVIE_BASE_URL =
                    "http://api.themoviedb.org/3/movie/" + movieId + "/reviews";
            final String KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(KEY_PARAM, keyQuery)
                    .build();
            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            reviewsJsonStr = buffer.toString();


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        List<Review> data = new ArrayList<Review>();
        ;
        try {
            JSONObject JO = new JSONObject(reviewsJsonStr);
            JSONArray JA = JO.getJSONArray("results");
            String tmpAuthor, tmpContent;

            data = new ArrayList<>();

            for (int i = 0; i < JO.getInt("total_results"); i++) {
                tmpAuthor = JA.getJSONObject(i).getString("author");
                tmpContent = JA.getJSONObject(i).getString("content");
                Review current = new Review(tmpAuthor, tmpContent);
                data.add(current);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

}