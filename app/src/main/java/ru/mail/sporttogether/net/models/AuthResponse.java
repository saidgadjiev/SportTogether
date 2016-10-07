package ru.mail.sporttogether.net.models;

/**
 * Created by said on 06.10.16.
 */

public class AuthResponse {

    private int code;
    private String message;

    public AuthResponse() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
