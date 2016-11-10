package ru.mail.sporttogether.auth.core;

/**
 * Created by said on 07.11.16.
 */

public class AuthError {
    private final String msg;
    private final int code;


    public AuthError(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
