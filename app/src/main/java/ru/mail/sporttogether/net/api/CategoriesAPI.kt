package ru.mail.sporttogether.net.api

import retrofit2.http.*
import ru.mail.sporttogether.net.models.Category
import ru.mail.sporttogether.net.CategoriesResponse
import ru.mail.sporttogether.net.Response
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

    @GET("category")
    fun getCategoriesBySubname(@Query("subname") subname: String?): Observable<Response<CategoriesResponse>>
}