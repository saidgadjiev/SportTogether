package ru.mail.sporttogether.data.binding

import android.databinding.ObservableBoolean
import android.databinding.ObservableField

/**
 * Created by bagrusss on 12.02.17
 */
class ToolbarWithButtonData {
    val text = ObservableField<String>()
    val isVisible = ObservableBoolean(true)
}