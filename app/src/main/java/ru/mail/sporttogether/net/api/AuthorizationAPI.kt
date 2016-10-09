package ru.mail.sporttogether.net.api

import retrofit2.http.*
import ru.mail.sporttogether.net.models.*
import ru.mail.sporttogether.net.responses.Response
import rx.Observable

/**
 * Created by bagrusss on 29.09.16.
 *
 */
interface AuthorizationAPI {

    @POST("auth")
    fun updateAuthorization(@Body user: User): Observable<Response<Any>>

    @DELETE("auth")
    fun unauthorize(): Observable<Response<Any>>

    @GET("auth")
    fun checkAuthorization(): Observable<Response<User>>

    @GET("hello/world")
    fun helloWorld(): Observable<Response<Any>>

}