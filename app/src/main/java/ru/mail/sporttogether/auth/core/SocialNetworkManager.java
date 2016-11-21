package ru.mail.sporttogether.auth.core;

import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.mail.sporttogether.auth.core.listeners.OnInitializationCompleteListener;
import ru.mail.sporttogether.auth.core.socialNetworks.FacebookSocialNetwork;
import ru.mail.sporttogether.auth.core.socialNetworks.VKSocialNetwork;


/**
 * Created by said on 12.11.16.
 */

public class SocialNetworkManager {
    private static final String TAG = "SocialNetworkManager";
    public static final String SOCIALMANAGER_TAG = "SocialNetworkManager";
    private static final String PARAM_FACEBOOK = "SocialNetworkManager.PARAM_FACEBOOK";
    public static final String SHARED_PREFERCE_TAG = "SocialNetworkManager";
    public static final String ACCESS_TOKEN = "AccessToken";
    private Map<Integer, ISocialNetwork> socialNetworksMap = new HashMap<>();
    private OnInitializationCompleteListener onInitializationCompleteListener;
    private static SocialNetworkManager instance;

    public static SocialNetworkManager getInstance() {
        if (instance == null) {
            instance = new SocialNetworkManager();
        }

        return instance;
    }

    private SocialNetworkManager() {}

    public void addSocialNetwork(ISocialNetwork network) {
        socialNetworksMap.put(network.getID(), network);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "SocialNetworkManager.onActivityResult: " + requestCode + " : " + resultCode);

        for (ISocialNetwork network : socialNetworksMap.values()) {
            network.onActivityResult(requestCode, resultCode, data);
        }
    }

    public FacebookSocialNetwork getFacebookSocialNetwork() {
        return (FacebookSocialNetwork) socialNetworksMap.get(FacebookSocialNetwork.ID);
    }

    public VKSocialNetwork getVKSocialNetwork() {
        return (VKSocialNetwork) socialNetworksMap.get(VKSocialNetwork.ID);
    }

    public void setOnInitializationCompleteListener(OnInitializationCompleteListener onInitializationCompleteListener) {
        this.onInitializationCompleteListener = onInitializationCompleteListener;
    }

    public List<ISocialNetwork> getInitializedSocialNetworks() {
        return Collections.unmodifiableList(new ArrayList<>(socialNetworksMap.values()));
    }

    public void onResume() {
        onInitializationCompleteListener.onSocialNetworkManagerInitialized();
    }
}
