package com.example.android.movieapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by XPS on 17/10/2015.
 */
public class FetchDurationTask extends AsyncTask<String, Void, String> {

    private final String LOG_TAG = FetchDurationTask.class.getSimpleName();

    @Override
    protected String doInBackground(String... params) {


        String keyQuery = params[0];
        String movieId = params[1];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String durationJsonStr = null;

        try {
            final String MOVIE_BASE_URL =
                    "http://api.themoviedb.org/3/movie/" + movieId;
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
            durationJsonStr = buffer.toString();

            JSONObject jo = new JSONObject(durationJsonStr);
            durationJsonStr = jo.getString("runtime");


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
        return durationJsonStr;
    }
}