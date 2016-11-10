package ru.mail.sporttogether.net.models

import com.google.gson.annotations.SerializedName

/**
 * Created by bagrusss on 29.09.16.
 *
 */
data class Event(
        var name: String = "",
        var categoryId: Long = 0L,
        @SerializedName("latitude") var lat: Double = 0.0,
        @SerializedName("longtitude") var lng: Double = 0.0,
        var id: Long = 0L,
        var userId: Long = 0L,
        var maxPeople: Int = 5,
        var description: String = "",
        var isEnded: Boolean = false,
        var date: Long,
        var reports: Int = 0,
        var nowPeople: Int = 0
) {
    override fun hashCode(): Int {
        return (id + date).toInt()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Event

        if (id != other.id) return false

        return true
    }
}