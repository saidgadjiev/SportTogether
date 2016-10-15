package ru.mail.sporttogether.net.api

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import ru.mail.sporttogether.net.models.User
import ru.mail.sporttogether.net.responses.Response
import rx.Observable

/**
 * Created by bagrusss on 29.09.16.
 *
 */
interface AuthorizationAPI {

    @POST("auth")
    fun updateAuthorization(): Observable<Response<Any>>

    @DELETE("auth")
    fun unauthorize(): Observable<Response<Any>>

    @GET("auth")
    fun checkAuthorization(): Observable<Response<User>>

    @GET("hello/world")
    fun helloWorld(): Observable<Any>

}