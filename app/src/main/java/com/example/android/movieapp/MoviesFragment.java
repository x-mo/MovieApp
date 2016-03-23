package com.example.android.movieapp;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class MoviesFragment extends Fragment {


    public interface Callback {


        public void onItemSelected(JSONObject JO);
    }

    JSONObject JO;
    private boolean firstLaunch = true;
    private String sortQuery;
    private String keyQuery;
    SharedPreferences prefs;
    GridView gridview;

    public MoviesFragment() {

    }


    @Override
    public void onResume() {
        if (!firstLaunch) {
            sortQuery = prefs.getString(getActivity().getApplicationContext().getString(R.string.order_key), getActivity().getApplicationContext().getString(R.string.pref_default_sort_order));
            B = startFetching();

            final Handler handler1 = new Handler(Looper.getMainLooper());
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (gridview.getChildCount() == 0)
                    B = startFetching();

                }
            }, 3000);
            final Handler handler2 = new Handler(Looper.getMainLooper());
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (gridview.getChildCount() == 0)
                        Toast.makeText(getActivity().getApplicationContext(), "Slow internet connection, Use the refresh button.", Toast.LENGTH_LONG).show();

                }
            }, 15000);
        }

        super.onResume();
    }


    String[] startFetching() {
        firstLaunch = false;

        FetchMoviesTask fm = new FetchMoviesTask();
        fm.execute(keyQuery, sortQuery);
        try {
            String[] A = fm.get();
            gridview.setAdapter(new ImageAdapter(getActivity(), A));
            return A;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    String[] B;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        setHasOptionsMenu(true);
        keyQuery = getResources().getString(R.string.api_key);
        gridview = (GridView) rootView.findViewById(R.id.gv);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        sortQuery = prefs.getString(getActivity().getApplicationContext().getString(R.string.order_key), getActivity().getApplicationContext().getString(R.string.pref_default_sort_order));
        B = startFetching();


        final Handler handler1 = new Handler(Looper.getMainLooper());
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (gridview.getChildCount() == 0)
                B = startFetching();

            }
        }, 3000);

        final Handler handler2 = new Handler(Looper.getMainLooper());
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (gridview.getChildCount() == 0)
                    Toast.makeText(getActivity().getApplicationContext(), "Slow internet connection, Use the refresh button.", Toast.LENGTH_LONG).show();

            }
        }, 15000);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                try {
                    JSONArray JA = new JSONArray(B[20]);
                    JO = JA.getJSONObject(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!MainActivity.mTwoPane) {

                    Intent DA = new Intent(getActivity(), DetailActivity.class);
                    DA.putExtra("testExtra", JO.toString());
                    startActivity(DA);
                } else {

                    ((Callback) getActivity()).onItemSelected(JO);

                }
            }
        });

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {

            if (gridview.getChildCount() == 0)
                B = startFetching();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}