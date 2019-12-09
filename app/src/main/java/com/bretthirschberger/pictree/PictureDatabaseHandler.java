package com.bretthirschberger.pictree;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PictureDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "posts.db";
    private static final String TABLE_POSTS = "posts";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_POST_USERNAME = "username";
    private static final String COLUMN_POST_IMAGE = "image";
    private static final String COLUMN_POST_DESCRIPTION = "description";
    private static final String COLUMN_POST_DATE = "date";


    public PictureDatabaseHandler(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_POSTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_POST_USERNAME + " TEXT, " +
                COLUMN_POST_IMAGE + " TEXT, " +
                COLUMN_POST_DESCRIPTION + " TEXT, " +
                COLUMN_POST_DATE + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        onCreate(db);
    }

    public void addPost(Bitmap bitmap,String uri) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                .getCloudImageLabeler();
        labeler.processImage(image)
                .addOnSuccessListener(labels -> {
                    String description = "";
                    for (FirebaseVisionImageLabel label : labels) {
                        Log.i("Detected Objects", label.getText());
                        description += label.getText() + " ";
                    }
                    values.put(COLUMN_POST_DESCRIPTION, description);
                    values.put(COLUMN_POST_IMAGE, uri);
                    values.put(COLUMN_POST_USERNAME, User.getCurrentUser().getUsername());
                    values.put(COLUMN_POST_DATE, LocalDateTime.now().toString());

                    db.insert(TABLE_POSTS, null, values);
                    db.close();
                })
                .addOnFailureListener(e -> e.printStackTrace());
    }

    public ArrayList<LocalPost> getAllEntries() {
        ArrayList<LocalPost> posts = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_POSTS;
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            LocalPost localPost = new LocalPost(
                    c.getString(c.getColumnIndex(COLUMN_POST_USERNAME)),
                    c.getString(c.getColumnIndex(COLUMN_POST_IMAGE)),
                    c.getString(c.getColumnIndex(COLUMN_POST_DESCRIPTION)),
                    c.getString(c.getColumnIndex(COLUMN_POST_DATE)));
            posts.add(0,localPost);
            c.moveToNext();
        }
        db.close();
        return posts;
    }

}
