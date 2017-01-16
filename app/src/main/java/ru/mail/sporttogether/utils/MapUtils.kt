package ru.mail.sporttogether.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng


/**
 * Created by bagrusss on 16.01.17
 */
object MapUtils {

    @JvmStatic
    fun distanceBetweenPoints(p1: LatLng, p2: LatLng): Float {
        val results = FloatArray(1)
        Location.distanceBetween(p1.latitude, p1.longitude,
                p2.latitude, p2.longitude, results)
        return results[0] / 1000
    }

}