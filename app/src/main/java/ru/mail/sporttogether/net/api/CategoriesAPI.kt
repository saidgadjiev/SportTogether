package ru.mail.sporttogether.net.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.mail.sporttogether.net.models.CategoriesResponse
import ru.mail.sporttogether.net.models.Category
import ru.mail.sporttogether.net.responses.Response
import rx.Observable

/**
 * Created by bagrusss on 09.10.16.
 *
 */
interface CategoriesAPI {
    @POST("category")
    fun createCategory(@Body category: Category): Observable<Response<Category>>

    @GET("category/{id}")
    fun getCategoryById(@Path("id") id: Long): Observable<Response<Category>>

    @GET("category")
    fun getAllCategoryes(): Observable<Response<CategoriesResponse>>
}