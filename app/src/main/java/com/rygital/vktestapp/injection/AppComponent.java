package com.rygital.vktestapp.injection;

import com.rygital.vktestapp.data.remote.VkMyApi;
import com.rygital.vktestapp.data.local.PreferencesHelper;
import com.rygital.vktestapp.ui.main.MainActivity;
import com.rygital.vktestapp.ui.user.UserActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    VkMyApi getVkMyApi();
    PreferencesHelper getPreferencesHelper();

    void injectMainActivity(MainActivity activity);
    void injectUserActivity(UserActivity activity);
}
