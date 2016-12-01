package ru.mail.sporttogether.auth.core

import android.content.Intent
import android.util.Log
import ru.mail.sporttogether.auth.core.listeners.OnInitializationCompleteListener
import ru.mail.sporttogether.auth.core.social_networks.FacebookSocialNetwork
import ru.mail.sporttogether.auth.core.social_networks.VKSocialNetwork
import ru.mail.sporttogether.net.models.User
import java.util.*


/**
 * Created by said on 12.11.16.
 */

class SocialNetworkManager private constructor() {
    private val socialNetworksMap = HashMap<Int, ISocialNetwork>()
    private var onInitializationCompleteListener: OnInitializationCompleteListener? = null
    private var networkID: Int = -1
    lateinit var activeUser: User

    fun addSocialNetwork(network: ISocialNetwork) {
        socialNetworksMap.put(network.id!!, network)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "SocialNetworkManager.onActivityResult: $requestCode : $resultCode")

        for (network in socialNetworksMap.values) {
            network.onActivityResult(requestCode, resultCode, data)
        }
    }

    val facebookSocialNetwork: FacebookSocialNetwork
        get() = socialNetworksMap.get(FacebookSocialNetwork.ID) as FacebookSocialNetwork

    val vkSocialNetwork: VKSocialNetwork
        get() = socialNetworksMap.get(VKSocialNetwork.ID) as VKSocialNetwork

    fun setOnInitializationCompleteListener(onInitializationCompleteListener: OnInitializationCompleteListener) {
        this.onInitializationCompleteListener = onInitializationCompleteListener
    }

    fun getSocialNetwork(ID: Int): ISocialNetwork? {
        return socialNetworksMap.get(ID)
    }

    val initializedSocialNetworks: List<ISocialNetwork>
        get() = socialNetworksMap.values.toList()

    fun onResume() {
        onInitializationCompleteListener!!.onSocialNetworkManagerInitialized()
    }

    fun setNetworkID(ID: Int) {
        networkID = ID
    }

    fun getNetworkID(): Int {
        return networkID
    }

    private object Holder { val INSTANCE = SocialNetworkManager() }

    companion object {
        private val TAG = "SocialNetworkManager"
        val SOCIALMANAGER_TAG = "SocialNetworkManager"
        private val PARAM_FACEBOOK = "SocialNetworkManager.PARAM_FACEBOOK"
        val SHARED_PREFERCE_TAG = "SocialNetworkManager"
        val ACCESS_TOKEN = "AccessToken"
        val instance: SocialNetworkManager by lazy { Holder.INSTANCE }
    }
}
