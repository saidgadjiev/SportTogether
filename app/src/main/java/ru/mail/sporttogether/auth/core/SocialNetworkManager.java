package ru.mail.sporttogether.auth.core;

import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

import ru.mail.sporttogether.app.App;

/**
 * Created by said on 07.11.16.
 */

public class SocialNetworkManager {

    private Map<Integer, SocialNetwork> socialNetworks = new HashMap<>();
    private static SocialNetworkManager instance = null;
    public static final String SHARED_PREFERENCES_NAME = "social_networks";
    public static final String AUTH_ID = "auth id";
    public static final String AUTH_TOKEN = "auth token";
    private Integer socialNetworkID = 0;

    private SocialNetworkManager() {
    }

    public static SocialNetworkManager getInstance() {
        if (instance == null) {
            instance = new SocialNetworkManager();
        }

        return instance;
    }

    public Integer getSocialNetworkID() {
        return socialNetworkID;
    }

    public void setSocialNetworkID(Integer socialNetworkID) {
        this.socialNetworkID = socialNetworkID;
    }

    public Map<Integer, SocialNetwork> getRegisteredNetworks() {
        return socialNetworks;
    }

    public SocialNetwork getAuthSocialNetwork() {
        return socialNetworks.get(socialNetworkID);
    }

    public SharedPreferences getSharedPreferces() {
        return App.sharedPreferences;
    }

    public void addSocialNetwork(SocialNetwork network) {
        socialNetworks.put(network.getID(), network);
    }

    public SocialNetwork getSocialNetwork(Integer ID) {
        return socialNetworks.get(ID);
    }
}
