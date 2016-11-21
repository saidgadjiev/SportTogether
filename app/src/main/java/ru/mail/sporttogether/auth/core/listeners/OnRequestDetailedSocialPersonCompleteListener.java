package ru.mail.sporttogether.auth.core.listeners;

import ru.mail.sporttogether.auth.core.SocialNetworkError;

/**
 * Created by said on 21.11.16.
 */
public interface OnRequestDetailedSocialPersonCompleteListener {
    void onError(SocialNetworkError socialNetworkError);
}
