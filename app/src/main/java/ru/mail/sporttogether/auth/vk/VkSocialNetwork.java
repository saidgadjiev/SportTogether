package ru.mail.sporttogether.auth.vk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiLink;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKWallPostResult;

import ru.mail.sporttogether.auth.core.AuthError;
import ru.mail.sporttogether.auth.core.SocialNetwork;
import ru.mail.sporttogether.auth.core.SocialNetworkManager;

/**
 * Created by said on 07.11.16.
 */

public class VKSocialNetwork implements SocialNetwork {

    public static final Integer ID = 1;
    private String[] scopes;
    private LoginCallback callback;

    public VKSocialNetwork(Context context, String[] scopes, LoginCallback callback) {
        this.scopes = scopes;
        this.callback = callback;
        VKSdk.initialize(context);
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void login(Activity activity) {
        VKSdk.login(activity, scopes);
        SocialNetworkManager.getInstance().setSocialNetworkID(ID);
    }

    @Override
    public boolean isLoggedIn() {
        String token = SocialNetworkManager.getInstance().getSharedPreferces().getString(SocialNetworkManager.AUTH_TOKEN, null);

        return token != null;
    }

    @Override
    public boolean isLoginOK(int requestCode, int resultCode, Intent data) {
        return VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                SocialNetworkManager.getInstance().getSharedPreferces()
                        .edit()
                        .putInt(SocialNetworkManager.AUTH_ID, ID)
                        .putString(SocialNetworkManager.AUTH_TOKEN, res.accessToken)
                        .apply();
                callback.onOK();
            }

            @Override
            public void onError(VKError error) {
                callback.onError(new AuthError(error.errorMessage, error.errorCode));
            }
        });
    }

    @Override
    public void logout() {
        VKSdk.logout();
        SocialNetworkManager.getInstance().getSharedPreferces()
                .edit()
                .putInt(SocialNetworkManager.AUTH_ID, 0)
                .putString(SocialNetworkManager.AUTH_TOKEN, null)
                .apply();
    }

    @Override
    public void sharePost(Activity activity) {
        VKApiLink vkLink = new VKApiLink();

        vkLink.url = "test.com";
        vkLink.title = "Test";
        vkLink.description = "Test";
        VKAttachments attachments = new VKAttachments();

        attachments.add(vkLink);
        VKRequest post = VKApi.wall().post(VKParameters.from(VKApiConst.ATTACHMENTS, attachments, VKApiConst.MESSAGE, "Test message"));
        post.setModelClass(VKWallPostResult.class);
        post.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
            }
        });
    }
}
