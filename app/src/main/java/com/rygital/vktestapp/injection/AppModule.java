package com.rygital.vktestapp.injection;

import android.app.Application;
import android.content.Context;

import com.rygital.vktestapp.data.remote.VkMyApi;
import com.rygital.vktestapp.data.local.PreferencesHelper;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Application app;
    private final File cacheFile;


    public AppModule(Application app, File cacheFile) {
        this.app = app;
        this.cacheFile = cacheFile;
    }

    @Provides
    @Singleton
    Context provideContext() { return app; }

    @Provides
    @Singleton
    VkMyApi provideVkMyApi() {
        return VkMyApi.Creator.createVkMyApi(cacheFile);
    }

    @Provides
    @Singleton PreferencesHelper providePreferencesHelper() {
        return new PreferencesHelper(app);
    }
}
