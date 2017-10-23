package com.rygital.vktestapp.data.models.friends;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FriendsResponse {
    @SerializedName("response")
    @Expose
    private List<Friend> friends = null;


    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }
}
