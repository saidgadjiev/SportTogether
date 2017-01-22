package ru.mail.sporttogether.net.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ru.mail.sporttogether.managers.HeaderManagerImpl


/**
 * Created by bagrusss on 30.09.16.
 *
 */
class SportInterceptor(val headerManager: HeaderManagerImpl) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val headers = headerManager.getHeaders()
        for ((k, v) in headers) {
            builder.addHeader(k, v)
        }
        return chain.proceed(builder.build())
    }

}