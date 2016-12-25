package ru.mail.sporttogether.net.api

import retrofit2.http.GET
import retrofit2.http.Path
import ru.mail.sporttogether.net.models.IpResponse
import rx.Observable

/**
 * Created by bagrusss on 24.12.16
 */
interface ServiceApi {

    @GET("/ip/{ip}")
    fun getLocationByIP(@Path("ip") ip: String = ""): Observable<IpResponse>
}