package ru.mail.sporttogether.net.models

import android.os.Parcel
import android.os.Parcelable
import ru.mail.sporttogether.net.utils.createParcel

/**
 * Created by Ivan on 27.11.2016
 */
data class Task(
        var id: Long = 0,
        var message: String = "",
        var user: User? = null,
        var eventId: Long = 0
) : Parcelable {

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        eventId = parcel.readLong()
        user = parcel.readParcelable(User::class.java.classLoader)
        message = parcel.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeLong(eventId)
        dest.writeParcelable(user, 0)
        dest.writeString(message)
    }

    override fun describeContents() = 0


    override fun toString(): String {
        return message + " " + id
    }

    companion object {
        @JvmField
        val CREATOR = createParcel(::Task)
    }
}