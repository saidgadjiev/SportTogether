package ru.mail.sporttogether.mvp.presenters.splash

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.BaseCallback
import com.auth0.android.facebook.FacebookAuthHandler
import com.auth0.android.facebook.FacebookAuthProvider
import com.auth0.android.lock.AuthenticationCallback
import com.auth0.android.lock.InitialScreen
import com.auth0.android.lock.Lock
import com.auth0.android.lock.utils.LockException
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.facebook.FacebookSdk
import ru.mail.sporttogether.activities.SplashActivity
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.managers.auth.AuthManager
import ru.mail.sporttogether.managers.data.CredentialsManager
import ru.mail.sporttogether.managers.headers.HeaderManagerImpl
import ru.mail.sporttogether.mvp.views.ISplashView
import ru.mail.sporttogether.net.api.AuthorizationAPI
import java.util.*
import javax.inject.Inject

/**
 * Created by bagrusss on 07.11.16.
 *
 */
class SplashActivityPresenterImpl : SplashActivityPresenter {

    private var view: ISplashView? = null
    @Inject lateinit var api: AuthorizationAPI
    @Inject lateinit var context: Context
    @Inject lateinit var auth0: Auth0
    @Inject lateinit var aClient: AuthenticationAPIClient
    @Inject lateinit var credentialsManager: CredentialsManager
    @Inject lateinit var headerManager: HeaderManagerImpl
    @Inject lateinit var authManager: AuthManager
    @Inject lateinit var provider: FacebookAuthProvider
    private var lock: Lock? = null

    constructor(view: ISplashView) {
        App.injector.usePresenterComponent().inject(this)
        this.view = view
    }

    fun generateConnections(): List<String> {
        val connections = ArrayList<String>()

        connections.add("facebook")
        if (connections.isEmpty()) {
            connections.add("no-connection")
        }

        return connections
    }

    fun trySignIn() {
        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(context)
        }
        aClient.tokenInfo(credentialsManager.getCredentials().idToken)
                .start(object : BaseCallback<UserProfile, AuthenticationException> {
                    override fun onSuccess(payload: UserProfile) {
                        Log.d("AUTH", "Authomatic login")

                        headerManager.token = credentialsManager.getCredentials().idToken
                        headerManager.clientId = payload.id
                        authManager.auth(api, view)
                    }

                    override fun onFailure(error: AuthenticationException) {
                        Log.d("AUTH", "Session expired")

                        credentialsManager.deleteCredentials()
                        view?.startLockActivity(lock)
                    }
                })
    }

    override fun onCreate(args: Bundle?) {
        val builder = Lock.newBuilder(auth0, callback)

        aClient = AuthenticationAPIClient(auth0)
        provider.setPermissions(Arrays.asList("public_profile"))

        var handler = FacebookAuthHandler(provider)
        builder.closable(true)
        builder.withAuthHandlers(handler)
        //builder.withAuthButtonSize(AuthButtonSize.SMALL);
        //builder.withUsernameStyle(UsernameStyle.USERNAME);
        //builder.allowLogIn(true);
        //builder.allowSignUp(true);
        //builder.allowForgotPassword(true);
        builder.initialScreen(InitialScreen.LOG_IN)
        builder.allowedConnections(generateConnections())
        builder.setDefaultDatabaseConnection("Username-Password-Authentication")
        lock = builder.build(view as SplashActivity)
        if (credentialsManager.getCredentials().idToken == null) {
            view?.startLockActivity(lock)

            return
        }
        trySignIn()
    }

    var callback = object : AuthenticationCallback() {
        override fun onAuthentication(credentials: Credentials) {
            Log.d("AUTH", "Logged in")

            credentialsManager.saveCredentials(credentials)
            trySignIn()
        }

        override fun onCanceled() {
            Log.d("Lock", "User pressed back.")
            view?.close()
        }

        override fun onError(error: LockException) {
            Log.d("Lock", "Error")
        }
    }

    override fun onDestroy() {
        lock?.onDestroy(view as SplashActivity)
        lock = null
        view = null
    }
}