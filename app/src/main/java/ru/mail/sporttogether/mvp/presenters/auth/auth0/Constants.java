package ru.mail.sporttogether.mvp.presenters.auth.auth0;

/**
 * Created by said on 23.10.16.
 */

abstract class Constants {
    static final String LIBRARY_NAME = "Lock.Android";

    static final String OPTIONS_EXTRA = "com.auth0.android.lock.key.Options";

    static final String AUTHENTICATION_ACTION = "com.auth0.android.lock.action.Authentication";
    static final String SIGN_UP_ACTION = "com.auth0.android.lock.action.SignUp";
    static final String CANCELED_ACTION = "com.auth0.android.lock.action.Canceled";

    static final String ERROR_EXTRA = "com.auth0.android.lock.extra.Error";
    static final String ID_TOKEN_EXTRA = "com.auth0.android.lock.extra.IdToken";
    static final String ACCESS_TOKEN_EXTRA = "com.auth0.android.lock.extra.AccessToken";
    static final String TOKEN_TYPE_EXTRA = "com.auth0.android.lock.extra.TokenType";
    static final String REFRESH_TOKEN_EXTRA = "com.auth0.android.lock.extra.RefreshToken";
    static final String EMAIL_EXTRA = "com.auth0.android.lock.extra.Email";
    static final String USERNAME_EXTRA = "com.auth0.android.lock.extra.Username";
}
