package com.example.android.movieapp.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
Context context;
    private static final int DATABASE_VERSION = 5;

    private static final String DATABASE_NAME = "MovieAppDB";

    private static final String TABLE_FAVORITES = "favorites";


    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DATE = "date";
    private static final String KEY_POSTER = "poster";
    private static final String KEY_VOTE = "vote";
    private static final String KEY_SYNOPSIS = "synopsis";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + KEY_ID + " PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_POSTER + " TEXT,"
                + KEY_VOTE + " TEXT,"
                + KEY_SYNOPSIS + " TEXT"
                + ")";
        db.execSQL(CREATE_FAVORITES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);

        onCreate(db);
    }

    public void addFavorite(String id, String title, String date, String poster, String vote, String synopsis) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_TITLE, title);
        values.put(KEY_DATE, date);
        values.put(KEY_POSTER, poster);
        values.put(KEY_VOTE, vote);
        values.put(KEY_SYNOPSIS, synopsis);


        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    public int getRowCount() {
        String
                countQuery = "SELECT  * FROM " + TABLE_FAVORITES;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        return rowCount;
    }

    public String[] getMovieByID(String movieID) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor =
                db.query(true, TABLE_FAVORITES, new String[]{KEY_ID, KEY_TITLE, KEY_DATE, KEY_POSTER, KEY_VOTE, KEY_SYNOPSIS}, null, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        while (mCursor.getString(0).compareTo(movieID) != 0) {
            mCursor.moveToNext();
        }
        String[] movie = {mCursor.getString(0), mCursor.getString(1), mCursor.getString(2), mCursor.getString(3), mCursor.getString(4), mCursor.getString(5)};
        db.close();
        mCursor.close();
        return movie;
    }

    public String[][] getFavoritesList() throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor =
                db.query(true, TABLE_FAVORITES, new String[]{KEY_ID, KEY_POSTER}, null, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        String[][] couples = new String[100][100];


        int i = 0;
        while (!mCursor.isLast()) {
            couples[i][0] = mCursor.getString(0);
            couples[i][1] = mCursor.getString(1);
            mCursor.moveToNext();
            i++;
        }

        couples[i][0] = mCursor.getString(0);
        couples[i][1] = mCursor.getString(1);
        Log.e("Couple : ",couples[i][0]);
        Log.e("Couple : ",couples[i][1]);

        db.close();
        mCursor.close();
        return couples;
    }

}