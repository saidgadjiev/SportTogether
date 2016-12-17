package ru.mail.sporttogether.net.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.mail.sporttogether.net.models.yandex.maps.GeoObject
import rx.Observable
import java.util.*


/**
 * Created by bagrusss on 17.12.16
 */
interface YandexMapsApi {
    @GET("1.x/")
    fun getAddressByCoordinates(@Query("format") format: String, @Query("geocode") longlat: String): Observable<ArrayList<GeoObject>>

    @GET("1.x/")
    fun getAddressesByString(@Query("format") format: String, @Query("geocode") address: String): Observable<ArrayList<GeoObject>>
}