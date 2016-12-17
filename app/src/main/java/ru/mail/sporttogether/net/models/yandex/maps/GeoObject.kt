package ru.mail.sporttogether.net.models.yandex.maps

import com.google.android.gms.maps.model.LatLng

/**
 * Created by bagrusss on 17.12.16
 */
data class GeoObject(
        val textAddress: String, //Россия, Москва, улица Сталеваров, 14к3
        val point: LatLng //37.839088 55.757344
)