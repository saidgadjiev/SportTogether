package ru.mail.sporttogether.net.adapters

import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import ru.mail.sporttogether.net.models.yandex.maps.GeoObject
import java.lang.reflect.Type
import java.util.*

/**
 * Created by bagrusss on 17.12.16
 */
class GeoObjectListAdapterDeserializer : JsonDeserializer<ArrayList<GeoObject>> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): ArrayList<GeoObject> {
        val geoCollection = json.asJsonObject["response"]
                .asJsonObject["GeoObjectCollection"].asJsonObject

        val size = geoCollection["metaDataProperty"]
                .asJsonObject["GeocoderResponseMetaData"]
                .asJsonObject["found"]
                .asInt
        val members = geoCollection["featureMember"].asJsonArray
        val list = ArrayList<GeoObject>(size)
        var textAddress: String
        var mapsGeoObject: JsonObject
        for (member in members) {
            mapsGeoObject = member.asJsonObject["GeoObject"].asJsonObject

            textAddress = mapsGeoObject
                    .asJsonObject["metaDataProperty"]
                    .asJsonObject["GeocoderMetaData"]
                    .asJsonObject["AddressDetails"]
                    .asJsonObject["Country"]
                    .asJsonObject["AddressLine"]
                    .asString
            val coordinates = mapsGeoObject["Point"].asJsonObject["pos"].asString.split(" ")
            val point = LatLng(coordinates[1].toDouble(), coordinates[0].toDouble())
            val geoObject = GeoObject(textAddress = textAddress, point = point)
            list.add(geoObject)
        }
        return list
    }
}
