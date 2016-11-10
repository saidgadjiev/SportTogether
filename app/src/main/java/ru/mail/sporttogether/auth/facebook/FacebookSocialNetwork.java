package ru.mail.sporttogether.auth.facebook;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.Collection;

import ru.mail.sporttogether.auth.core.AuthError;
import ru.mail.sporttogether.auth.core.SocialNetwork;
import ru.mail.sporttogether.auth.core.SocialNetworkManager;

/**
 * Created by said on 07.11.16.
 */

public class FacebookSocialNetwork implements SocialNetwork {

    public static final int ID = 3;
    private static final int RC_SIGN_IN = 111;
    private final int ERROR_CODE = -1;
    private final CallbackManager callbackManager;
    private final LoginCallback callback;
    private final Collection<String> permissions;

    public FacebookSocialNetwork(Activity activity, Collection<String> permissions, LoginCallback callback) {
        this.callback = callback;
        this.permissions = permissions;
        callbackManager = CallbackManager.Factory.create();
        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(activity, RC_SIGN_IN);
        }
    }

    @Override
    public boolean isLoginOK(int requestCode, int resultCode, Intent data) {
        return callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public boolean isLoggedIn() {
        String token = SocialNetworkManager.getInstance().getSharedPreferces().getString(SocialNetworkManager.AUTH_TOKEN, null);
        return token != null;
    }

    @Override
    public void login(Activity activity) {
        SocialNetworkManager.getInstance().setSocialNetworkID(ID);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult result) {
                SocialNetworkManager.getInstance().getSharedPreferces()
                        .edit()
                        .putInt(SocialNetworkManager.AUTH_ID, ID)
                        .putString(SocialNetworkManager.AUTH_TOKEN, result.getAccessToken().getToken())
                        .apply();
                callback.onOK();
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }

            @Override
            public void onError(FacebookException error) {
                callback.onError(new AuthError(error.getMessage(), ERROR_CODE));
            }
        });
        LoginManager.getInstance().logInWithReadPermissions(activity, permissions);
    }

    @Override
    public void logout() {
        if (FacebookSdk.isInitialized()) {
            LoginManager.getInstance().logOut();
            SocialNetworkManager.getInstance().getSharedPreferces()
                    .edit()
                    .putInt(SocialNetworkManager.AUTH_ID, 0)
                    .putString(SocialNetworkManager.AUTH_TOKEN, null)
                    .apply();
        } else {
            Log.w("FacebookAuthProvider", "Couldn't log out as the SDK wasn't initialized yet.");
        }
    }

    @Override
    public void sharePost(Activity activity) {
        ShareLinkContent content = new ShareLinkContent
                .Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .setContentTitle("Test post")
                .setContentDescription("Welcome to sport together")
                .build();
        ShareDialog.show(activity, content);
    }
}
