package ru.mail.sporttogether.mvp.presenters.splash

import ru.mail.sporttogether.auth.core.listeners.OnLoginCompleteListener
import ru.mail.sporttogether.auth.core.listeners.OnRequestSocialPersonCompleteListener
import ru.mail.sporttogether.mvp.presenters.IPresenter

/**
 * Created by bagrusss on 15.10.16.
 *
 */
interface SplashActivityPresenter : IPresenter, OnRequestSocialPersonCompleteListener, OnLoginCompleteListener