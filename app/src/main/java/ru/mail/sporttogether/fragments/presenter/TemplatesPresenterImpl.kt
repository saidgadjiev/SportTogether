package ru.mail.sporttogether.fragments.presenter

import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.fragments.view.TemplatesView
import ru.mail.sporttogether.net.Response
import ru.mail.sporttogether.net.api.TemplatesApi
import ru.mail.sporttogether.net.models.Event
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.Subscriptions
import java.util.*
import javax.inject.Inject

/**
 * Created by bagrusss on 05.02.17
 */
class TemplatesPresenterImpl(val view: TemplatesView?) : TemplatesPresenter {

    @Inject lateinit var templatesApi: TemplatesApi
    private var subscription = Subscriptions.empty()

    init {
        App.injector.usePresenterComponent().inject(this)
    }

    override fun loadTemplates() {
        subscription.unsubscribe()
        view?.showProgressDialog()
        subscription = templatesApi.getTemplates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<ArrayList<Event>>>() {
                    override fun onNext(t: Response<ArrayList<Event>>) {
                        if (t.code == 0 && t.data.isNotEmpty())
                            view?.swapTemplates(t.data)
                        view?.hideProgressDialog()
                    }

                    override fun onError(e: Throwable) {
                        view?.hideProgressDialog()
                    }

                    override fun onCompleted() {

                    }

                })

    }

}