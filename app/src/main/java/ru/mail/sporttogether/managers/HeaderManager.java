package ru.mail.sporttogether.managers;

import java.util.Map;

/**
 * Created by said on 08.10.16.
 */

public enum HeaderManager {
    INSTANCE;
    private String token;
    private String clientId;
    private Map<String, String> headers = null;

    public void addHeaders(String token, String clientId) {
        headers.put("Token", token);
        headers.put("ClientId", clientId);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
