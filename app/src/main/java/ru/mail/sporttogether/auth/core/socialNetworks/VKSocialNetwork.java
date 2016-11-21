package ru.mail.sporttogether.auth.core.socialNetworks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.model.VKScopes;
import com.vk.sdk.dialogs.VKShareDialog;
import com.vk.sdk.dialogs.VKShareDialogBuilder;

import ru.mail.sporttogether.auth.core.ISocialNetwork;
import ru.mail.sporttogether.auth.core.SocialNetworkError;
import ru.mail.sporttogether.auth.core.SocialNetworkManager;
import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener;
import ru.mail.sporttogether.auth.core.listeners.OnRequestDetailedSocialPersonCompleteListener;

/**
 * Created by said on 17.11.16.
 */


public class VKSocialNetwork implements ISocialNetwork {

    private OnLoginCompleteListener onLoginCompleteListener;
    private Activity activity;
    private static final String TAG= "VKSocialNetwork";
    public static final Integer ID = 2;
    private SharedPreferences sharedPreferences;
    private String[] scopes;

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken != null) {
                sharedPreferences
                        .edit()
                        .putString(SocialNetworkManager.ACCESS_TOKEN, newToken.accessToken)
                        .apply();
                onLoginCompleteListener.onSuccess();
            }
        }
    };

    public VKSocialNetwork(Activity activity) {
        this.activity = activity;
        this.sharedPreferences = activity.getSharedPreferences(SocialNetworkManager.SHARED_PREFERCE_TAG, Context.MODE_PRIVATE);
        this.scopes = new String[] {
                VKScopes.WALL
        };
        vkAccessTokenTracker.startTracking();
    }


    @Override
    public void setOnLoginCompleteListener(OnLoginCompleteListener onLoginCompleteListener) {
        this.onLoginCompleteListener = onLoginCompleteListener;
    }

    @Override
    public void logout() {
        VKSdk.logout();
    }

    @Override
    public void login() {
        VKSdk.login(activity, scopes);
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
        vkAccessTokenTracker.stopTracking();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {

            }
            @Override
            public void onError(VKError error) {
                onLoginCompleteListener.onError(new SocialNetworkError(error.errorMessage, error.errorCode));
            }
        });
    }

    @Override
    public boolean isConnected() {
        return !sharedPreferences.getString(SocialNetworkManager.ACCESS_TOKEN, "").isEmpty();
    }

    @Override
    public void sharePost(Activity activity, String title, String description, String uri) {
        VKShareDialogBuilder builder = new VKShareDialogBuilder();
        builder.setText(description);

        builder.setAttachmentLink(title, uri);
        builder.setShareDialogListener(new VKShareDialog.VKShareDialogListener() {
            @Override
            public void onVkShareComplete(int postId) {
                Log.d("TAG", "Share complete");
            }
            @Override
            public void onVkShareCancel() {
                Log.d("TAG", "Share complete");
                // recycle bitmap if need
            }
            @Override
            public void onVkShareError(VKError error) {
                Log.d("TAG", "Share complete");
                // recycle bitmap if need
            }
        });
        builder.show(activity.getFragmentManager(), "VK_SHARE_DIALOG");
    }

    @Override
    public void requestPerson(OnRequestDetailedSocialPersonCompleteListener onRequestDetailedSocialPersonCompleteListener) {
        if (!isConnected()) {
            onRequestDetailedSocialPersonCompleteListener.onError(new SocialNetworkError("Please loggin first", -1));
        }
    }

    @Override
    public Integer getID() {
        return ID;
    }
}
