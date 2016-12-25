package ru.mail.sporttogether.net.models

import android.os.Parcel
import android.os.Parcelable
import ru.mail.sporttogether.net.utils.createParcel

/**
 * Created by bagrusss on 30.09.16
 */
data class User(
        var clientId: String = "",
        var id: Long = 0,
        var role: Int = 0,
        var name: String = "",
        var avatar: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this() {
        clientId = parcel.readString()
        id = parcel.readLong()
        role = parcel.readInt()
        name = parcel.readString()
        avatar = parcel.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(clientId)
        dest.writeLong(id)
        dest.writeInt(role)
        dest.writeString(name)
        dest.writeString(avatar)
    }

    override fun describeContents() = 0

    companion object {
        @JvmStatic
        val CREATOR = createParcel(::User)
    }

}
