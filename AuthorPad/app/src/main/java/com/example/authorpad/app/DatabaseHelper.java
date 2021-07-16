package com.example.authorpad.app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.authorpad.model.Story;
import com.example.authorpad.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "AUTHORPAD_DB";
    private static final String DB_TABLE_USER = "User_tb";
    private static final String DB_TABLE_STORY = "Story_tb";

    private static final int DB_VERSION = 1;

    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://authorpad-317506-default-rtdb.firebaseio.com/");
    private DatabaseReference storyRefs = database.getReference("story");

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DB_TABLE_USER + "(userid Integer " +
                "PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "username VARCHAR NOT NULL, password VARCHAR NOT NULL )");
        db.execSQL("CREATE TABLE " + DB_TABLE_STORY + "(storyid Integer " +
                "PRIMARY KEY AUTOINCREMENT " +
                "NOT NULL, storytitle VARCHAR NOT NULL, story VARCHAR NOT NULL, " +
                "userId INTEGER NOT NULL," +
                "firebaseID VARCHAR NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DB_TABLE_USER);
        onCreate(db);
    }

    
    public Cursor login(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+DB_TABLE_USER+" WHERE " +
                "username = (?) AND password = (?)",
                new String[]{user.getUsername(), user.getPassword()});
        return res;
    }

    public void signup(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + DB_TABLE_USER
                + " (username, password) VALUES (?,?)",
                new String[] {user.getUsername(), user.getPassword()});
    }

    public void insertStory(Story story) {
        SQLiteDatabase db = this.getWritableDatabase();
        String autoId = storyRefs.push().getKey();
        story.setFirebaseID(autoId);
        db.execSQL("INSERT INTO " + DB_TABLE_STORY + " (storytitle, story, userId, firebaseID) " +
                "VALUES (?,?,?,?)",
                new Object[] {story.getTitle(), story.getStory(), story.getUserID(), story.getFirebaseID()});
        storyRefs.child(autoId).setValue(story);
    }

    public Cursor fetchStories() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + DB_TABLE_STORY, null);

        return res;
    }

    public Cursor fetchPersonalStories(Context context) {
        int userId = UserSessionManager.getInstance(context).getUserId();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + DB_TABLE_STORY + " WHERE Story_tb.userId = (?)", new String[] {String.valueOf(userId)});
        return res;
    }

    public void updateStory(Story story) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + DB_TABLE_STORY + " SET storytitle = (?), story = (?)", new String[] {story.getTitle(), story.getStory()});
        storyRefs.child(story.getFirebaseID()).setValue(story);
    }

    public void deleteStory(Story story) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DB_TABLE_STORY + " WHERE storyid = (?)", new String[] {String.valueOf(story.getStoryID())});
        Log.d("Firebaseasd", "deleteStory: " + story.getFirebaseID());
        storyRefs.child(story.getFirebaseID()).removeValue();
    }

}
