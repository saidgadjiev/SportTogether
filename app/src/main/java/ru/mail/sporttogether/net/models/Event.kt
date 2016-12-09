package ru.mail.sporttogether.net.models

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by bagrusss on 29.09.16.
 *
 */
data class Event(
        var name: String = "",
        var category: Category = Category(null, "_"),
        @SerializedName("latitude") var lat: Double = 0.0,
        @SerializedName("longtitude") var lng: Double = 0.0,
        var id: Long = 0L,
        var maxPeople: Int = 5,
        var description: String = "",
        var result: String? = null,
        var isEnded: Boolean = false,
        var isJoined: Boolean = false,
        var isReported: Boolean = false,
        var tasks: ArrayList<Task>? = null,
        var date: Long = 0L,
        var reports: Int = 0,
        var nowPeople: Int = 0,
        var user: User = User("", -1L, -1)
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

    fun tasksInfo(): String {
        if (tasks != null) {
            val allTasks: Int = tasks.orEmpty().size
            val acceptedTasks: Int = tasks?.filter { it.userId != null }!!.count().or(0)
            return "" + acceptedTasks + '/' + allTasks
        } else return "0/0"
    }
}