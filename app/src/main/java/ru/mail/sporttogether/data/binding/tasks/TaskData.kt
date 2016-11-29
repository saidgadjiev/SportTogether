package ru.mail.sporttogether.data.binding.tasks

import android.databinding.ObservableBoolean
import android.databinding.ObservableField

class TaskData {
    val id = ObservableField<String>()
    val message = ObservableField<String>()
    val isChecked = ObservableBoolean()
}