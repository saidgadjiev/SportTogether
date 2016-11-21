package ru.mail.sporttogether.auth.core.listeners;


import ru.mail.sporttogether.auth.core.SocialNetworkError;

/**
 * Created by said on 13.11.16.
 */

public interface OnLoginCompleteListener {
    void onSuccess();

    void onCancel();

    void onError(SocialNetworkError socialNetworkError);
}
