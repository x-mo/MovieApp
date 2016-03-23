package com.example.android.movieapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.movieapp.library.DatabaseHandler;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback {
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    public static boolean mTwoPane;
    public static boolean fLaunch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }


        if (mTwoPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, new DetailFragment(), "DFTAG")
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_favorites) {
            DatabaseHandler dbHandler = new DatabaseHandler(this);
            if (dbHandler.getRowCount() == 0) {
                Toast.makeText(MainActivity.this, "You don't have any Favorite movies yet.", Toast.LENGTH_SHORT).show();

            } else {

                startActivity(new Intent(this, FavoritesActivity.class));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(JSONObject JO) {


        Bundle args = new Bundle();
        args.putString("testExtra", JO.toString());

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                .commit();


    }
}
