package ru.mail.sporttogether.auth.core

import android.os.Parcel
import android.os.Parcelable
import ru.mail.sporttogether.utils.createParcel

/**
 * Created by said on 21.11.16
 */

class SocialPerson : Parcelable {

    var id: String = ""

    var name: String = ""

    var avatarURL: String = ""

    var profileURL: String = ""

    var email: String? = null

    constructor() {

    }

    constructor(`in`: Parcel) {
        id = `in`.readString()
        name = `in`.readString()
        avatarURL = `in`.readString()
        profileURL = `in`.readString()
        email = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(name)
        dest.writeString(avatarURL)
        dest.writeString(profileURL)
        dest.writeString(email)
    }


    override fun toString(): String {
        return "SocialPerson{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", avatarURL='" + avatarURL + '\'' +
                ", profileURL='" + profileURL + '\'' +
                ", email='" + email + '\'' +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as SocialPerson

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {

        @JvmStatic
        val CREATOR = createParcel(::SocialPerson)
    }

}
