package ru.mail.sporttogether.net.api

import retrofit2.http.*
import ru.mail.sporttogether.net.Response
import ru.mail.sporttogether.net.models.Event
import rx.Observable

/**
 * Created by bagrusss on 05.02.17
 */
interface TemplatesApi {

    @DELETE("templates/{id}")
    fun deleteTemplate(@Path("id") id: Long): Observable<Response<Any>>

    @GET("templates")
    fun getTemplates(): Observable<Response<ArrayList<Event>>>

    @POST("templates")
    fun createTemplate(@Body event: Event): Observable<Response<Event>>

    @PUT("templates/{id}")
    fun updateTemplate(@Body event: Event): Observable<Response<Event>>
}