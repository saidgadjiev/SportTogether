package ru.mail.sporttogether.data.binding.tasks

import android.databinding.ObservableField

/**
 * Created by Ivan on 27.11.2016.
 */
class AddTasksData {
    val tasks = ObservableField<String>()
    val message = ObservableField<String>()
}