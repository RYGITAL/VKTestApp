package com.rygital.vktestapp.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

public class PreferencesHelper {
    private static final String PREF_FILE_NAME = "vktestapp_pref_file";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String SERVICE_KEY = "e0660aebe0660aebe0660aebf9e038f64cee066e0660aebb981558736ad28c23f6fc444";

    private final SharedPreferences preferences;

    public PreferencesHelper(Context context) {
        preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void setAccessToken(String accessToken) {
        preferences.edit().putString(ACCESS_TOKEN, accessToken).apply();
    }

    public String getAccessToken() {
        return preferences.getString(ACCESS_TOKEN, SERVICE_KEY);
    }

    public void clear() { preferences.edit().putString(ACCESS_TOKEN, SERVICE_KEY).apply(); }
}
