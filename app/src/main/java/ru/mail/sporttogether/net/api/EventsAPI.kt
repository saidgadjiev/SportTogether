package ru.mail.sporttogether.net.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.EventsResponse
import ru.mail.sporttogether.net.responses.Response
import rx.Observable

/**
 * Created by bagrusss on 09.10.16.
 *
 */
interface EventsAPI {

    @POST("event")
    fun createEvent(@Body event: Event): Observable<Response<Event>>

    @GET("event/{id}")
    fun getEventById(@Path("id") id: Long): Observable<Response<Event>>

    @GET("event")
    fun getAllEvents(): Observable<Response<EventsResponse>>

    @GET("event/joined")
    fun getMyEvents(): Observable<Response<EventsResponse>>
}