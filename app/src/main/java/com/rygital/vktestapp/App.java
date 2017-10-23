package com.rygital.vktestapp;

import android.app.Application;

import com.rygital.vktestapp.injection.AppComponent;
import com.rygital.vktestapp.injection.AppModule;
import com.rygital.vktestapp.injection.DaggerAppComponent;
import com.vk.sdk.VKSdk;

import java.io.File;

public class App extends Application {
    // check access token. Не стал реализовывать
    /*VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
// VKAccessToken is invalid
            }
        }
    };*/

    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this, new File(getCacheDir(), "responses")))
                .build();
        // vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
    }

    public static AppComponent getComponent() {
        return component;
    }
}
