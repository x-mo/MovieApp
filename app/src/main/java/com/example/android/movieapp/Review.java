package com.example.android.movieapp;

/**
 * Created by XPS on 11/10/2015.
 */
public class Review {

    public String author;
    public String content;

    public Review(String user, String content) {
        this.author = user;
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}