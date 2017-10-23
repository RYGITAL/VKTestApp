package com.rygital.vktestapp.data.remote;

import com.rygital.vktestapp.data.models.friends.FriendsResponse;
import com.rygital.vktestapp.data.models.user.UserResponse;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface VkMyApi {

    String ENDPOINT = "https://api.vk.com/method/";

    @GET("users.get?user_ids=query&fields=photo_50,counters&access_token=query")
    Observable<UserResponse> getUserData(
            @Query("user_ids") String user_id,
            @Query("access_token") String access_token
    );

    @GET("friends.get?user_id=query&fields=photo_50&access_token=query")
    Observable<FriendsResponse> getFriendsByUser(
            @Query("user_id") String user_id,
            @Query("access_token") String access_token
    );

    class Creator {
        public static VkMyApi createVkMyApi(File cacheFile) {
            Cache cache = null;

            try {
                cache = new Cache(cacheFile, 10*1024*1024);
            } catch (Exception e) {
                e.printStackTrace();
            }

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor((chain -> {
                        okhttp3.Response response = chain.proceed(chain.request());
                        response.cacheResponse();

                        return response;
                    }))
                    .cache(cache)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .baseUrl(ENDPOINT)
                    .build();
            return retrofit.create(VkMyApi.class);
        }
    }
}
