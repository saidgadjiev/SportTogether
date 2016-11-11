package ru.mail.sporttogether.net.api

import retrofit2.http.*
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.responses.EventsResponse
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

    @POST("event/{id}/report")
    fun report(@Path("id") id: Long): Observable<Response<Any>>

    @DELETE("/event/{id}")
    fun cancelEvent(@Path("id") id: Long): Observable<Response<Any>>

    @GET("event/{id}/join")
    fun joinToEvent(@Path("id") id: Long, @Query("token") token: String?): Observable<Response<Any>>

    @GET("event/distance/{dis}")
    fun getEventsByDistanceAndPosition(@Path("dis") distance: Double, @Query("latitude") latitude: Double, @Query("longtitude") longitude: Double): Observable<Response<EventsResponse>>

}