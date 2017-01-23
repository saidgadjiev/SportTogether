package ru.mail.sporttogether.net.api

import retrofit2.http.*
import ru.mail.sporttogether.net.Response
import ru.mail.sporttogether.net.models.Profile
import ru.mail.sporttogether.net.models.RemindTime
import ru.mail.sporttogether.net.models.User
import rx.Observable

/**
 * Created by bagrusss on 29.09.16
 */
interface AuthorizationAPI {

    // жду сервер чтобы отдавал User
    @POST("auth")
    fun updateAuthorization(@Body profile: Profile): Observable<Response<User>>

    @PUT("user")
    fun updateReminderTime(@Body remindTime: RemindTime): Observable<Response<User>>

    @DELETE("auth")
    fun unauthorize(): Observable<Response<Any>>

    @GET("auth")
    fun checkAuthorization(): Observable<Response<User>>

}