package ru.mail.sporttogether.auth.core.persons

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by said on 21.11.16.
 */

class SocialPerson : Parcelable {

    /***
     * Id of social person from chosen social network.
     */
    var id: String? = null
    /***
     * Name of social person from social network.
     */
    var name: String? = null
    /***
     * Profile picture url of social person from social network.
     */
    var avatarURL: String? = null
    /***
     * Profile URL of social person from social network.
     */
    var profileURL: String? = null
    /***
     * Email of social person from social network if exist.
     */
    var email: String? = null

    constructor() {

    }

    private constructor(`in`: Parcel) {
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

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val that = o as SocialPerson?

        if (if (avatarURL != null) avatarURL != that!!.avatarURL else that!!.avatarURL != null)
            return false
        if (if (email != null) email != that.email else that.email != null) return false
        if (if (id != null) id != that.id else that.id != null) return false
        if (if (name != null) name != that.name else that.name != null) return false
        if (if (profileURL != null) profileURL != that.profileURL else that.profileURL != null)
            return false

        return true
    }

    override fun hashCode(): Int {
        var result = if (id != null) id!!.hashCode() else 0
        result = 31 * result + if (name != null) name!!.hashCode() else 0
        result = 31 * result + if (profileURL != null) profileURL!!.hashCode() else 0
        result = 31 * result + if (email != null) email!!.hashCode() else 0
        result = 31 * result + if (avatarURL != null) avatarURL!!.hashCode() else 0
        return result
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

    companion object {

        val CREATOR: Parcelable.Creator<SocialPerson> = object : Parcelable.Creator<SocialPerson> {
            override fun createFromParcel(`in`: Parcel): SocialPerson {
                return SocialPerson(`in`)
            }

            override fun newArray(size: Int): Array<SocialPerson> {
                return Array(size) {SocialPerson()}
            }
        }
    }
}
