package ru.mail.sporttogether.auth.core.socialNetworks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import ru.mail.sporttogether.auth.core.ISocialNetwork;
import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener;


/**
 * Created by said on 17.11.16.
 */

public class GoogleSocialNetwork implements ISocialNetwork {

    private final Activity activity;
    private final String appId;

    public GoogleSocialNetwork(@NonNull Activity activity, @NonNull String serverClientId) {
        this.activity = activity;
        this.appId = serverClientId;
    }

    @Override
    public void setOnLoginCompleteListener(OnLoginCompleteListener onLoginCompleteListener) {

    }

    @Override
    public void logout() {

    }

    @Override
    public void login() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void sharePost(Activity activity, String title, String description, String uri) {

    }

    @Override
    public Integer getID() {
        return null;
    }
}
