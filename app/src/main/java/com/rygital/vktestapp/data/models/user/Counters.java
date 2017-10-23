package com.rygital.vktestapp.data.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Counters {

//    @SerializedName("albums")
//    @Expose
//    private int albums;
//    @SerializedName("videos")
//    @Expose
//    private int videos;
//    @SerializedName("audios")
//    @Expose
//    private int audios;
//    @SerializedName("notes")
//    @Expose
//    private int notes;
//    @SerializedName("photos")
//    @Expose
//    private int photos;
//    @SerializedName("groups")
//    @Expose
//    private int groups;
//    @SerializedName("gifts")
//    @Expose
//    private int gifts;
    @SerializedName("friends")
    @Expose
    private int friends;
//    @SerializedName("online_friends")
//    @Expose
//    private int onlineFriends;
//    @SerializedName("user_photos")
//    @Expose
//    private int userPhotos;
//    @SerializedName("followers")
//    @Expose
//    private int followers;
//    @SerializedName("subscriptions")
//    @Expose
//    private int subscriptions;
//    @SerializedName("pages")
//    @Expose
//    private int pages;


    public Counters(int friends) {
        this.friends = friends;
    }

    public int getFriends() {
        return friends;
    }

    public void setFriends(int friends) {
        this.friends = friends;
    }
}
