package ru.mail.sporttogether.fragments.adapter.presenters

import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.fragments.adapter.views.TemplateHolderView
import ru.mail.sporttogether.mvp.IPresenter
import ru.mail.sporttogether.net.Response
import ru.mail.sporttogether.net.api.TemplatesApi
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.Subscriptions
import javax.inject.Inject

/**
 * Created by bagrusss on 12.02.17
 */
class TemplatesHolderPresenterImpl(private var view: TemplateHolderView?) : IPresenter {

    @Inject lateinit var templatesApi: TemplatesApi

    init {
        App.injector.useViewComponent().inject(this)
    }

    private var apiSubscription = Subscriptions.empty()

    fun deleteTemplate(id: Long) {
        apiSubscription = templatesApi.deleteTemplate(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<Any>>() {
                    override fun onError(e: Throwable) {

                    }

                    override fun onNext(t: Response<Any>) {
                        if (t.code == 0) {
                            view?.templateDeleted()
                        }
                    }

                    override fun onCompleted() {

                    }

                })
    }

}