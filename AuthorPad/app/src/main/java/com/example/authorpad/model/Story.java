package com.example.authorpad.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Story implements Parcelable {

    private String title;
    private String story;
    private int storyID;
    private int userID;
    private String firebaseID;

    public Story(String title, String story, int userID) {
        this.title = title;
        this.story = story;
        this.userID = userID;
    }

    public Story(String title, String story, int storyID, int userID, String firebaseID) {
        this.title = title;
        this.story = story;
        this.storyID = storyID;
        this.userID = userID;
        this.firebaseID = firebaseID;
    }

    protected Story(Parcel in) {
        title = in.readString();
        story = in.readString();
        storyID = in.readInt();
        userID = in.readInt();
        firebaseID = in.readString();
    }

    public static final Creator<Story> CREATOR = new Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel in) {
            return new Story(in);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public void setStoryID(int storyID) {
        this.storyID = storyID;
    }

    public int getStoryID() {
        return storyID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getFirebaseID() {
        return firebaseID;
    }

    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(story);
        parcel.writeInt(storyID);
        parcel.writeInt(userID);
        parcel.writeString(firebaseID);
    }
}
