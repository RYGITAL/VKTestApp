package com.rygital.vktestapp.data.models.friends;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Friend {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("photo_50")
    @Expose
    private String photo50;
    @SerializedName("online")
    @Expose
    private int online;

    public Friend(String id, String firstName, String lastName, String photo50, int online) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo50 = photo50;
        this.online = online;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto50() {
        return photo50;
    }

    public void setPhoto50(String photo50) {
        this.photo50 = photo50;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }
}
