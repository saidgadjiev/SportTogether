package ru.mail.sporttogether.mvp.presenters.auth.auth0provider.vkprovider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.provider.AuthHandler;
import com.auth0.android.provider.AuthProvider;

/**
 * Created by said on 22.10.16.
 */

public class VkAuthHandler implements AuthHandler {

    private final VKAuthProvider provider;

    public VkAuthHandler(@NonNull AuthenticationAPIClient apiClient) {
        this(new VKAuthProvider(apiClient));
    }

    public VkAuthHandler(@NonNull VKAuthProvider provider) {
        this.provider = provider;
    }

    @Nullable
    @Override
    public AuthProvider providerFor(@Nullable String strategy, @NonNull String connection) {
        if ("vkontakte".equals(strategy)) {
            return provider;
        }
        return null;
    }
}
