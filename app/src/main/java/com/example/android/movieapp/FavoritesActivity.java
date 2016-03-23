package com.example.android.movieapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class FavoritesActivity extends AppCompatActivity implements FavoritesFragment.Callback {

    private static final String FAVDETAILFRAGMENT_TAG = "FDFTAG";
    public static boolean mTwoPane;
    public static boolean fLaunch = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        if (findViewById(R.id.movie_detail_container) != null) {

            mTwoPane = true;

        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        if (mTwoPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, new FavoritesDetailFragment(), FAVDETAILFRAGMENT_TAG)
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String movieId) {
        Bundle args = new Bundle();
        args.putString("testExtra", movieId);

        FavoritesDetailFragment fragment = new FavoritesDetailFragment();
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_detail_container, fragment, FAVDETAILFRAGMENT_TAG)
                .commit();
    }
}