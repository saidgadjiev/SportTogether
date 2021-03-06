package ru.mail.sporttogether.net.api

import retrofit2.http.*
import ru.mail.sporttogether.net.EventsResponse
import ru.mail.sporttogether.net.Response
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.EventResult
import ru.mail.sporttogether.net.models.Task
import rx.Observable
import java.util.*

/**
 * Created by bagrusss on 09.10.16
 */
interface EventsAPI {

    @POST("event")
    fun createEvent(@Body event: Event): Observable<Response<Event>>

    @GET("event/{id}")
    fun getEventById(@Path("id") id: Long): Observable<Response<Event>>

    @GET("event")
    fun getAllEvents(): Observable<Response<EventsResponse>>

    @GET("event/joined")
    fun getMyEvents(@Query("events:sort") sort: String = "date"): Observable<Response<EventsResponse>>

    @POST("event/{id}/report")
    fun report(@Path("id") id: Long): Observable<Response<Any>>

    //удаляет событие и рассылает пуши
    @DELETE("/event/{id}")
    fun cancelEvent(@Path("id") id: Long): Observable<Response<Any>>

    @PUT("event/result")
    fun updateResult(@Body event: EventResult): Observable<Response<Any>>

    @GET("task")
    fun getTasksByEventId(@Query("eventId") idEvent: Long): Observable<Response<ArrayList<Task>>>

    @PUT("task")
    fun checkTask(@Body task: Task): Observable<Response<Any>>

    @GET("event/{id}/join")
    fun joinToEvent(@Path("id") id: Long,
                    @Query("token") token: String?): Observable<Response<Any>>

    @DELETE("event/{id}/join")
    fun unjoinFromEvent(@Path("id") id: Long): Observable<Response<Any>>

    @GET("event/distance/{dis}")
    fun getEventsByDistanceAndPosition(@Path("dis") distance: Double,
                                       @Query("latitude") latitude: Double,
                                       @Query("longtitude") longitude: Double): Observable<Response<EventsResponse>>

    @GET("event")
    fun getEventsByCategory(@Query("events:category:name") category: String): Observable<Response<EventsResponse>>

    @GET("event/user")
    fun getOrganizedEvents(@Query("events:sort") sort: String = "date"): Observable<Response<EventsResponse>>

    @GET("event/user")
    fun getResultedEvents(@Query("events:isEnded") isEnded: Boolean = true, @Query("events:sort") sort: String = "dateDesc"): Observable<Response<EventsResponse>>

    @PUT("event/{id}")
    fun updateEvent(@Path("id") id: Long, @Body event: Event): Observable<Response<Any>>


}