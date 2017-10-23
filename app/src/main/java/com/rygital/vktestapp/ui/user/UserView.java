package com.rygital.vktestapp.ui.user;

import com.rygital.vktestapp.data.models.friends.Friend;
import com.rygital.vktestapp.ui.base.MvpView;

import java.util.List;

public interface UserView extends MvpView {
    void showFriends(List<Friend> list);
}
