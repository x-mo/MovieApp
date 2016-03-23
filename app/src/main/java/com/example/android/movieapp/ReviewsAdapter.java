package com.example.android.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by XPS on 18/10/2015.
 */
public class ReviewsAdapter extends BaseAdapter{
    private Context context;

    private List<Review> listReviews;

    public ReviewsAdapter(Context context, List<Review> listReviews) {
        this.context = context;
        this.listReviews = listReviews;
    }

    public int getCount() {
        return listReviews.size();
    }

    public Object getItem(int position) {
        return listReviews.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Review entry = listReviews.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_review, null);
        }
        TextView author = (TextView) convertView.findViewById(R.id.author);
        author.setText(entry.getAuthor());
        TextView content = (TextView) convertView.findViewById(R.id.content);
        content.setText(entry.getContent());

        return convertView;
    }
}