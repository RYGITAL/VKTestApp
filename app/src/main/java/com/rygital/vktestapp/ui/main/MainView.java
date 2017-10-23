package com.rygital.vktestapp.ui.main;

import com.rygital.vktestapp.data.models.user.User;
import com.rygital.vktestapp.ui.base.MvpView;

import java.util.List;

interface MainView extends MvpView {
    void showWait();
    void showUsers(List<User> list);
    void addUser(User item);
    void deleteUser(int position);
    void startUserActivity(User item);
}
