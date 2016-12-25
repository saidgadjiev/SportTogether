package ru.mail.sporttogether.net.models

import android.os.Parcel
import android.os.Parcelable
import ru.mail.sporttogether.net.utils.createParcel

/**
 * Created by bagrusss on 29.09.16.
 *
 */
data class Category(
        var id: Long = 0,
        var name: String = "") : Parcelable {

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        name = parcel.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(name)
    }

    override fun describeContents() = 0

    override fun toString() = name

    companion object {

        @JvmStatic
        val CREATOR = createParcel(::Category)
    }
}