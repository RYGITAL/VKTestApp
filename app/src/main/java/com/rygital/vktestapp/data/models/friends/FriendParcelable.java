package com.rygital.vktestapp.data.models.friends;

import android.os.Parcel;
import android.os.Parcelable;

public class FriendParcelable implements Parcelable {
    private Friend friend;

    public FriendParcelable(Friend friend) {
        this.friend = friend;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(friend.getId());
        parcel.writeString(friend.getFirstName());
        parcel.writeString(friend.getLastName());
        parcel.writeString(friend.getPhoto50());
        parcel.writeInt(friend.getOnline());
    }



    public static final Creator<FriendParcelable> CREATOR = new Creator<FriendParcelable>() {
        @Override
        public FriendParcelable createFromParcel(Parcel in) {
            return new FriendParcelable(in);
        }

        @Override
        public FriendParcelable[] newArray(int size) {
            return new FriendParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    protected FriendParcelable(Parcel in) {
        friend = new Friend(
                in.readString(),
                in.readString(),
                in.readString(),
                in.readString(),
                in.readInt()
        );
    }


    public Friend getFriend() {
        return friend;
    }
}
