package ru.mail.sporttogether.data.binding.event

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import ru.mail.sporttogether.net.models.User

/**
 * Created by bagrusss on 20.10.16
 */
class EventDetailsData {
    val name = ObservableField<String>()
    val date = ObservableField<String>()
    val isJoined = ObservableBoolean()
    val isReported = ObservableBoolean()
    val peopleCount = ObservableField<String>()
    val description = ObservableField<String>()
    val isEnded = ObservableBoolean()
    val result = ObservableField<String>()
    val category = ObservableField<String>()

    val tasksInfo = ObservableField<String>()
    val tasksMessage = ObservableField<String>()
    val isTasksCanBeChanged = ObservableBoolean()
    val withTasks = ObservableBoolean()

    val reports = ObservableField<String>()
    val showCancelButton = ObservableBoolean(false)

    val fabForBottomSheet = ObservableBoolean(false)

    val address = ObservableField<String>()
}