package ru.mail.sporttogether.net.api

import retrofit2.http.*
import ru.mail.sporttogether.net.models.*
import ru.mail.sporttogether.net.responses.Response
import rx.Observable

/**
 * Created by bagrusss on 29.09.16.
 *
 */
interface RestAPI {

    @POST("category")
    fun createCategory(@Body category: Category): Observable<Response<Category>>

    @GET("category/{id}")
    fun getCategoryById(@Path("id") id: Long): Observable<Response<Category>>

    @GET("category")
    fun getAllCategoryes(): Observable<Response<CategoriesResponse>>



    @POST("event")
    fun createEvent(@Body event: Event): Observable<Response<Event>>

    @GET("event/{id}")
    fun getEventById(@Path("id") id: Long): Observable<Response<Event>>

    @GET("event")
    fun getAllEvents(): Observable<Response<EventsResponse>>
    


    @POST("auth")
    fun updateAuthorization(@Body user: User): Observable<Response<Any>>

    @DELETE("auth")
    fun unauthorize(): Observable<Response<Any>>

    @GET("auth")
    fun checkAuthorization(): Observable<Response<User>>

    @GET("hello/world")
    fun helloWorld(): Observable<Response<Any>>

}