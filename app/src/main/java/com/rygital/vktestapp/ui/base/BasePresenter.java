package com.rygital.vktestapp.ui.base;

import com.rygital.vktestapp.data.remote.VkMyApi;
import com.rygital.vktestapp.App;
import com.rygital.vktestapp.data.local.PreferencesHelper;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BasePresenter<T extends MvpView> implements Presenter<T> {
    private T mMvpView;

    private VkMyApi vkMyApi;
    private PreferencesHelper preferencesHelper;
    private CompositeSubscription compositeSubscription;

    public BasePresenter() {
        vkMyApi = App.getComponent().getVkMyApi();
        preferencesHelper = App.getComponent().getPreferencesHelper();
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
        compositeSubscription.clear();
    }

    protected VkMyApi getApi() { return vkMyApi; }

    public T getMvpView() {
        return mMvpView;
    }

    protected void addSubscription(Subscription subscription) {
        compositeSubscription.add(subscription);
    }

    protected PreferencesHelper getPreferencesHelper() {
        return preferencesHelper;
    }
}
