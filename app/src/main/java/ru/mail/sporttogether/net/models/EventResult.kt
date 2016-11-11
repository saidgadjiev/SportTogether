package ru.mail.sporttogether.net.models

/**
 * Created by bagrusss on 29.09.16.
 *
 */
data class EventResult(
        var id: Int = 0,
        var result: String = ""
) {
    override fun hashCode(): Int {
        return (id + result.hashCode())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as EventResult

        if (id != other.id) return false

        return true
    }
}