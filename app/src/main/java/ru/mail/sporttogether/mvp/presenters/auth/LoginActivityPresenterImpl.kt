package ru.mail.sporttogether.mvp.presenters.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.BaseCallback
import com.auth0.android.facebook.FacebookAuthHandler
import com.auth0.android.facebook.FacebookAuthProvider
import com.auth0.android.lock.AuthButtonSize
import com.auth0.android.lock.AuthenticationCallback
import com.auth0.android.lock.InitialScreen
import com.auth0.android.lock.Lock
import com.auth0.android.lock.utils.LockException
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.facebook.FacebookSdk
import ru.mail.sporttogether.activities.LoginActivity
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.managers.auth.AuthManager
import ru.mail.sporttogether.managers.data.ICredentialsManager
import ru.mail.sporttogether.managers.headers.HeaderManagerImpl
import ru.mail.sporttogether.mvp.presenters.auth.auth0provider.vkprovider.VKAuthProvider
import ru.mail.sporttogether.mvp.presenters.auth.auth0provider.vkprovider.VkAuthHandler
import ru.mail.sporttogether.mvp.views.login.ILoginView
import ru.mail.sporttogether.net.api.AuthorizationAPI
import java.util.*
import javax.inject.Inject

/**
 * Created by said on 05.10.16.
 */
class LoginActivityPresenterImpl : ILoginPresenter {
    private var view: ILoginView? = null
    @Inject lateinit var api: AuthorizationAPI
    @Inject lateinit var context: Context
    @Inject lateinit var auth0: Auth0
    @Inject lateinit var aClient: AuthenticationAPIClient
    @Inject lateinit var credentialsManager: ICredentialsManager
    @Inject lateinit var headerManager: HeaderManagerImpl
    @Inject lateinit var authManager: AuthManager
    @Inject lateinit var facebookProvider: FacebookAuthProvider
    @Inject lateinit var vkProvider: VKAuthProvider
    private var lock: Lock? = null

    constructor(view: ILoginView) {
        App.injector.usePresenterComponent().inject(this)
        this.view = view
    }

    fun generateConnections(): List<String> {
        val connections = ArrayList<String>()

        connections.add("facebook")
        connections.add("vkontakte")
        if (connections.isEmpty()) {
            connections.add("no-connection")
        }

        return connections
    }

    fun trySignIn() {
        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(context)
        }
        aClient.tokenInfo(credentialsManager.getCredentials(context).idToken)
                .start(object : BaseCallback<UserProfile, AuthenticationException> {
                    override fun onSuccess(payload: UserProfile) {
                        Log.d("AUTH", "Authomatic login")

                        headerManager.token = credentialsManager.getCredentials(context).idToken
                        headerManager.clientId = payload.id
                        authManager.auth(api, view)
                    }

                    override fun onFailure(error: AuthenticationException) {
                        Log.d("AUTH", "Session expired")

                        credentialsManager.deleteCredentials(context)
                        view?.startLockActivity(lock)
                    }
                })
    }

    override fun onCreate(args: Bundle?) {
        val builder = Lock.newBuilder(auth0, callback)

        aClient = AuthenticationAPIClient(auth0)
        facebookProvider.setPermissions(Arrays.asList("public_profile"))

        var facebookHandler = FacebookAuthHandler(facebookProvider)
        var vkHandler = VkAuthHandler(vkProvider)

        builder.closable(true)
        builder.withAuthHandlers(facebookHandler, vkHandler)
        builder.withAuthButtonSize(AuthButtonSize.SMALL)
        //builder.withUsernameStyle(UsernameStyle.USERNAME);
        //builder.allowLogIn(true);
        //builder.allowSignUp(true);
        //builder.allowForgotPassword(true);
        builder.initialScreen(InitialScreen.LOG_IN)
        builder.allowedConnections(generateConnections())
        builder.setDefaultDatabaseConnection("Username-Password-Authentication")
        lock = builder.build(view as LoginActivity)
        if (credentialsManager.getCredentials(context).idToken == null) {
            view?.startLockActivity(lock)

            return
        }
        trySignIn()
    }

    var callback = object : AuthenticationCallback() {
        override fun onAuthentication(credentials: Credentials?) {
            Log.d("AUTH", "Logged in")

            credentialsManager.saveCredentials(context, credentials)
            trySignIn()
        }

        override fun onCanceled() {
            Log.d("Lock", "User pressed back.")
        }

        override fun onError(error: LockException) {
            Log.d("Lock", "Error")
        }
    }

    override fun onDestroy() {
        lock?.onDestroy(view as LoginActivity)
        lock = null
        view = null
    }
}
