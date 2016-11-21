package ru.mail.sporttogether.auth.core;

/**
 * Created by said on 13.11.16.
 */

public class SocialNetworkError {
    public final String msg;
    public final int errCode;

    public SocialNetworkError(String msg, int errCode) {
        this.msg = msg;
        this.errCode = errCode;
    }
}
