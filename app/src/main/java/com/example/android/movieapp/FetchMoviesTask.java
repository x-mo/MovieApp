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

public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    @Override
    protected String[] doInBackground(String... params) {

        String[] paths = new String[21];

        String keyQuery = params[0];
        String sortQuery = params[1];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieJsonStr = null;

        try {
            final String MOVIE_BASE_URL =
                    "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, sortQuery)
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
            movieJsonStr = buffer.toString();

            JSONObject json = new JSONObject(movieJsonStr);
            JSONArray jarray = json.getJSONArray("results");

            paths[20] = jarray.toString();

            JSONObject jo;
            String imgpath;

            for (int i = 0; i < jarray.length(); i++) {
                jo = jarray.getJSONObject(i);
                imgpath = jo.getString("poster_path");
                if (jo.getString("poster_path") == null)
                    imgpath = jo.getString("backdrop_path");
                paths[i] = imgpath;
               }


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } catch (JSONException e) {
            e.printStackTrace();
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
        return paths;
    }
}