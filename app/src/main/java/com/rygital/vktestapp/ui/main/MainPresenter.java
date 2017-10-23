package com.rygital.vktestapp.ui.main;

import android.os.Bundle;

import com.rygital.vktestapp.data.local.Database;
import com.rygital.vktestapp.data.models.user.User;
import com.rygital.vktestapp.data.models.user.UserParcelable;
import com.rygital.vktestapp.data.models.user.UserResponse;
import com.rygital.vktestapp.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Singleton
public class MainPresenter extends BasePresenter<MainView> {
    private static final String BUNDLE_USER_LIST_KEY = "BUNDLE_USER_LIST_KEY";

    private final Database database;
    private List<UserParcelable> userList;

    @Inject
    MainPresenter(Database database) {
        super();
        this.database = database;
    }

    void setNewAccessToken(String accessToken) {
        getPreferencesHelper().setAccessToken(accessToken);
    }

    void resetAccessToken() {
        getPreferencesHelper().clear();
    }

    void syncData() {
        getMvpView().showWait();
        Subscription subscription = Observable.from(database.getIds())
                .subscribeOn(Schedulers.io())
                .flatMap(id -> getApi().getUserData(id, getPreferencesHelper().getAccessToken()))
                .subscribe(new Observer<UserResponse>() {
                    @Override
                    public void onCompleted() {
                        loadData();
                        // если все хорошо, то выводим обновленные данные на экран
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadData();
                        // если произошла ошибка, то выводим то что было в базе данных
                    }

                    @Override
                    public void onNext(UserResponse userResponse) {
                        for (User user : userResponse.getUsers()) {
                            database.updateItem(user);
                        }
                    }
                });
        addSubscription(subscription);
    }

    private void loadData() {
        Subscription subscription = database.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<User> list) {
                        getMvpView().showUsers(list);
                        convertToParcelable(list);
                    }
                });
        addSubscription(subscription);
    }

    void addItem(String id) {
        if (id.isEmpty()) {
            getMvpView().onError("Id field is empty");
            return;
        }

        Subscription subscription = getApi().getUserData(id, getPreferencesHelper().getAccessToken())
                .subscribeOn(Schedulers.io())
                .filter(userResponse -> {
                    for (User user : userResponse.getUsers())
                        for (String dbID : database.getIds()) {
                            if (user.getId().equals(dbID)) return false;

                        }
                    return true;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserResponse>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(UserResponse userResponse) {
                        for (User user : userResponse.getUsers()) {
                            database.addItem(user);
                            userList.add(new UserParcelable(user));
                            getMvpView().addUser(user);
                        }

                    }
                });
        addSubscription(subscription);
    }

    void deleteItem(User item, int position) {
        database.removeItem(item);
        getMvpView().deleteUser(position);
        userList.remove(position);
    }

    void startUserActivity(User item) {
        getMvpView().startUserActivity(item);
    }

    private void convertToParcelable(List<User> list) {
        Observable.just(list)
                .subscribeOn(Schedulers.computation())
                .map(users -> {
                    List<UserParcelable> parcelables = new ArrayList<>();
                    for (User user : users) {
                        parcelables.add(new UserParcelable(user));
                    }
                    return parcelables;
                })
                .subscribe(userParcelables -> userList = userParcelables);
    }

    @Override
    public void attachView(MainView mvpView) {
        super.attachView(mvpView);
        database.open();
    }

    @Override
    public void detachView() {
        super.detachView();
        database.close();
    }

    /* STATE MANAGER */
    void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            userList = savedInstanceState.getParcelableArrayList(BUNDLE_USER_LIST_KEY);
        }
        if (isUserListNotEmpty()) {
            Observable.just(userList)
                    .subscribeOn(Schedulers.computation())
                    .map(userParcelables -> {
                        List<User> list = new ArrayList<>();
                        for (UserParcelable parcelable : userParcelables)
                            list.add(parcelable.getUser());
                        return list;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> getMvpView().showUsers(list));
        } else syncData();
    }

    private boolean isUserListNotEmpty() { return (userList != null && !userList.isEmpty()); }

    void onSaveInstanceState(Bundle outState) {
        if (isUserListNotEmpty()) {
            outState.putParcelableArrayList(BUNDLE_USER_LIST_KEY, new ArrayList<>(userList));
        }
    }
}
