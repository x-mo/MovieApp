package com.example.android.movieapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.movieapp.library.DatabaseHandler;

public class FavoritesFragment extends Fragment {
    public interface Callback {


        public void onItemSelected(String movieId);
    }

    String[][] couples;
    GridView gridview;
    String A[];

    public FavoritesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        gridview = (GridView) rootView.findViewById(R.id.gv);


        DatabaseHandler dbHandler = new DatabaseHandler(getContext());
        A = new String[dbHandler.getRowCount()];

        couples = dbHandler.getFavoritesList();

        for (int i = 0; i < couples.length; i++) {
            if ((couples[i][0]) == null || (couples[i][1]) == null)
                break;

            A[i] = couples[i][1];
        }

        gridview.setAdapter(new ImageAdapter(getActivity(), A));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if (!FavoritesActivity.mTwoPane) {

                    Intent FDA = new Intent(getActivity(), FavoritesDetailActivity.class);
                    FDA.putExtra("testExtra", couples[position][0]);
                    startActivity(FDA);
                } else {


                    ((Callback) getActivity()).onItemSelected(couples[position][0]);

                }
            }
        });

        Handler handler1 = new Handler(Looper.getMainLooper());
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {

                gridview.setAdapter(new ImageAdapter(getActivity(), A));
            }
        }, 3000);
        Handler handler2 = new Handler(Looper.getMainLooper());
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (gridview.getChildCount() == 0)
                    Toast.makeText(getActivity().getApplicationContext(), "Slow internet connection, Use the refresh button.", Toast.LENGTH_LONG).show();
            }
        }, 10000);
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_refresh) {

                gridview.setAdapter(new ImageAdapter(getActivity(), A));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
