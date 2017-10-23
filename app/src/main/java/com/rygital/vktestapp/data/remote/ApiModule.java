package com.rygital.vktestapp.data.remote;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiModule {
    private File cacheFile;

    public ApiModule(File cacheFile) {
        this.cacheFile = cacheFile;
    }

    @Singleton
    @Provides
    VkMyApi provideVkMyApi() {
        return VkMyApi.Creator.createVkMyApi(cacheFile);
    }
}
