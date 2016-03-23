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

public class DetailFragment extends Fragment {
    TextView year, vote, duration, synopsis, title, line1, line2, reviewsTV, trailersTV;
    ImageView poster;
    String movieId, yeard, voted, durationd, synopsisd, titled;
    ListView trailersLV, reviewsLV;
    String[] trailerPaths = new String[5];
    String[] urls = new String[5];
    ArrayAdapter<String> trailersAdapter, reviewsAdapter;
    ListView lv;
    String posterPath;
    String[] Z, X;
    Button bFav;
    JSONObject JO = null;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            try {
                JO = new JSONObject(arguments.getString("testExtra"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        line1 = (TextView) rootView.findViewById(R.id.line1);
        line2 = (TextView) rootView.findViewById(R.id.line2);
        reviewsTV = (TextView) rootView.findViewById(R.id.reviews_tv);
        trailersTV = (TextView) rootView.findViewById(R.id.trailers_tv);
        bFav = (Button) rootView.findViewById(R.id.favortie);

        if (getActivity().getIntent().getStringExtra("testExtra") == null) {
            //In MainActivity (Tablet)
            if (MainActivity.fLaunch) {

                MainActivity.fLaunch = false;
                return rootView;

            }
        } else {

            try {
                JO = new JSONObject(getActivity().getIntent().getExtras().getString("testExtra"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        line1.setText("_______________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________");
        line2.setText("_______________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________");
        reviewsTV.setText("Reviews:");
        trailersTV.setText("Trailers:");
        bFav.setVisibility(View.VISIBLE);

        trailersLV = (ListView) rootView.findViewById(R.id.listview_trailers);
        reviewsLV = (ListView) rootView.findViewById(R.id.listview_reviews);


        bFav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatabaseHandler dbHandler = new DatabaseHandler(getContext());
                dbHandler.addFavorite(movieId, titled, yeard, posterPath, voted, synopsisd);
                Toast.makeText(getContext(), "Movie Added to Favorites", Toast.LENGTH_SHORT).show();
            }
        });

        year = (TextView) rootView.findViewById(R.id.year);
        vote = (TextView) rootView.findViewById(R.id.rate);
        duration = (TextView) rootView.findViewById(R.id.duration);
        synopsis = (TextView) rootView.findViewById(R.id.synopsis);
        title = (TextView) rootView.findViewById(R.id.title);
        poster = (ImageView) rootView.findViewById(R.id.poster);
        try {


            vote.setText(JO.getString("vote_average") + "/10");
            voted = JO.getString("vote_average") + "/10";
            synopsis.setText(JO.getString("overview"));
            title.setText(JO.getString("title"));
            titled = title.getText().toString();
            posterPath = JO.getString("poster_path");
            yeard = (JO.getString("release_date")).substring(0, 4);
            year.setText(yeard);
            voted = JO.getString("vote_average");
            synopsisd = JO.getString("overview");
            durationd = "120min";
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w342/" + JO.getString("poster_path")).into(poster);

            movieId = JO.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

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