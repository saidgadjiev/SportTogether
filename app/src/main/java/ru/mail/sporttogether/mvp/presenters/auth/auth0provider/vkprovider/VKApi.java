package ru.mail.sporttogether.mvp.presenters.auth.auth0provider.vkprovider;

import android.app.Activity;
import android.content.Intent;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.Collection;

/**
 * Created by said on 22.10.16.
 */

public class VKApi {

    private Callback callback;

    public void login(Activity activity, int requestCode, Collection<String> permissions, final Callback vkCallback) {
        VKSdk.login(activity);
        this.callback = vkCallback;

        /*new VKCallback<VKAccessToken>() {

            @Override
            public void onResult(VKAccessToken res) {
                vkCallback.onSuccess(res);
            }

            @Override
            public void onError(VKError error) {
                vkCallback.onError(error);
            }
        };*/
    }

    public void logout() {
        if (VKSdk.isLoggedIn()) {
            VKSdk.logout();
        }
    }

    public boolean finishLogin(int requestCode, int resultCode, Intent intent) {
        return VKSdk.onActivityResult(requestCode, resultCode, intent, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                callback.onSuccess(res);
            }

            @Override
            public void onError(VKError error) {
                callback.onError(error);
            }
        });
    }


    interface Callback {
        void onSuccess(VKAccessToken result);
        void onCancel();
        void onError(VKError exception);
    }
}
