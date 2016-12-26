package ru.mail.sporttogether.net.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import ru.mail.sporttogether.net.utils.createParcel
import java.util.*

/**
 * Created by bagrusss on 29.09.16.
 *
 */
data class Event(var name: String = "",
                 var address: String = "",
                 var category: Category = Category(),
                 var user: User = User(),
                 var tasks: ArrayList<Task>? = null,
                 @SerializedName("latitude") var lat: Double = 0.0,
                 @SerializedName("longtitude") var lng: Double = 0.0,
                 var id: Long = -1,
                 var maxPeople: Int = 5,
                 var description: String = "",
                 var result: String? = null,
                 var isEnded: Boolean = false,
                 var isJoined: Boolean = false,
                 var isReported: Boolean = false,
                 var date: Long = 0,
                 var reports: Int = 0,
                 var nowPeople: Int = 0) : Parcelable {

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        address = parcel.readString()
        lat = parcel.readDouble()
        lng = parcel.readDouble()
        id = parcel.readLong()
        date = parcel.readLong()
        maxPeople = parcel.readInt()
        reports = parcel.readInt()
        nowPeople = parcel.readInt()
        description = parcel.readString()
        result = parcel.readString()
        isEnded = if (parcel.readByte() > 0) true else false
        isJoined = if (parcel.readByte() > 0) true else false
        isReported = if (parcel.readByte() > 0) true else false
        category = parcel.readParcelable(Category::class.java.classLoader)
        user = parcel.readParcelable(User::class.java.classLoader)
        tasks = parcel.createTypedArrayList(Task.CREATOR)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(address)
        dest.writeDouble(lat)
        dest.writeDouble(lng)
        dest.writeLong(id)
        dest.writeLong(date)
        dest.writeInt(maxPeople)
        dest.writeInt(reports)
        dest.writeInt(nowPeople)
        dest.writeString(description)
        dest.writeString(result)
        dest.writeByte(if (isEnded) 1 else 0)
        dest.writeByte(if (isJoined) 1 else 0)
        dest.writeByte(if (isReported) 1 else 0)
        dest.writeParcelable(category, 0)
        dest.writeParcelable(user, 0)
        dest.writeTypedList(tasks)
    }

    override fun describeContents() = 0

    override fun hashCode() = id.toInt()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Event

        if (id != other.id) return false

        return true
    }

    companion object {

        @JvmStatic
        fun tasksInfo(tasks: ArrayList<Task>?): String {
            if (tasks != null) {
                val allTasks: Int = tasks.orEmpty().size
                val acceptedTasks: Int = tasks.filter { it.user != null }.count().or(0)
                return "" + acceptedTasks + '/' + allTasks
            } else return "0/0"
        }

        @JvmField
        val CREATOR: Parcelable.Creator<Event> = createParcel(::Event)

    }

}