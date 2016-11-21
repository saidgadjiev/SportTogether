package ru.mail.sporttogether.auth.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener;
import ru.mail.sporttogether.auth.core.listeners.OnRequestDetailedSocialPersonCompleteListener;


/**
 * Created by said on 12.11.16.
 */

public interface ISocialNetwork {

    void setOnLoginCompleteListener(OnLoginCompleteListener onLoginCompleteListener);

    void logout();

    void login();

    void onStart();

    void onPause();

    void onResume();

    void onStop();

    void onDestroy();

    void onSaveInstanceState(Bundle outState);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    boolean isConnected();

    void sharePost(Activity activity, String title, String description, String uri);

    void requestPerson(OnRequestDetailedSocialPersonCompleteListener onRequestDetailedSocialPersonCompleteListener)

    Integer getID();
}
