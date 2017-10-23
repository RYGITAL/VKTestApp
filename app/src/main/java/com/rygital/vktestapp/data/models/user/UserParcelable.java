package com.rygital.vktestapp.data.models.user;

import android.os.Parcel;
import android.os.Parcelable;

public class UserParcelable implements Parcelable {
    private User user;

    public UserParcelable(User user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(user.getId());
        parcel.writeString(user.getFirstName());
        parcel.writeString(user.getLastName());
        parcel.writeInt(user.getSex());
        parcel.writeString(user.getPhoto50());
        parcel.writeInt(user.getCounters().getFriends());
    }

    public static final Creator<UserParcelable> CREATOR = new Creator<UserParcelable>() {
        @Override
        public UserParcelable createFromParcel(Parcel in) {
            return new UserParcelable(in);
        }

        @Override
        public UserParcelable[] newArray(int size) {
            return new UserParcelable[size];
        }
    };

    protected UserParcelable(Parcel in) {
        user = new User(
                in.readString(),
                in.readString(),
                in.readString(),
                in.readInt(),
                in.readString(),
                in.readInt()
        );
    }

    public User getUser() {
        return user;
    }
}
