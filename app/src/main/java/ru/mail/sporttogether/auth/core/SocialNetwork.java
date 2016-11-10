package ru.mail.sporttogether.auth.core;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by said on 07.11.16.
 */

public interface SocialNetwork {

    boolean isLoginOK(int requestCode, int resultCode, Intent data);

    Integer getID();

    void login(Activity activity);

    boolean isLoggedIn();

    void logout();

    void sharePost(Activity activity);

    /**
     * Created by said on 07.11.16.
     */

    interface LoginCallback {
        void onOK();

        void onError(AuthError error);

        void onCancel();
    }
}
