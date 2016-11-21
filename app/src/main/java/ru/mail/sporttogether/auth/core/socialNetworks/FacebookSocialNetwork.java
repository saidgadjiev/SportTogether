package ru.mail.sporttogether.auth.core.socialNetworks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;

import java.util.Collection;
import java.util.Collections;

import ru.mail.sporttogether.auth.core.ISocialNetwork;
import ru.mail.sporttogether.auth.core.SocialNetworkError;
import ru.mail.sporttogether.auth.core.SocialNetworkManager;
import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener;
import ru.mail.sporttogether.auth.core.listeners.OnRequestDetailedSocialPersonCompleteListener;


/**
 * Created by said on 12.11.16.
 */

public class FacebookSocialNetwork implements ISocialNetwork {

    private Collection<String> permissions;
    private OnLoginCompleteListener onLoginCompleteListener;
    private final CallbackManager callbackManager;
    private Activity activity;
    private static final String TAG= "FacebookSocialNetwork";
    public static final Integer ID = 1;
    private SharedPreferences sharedPreferences;

    public FacebookSocialNetwork(Activity activity) {
        this.activity = activity;
        this.sharedPreferences = activity.getSharedPreferences(SocialNetworkManager.SHARED_PREFERCE_TAG, Context.MODE_PRIVATE);
        this.callbackManager = CallbackManager.Factory.create();
        this.permissions = Collections.singleton("public_profile");
        initializeSdk();
    }

    @Override
    public void setOnLoginCompleteListener(OnLoginCompleteListener onLoginCompleteListener) {
        this.onLoginCompleteListener = onLoginCompleteListener;
    }

    @Override
    public void logout() {
        if (FacebookSdk.isInitialized()) {
            LoginManager.getInstance().logOut();
            sharedPreferences
                    .edit()
                    .putString(SocialNetworkManager.ACCESS_TOKEN, null)
                    .apply();
            Log.d(TAG, "Facebook sdk logout");
        } else {
            Log.w(TAG, "Couldn't log out as the SDK wasn't initialized yet.");
        }
    }

    @Override
    public void login() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult result) {
                onLoginCompleteListener.onSuccess();
                sharedPreferences
                        .edit()
                        .putString(SocialNetworkManager.ACCESS_TOKEN, result.getAccessToken().getToken())
                        .apply();
                Log.d(TAG, "Facebook sdk login");
            }

            @Override
            public void onCancel() {
                onLoginCompleteListener.onCancel();
            }

            @Override
            public void onError(FacebookException error) {
                onLoginCompleteListener.onError(new SocialNetworkError(error.getMessage(), -1));
                Log.d(TAG, "Facebook sdk error");
            }
        });
        LoginManager.getInstance().logInWithReadPermissions(activity, permissions);
    }

    private void initializeSdk() {
        if (FacebookSdk.isInitialized()) {
            Log.d(TAG, "Facebook sdk already initialized");
        } else {
            FacebookSdk.sdkInitialize(activity);
            Log.d(TAG, "Facebook sdk initialize");
        }
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
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean isConnected() {
        return !sharedPreferences.getString(SocialNetworkManager.ACCESS_TOKEN, "").isEmpty();
    }

    @Override
    public void sharePost(Activity activity, String title, String description, String uri) {
        ShareLinkContent content = new ShareLinkContent
                .Builder()
                .setContentDescription(description)
                .setContentTitle(title)
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
        ShareDialog shareDialog = new ShareDialog(activity);

        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
    }

    @Override
    public void requestPerson(OnRequestDetailedSocialPersonCompleteListener onRequestDetailedSocialPersonCompleteListener) {
        if (!isConnected()) {
            onRequestDetailedSocialPersonCompleteListener.onError(new SocialNetworkError("Please loggin first", -1));

            return;
        }
        GraphRequest graphRequest = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d(TAG, object.toString());
                    }
                });
        Bundle parameters = new Bundle();

        parameters.putString("fields", "id,name,email,link");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    @Override
    public Integer getID() {
        return ID;
    }
}
