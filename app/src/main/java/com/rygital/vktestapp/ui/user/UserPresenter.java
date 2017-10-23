package com.rygital.vktestapp.ui.user;

import android.os.Bundle;

import com.rygital.vktestapp.data.models.friends.Friend;
import com.rygital.vktestapp.data.models.friends.FriendParcelable;
import com.rygital.vktestapp.data.models.friends.FriendsResponse;
import com.rygital.vktestapp.data.models.user.User;
import com.rygital.vktestapp.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserPresenter extends BasePresenter<UserView> {

    private static final String BUNDLE_FRIEND_LIST_KEY = "BUNDLE_FRIEND_LIST_KEY";

    private List<FriendParcelable> friendList;

    @Inject
    UserPresenter() {
        super();
    }

    void loadFriends(User user) {
        Subscription subscription = getApi().getFriendsByUser(user.getId(), getPreferencesHelper().getAccessToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FriendsResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(FriendsResponse friendsResponse) {
                        getMvpView().showFriends(friendsResponse.getFriends());
                        convertToParcelable(friendsResponse.getFriends());
                    }
                });

        addSubscription(subscription);
    }

    private void convertToParcelable(List<Friend> list) {
        Observable.just(list)
                .subscribeOn(Schedulers.computation())
                .map(friends -> {
                    List<FriendParcelable> parcelables = new ArrayList<>();
                    for (Friend friend : friends) {
                        parcelables.add(new FriendParcelable(friend));
                    }
                    return parcelables;
                })
                .subscribe(friendParcelables -> friendList = friendParcelables);
    }

    /* STATE MANAGER */
    void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            friendList = savedInstanceState.getParcelableArrayList(BUNDLE_FRIEND_LIST_KEY);
        }
        if (isFriendListNotEmpty()) {
            Observable.just(friendList)
                    .subscribeOn(Schedulers.computation())
                    .map(friendParcelables -> {
                        List<Friend> list = new ArrayList<>();
                        for (FriendParcelable parcelable : friendParcelables)
                            list.add(parcelable.getFriend());
                        return list;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> getMvpView().showFriends(list));
        }
    }

    private boolean isFriendListNotEmpty() { return (friendList != null && !friendList.isEmpty()); }

    void onSaveInstanceState(Bundle outState) {
        if (isFriendListNotEmpty()) {
            outState.putParcelableArrayList(BUNDLE_FRIEND_LIST_KEY, new ArrayList<>(friendList));

        }
    }
}
