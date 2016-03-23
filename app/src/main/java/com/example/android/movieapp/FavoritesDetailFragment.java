package com.example.android.movieapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieapp.library.DatabaseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class FavoritesDetailFragment extends Fragment {
    TextView year, vote, duration, synopsis, title, line1, line2, reviewsTV, trailersTV;
    ImageView poster;
    String movieId, durationd;
    ListView trailersLV, reviewsLV;
    ArrayAdapter<String> trailersAdapter;
    String[] Z;
    Button bFav;

    public FavoritesDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites_detail, container, false);

        line1 = (TextView) rootView.findViewById(R.id.line1);
        line2 = (TextView) rootView.findViewById(R.id.line2);
        reviewsTV = (TextView) rootView.findViewById(R.id.reviews_tv);
        trailersTV = (TextView) rootView.findViewById(R.id.trailers_tv);
        bFav = (Button) rootView.findViewById(R.id.favortie);
        trailersLV = (ListView) rootView.findViewById(R.id.listview_trailers);
        reviewsLV = (ListView) rootView.findViewById(R.id.listview_reviews);
        year = (TextView) rootView.findViewById(R.id.year);
        vote = (TextView) rootView.findViewById(R.id.rate);
        duration = (TextView) rootView.findViewById(R.id.duration);
        synopsis = (TextView) rootView.findViewById(R.id.synopsis);
        title = (TextView) rootView.findViewById(R.id.title);
        poster = (ImageView) rootView.findViewById(R.id.poster);


        //tablet case
        if (FavoritesActivity.mTwoPane) {

            if (FavoritesActivity.fLaunch) {

                FavoritesActivity.fLaunch = false;
                return rootView;


            }
            Bundle arguments = getArguments();
            if (arguments == null)
                return rootView;
            movieId = arguments.getString("testExtra");

        }
        //phone case..lol
        else {

            movieId = getActivity().getIntent().getExtras().getString("testExtra");

        }
        //common case


        DatabaseHandler dbHandler = new DatabaseHandler(getContext());
        String[] movie = dbHandler.getMovieByID(movieId);


        line1.setText("_______________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________");
        line2.setText("_______________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________");
        reviewsTV.setText("Reviews:");
        trailersTV.setText("Trailers:");
        bFav.setVisibility(View.VISIBLE);
        bFav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getContext(), "This feature is not yet implemented :)", Toast.LENGTH_SHORT).show();
            }
        });

        title.setText(movie[1]);
        year.setText(movie[2]);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w342/" + movie[3]).into(poster);
        vote.setText(movie[4] + "/10");
        synopsis.setText(movie[5]);

        String keyQuery = getResources().getString(R.string.api_key);
        FetchDurationTask fd = new FetchDurationTask();
        fd.execute(keyQuery, movieId);
        try {
            durationd = fd.get();
            duration.setText(durationd + "min");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        FetchTrailersTask ft = new FetchTrailersTask();
        ft.execute(keyQuery, movieId);

        try {

            final String[] Y = ft.get();
            Z = removeNull(Y);
            JSONObject jo;
            String[] trailerName = new String[Z.length];
            final String[] trailerKey = new String[Z.length];
            for (int i = 0; i < Z.length; i++) {

                jo = new JSONObject(Z[i]);
                trailerName[i] = jo.getString("name");
                trailerKey[i] = jo.getString("key");


            }
            trailersAdapter = new ArrayAdapter<String>(getContext(),
                    R.layout.list_item_trailer, R.id.trailers_tv, trailerName);
            trailersLV.setAdapter(trailersAdapter);
            trailersLV.postDelayed(new Runnable() {
                public void run() {
                    Utility.setListViewHeightBasedOnChildren(trailersLV);
                }
            }, 400);

            trailersLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailerKey[position])));

                }

            });


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FetchReviewsTask FR = new FetchReviewsTask();
        FR.execute(keyQuery, movieId);


        final ListView list = (ListView) rootView.findViewById(R.id.listview_reviews);

        ReviewsAdapter adapter = null;
        try {
            adapter = new ReviewsAdapter(getContext(), FR.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        list.setAdapter(adapter);

        list.postDelayed(new Runnable() {
            public void run() {
                Utility.setListViewHeightBasedOnChildren(list);
            }
        }, 400);


        return rootView;
    }

    public String[] removeNull(String[] a) {
        ArrayList<String> removed = new ArrayList<String>();
        for (String str : a)
            if (str != null)
                removed.add(str);
        return removed.toArray(new String[0]);
    }

}
