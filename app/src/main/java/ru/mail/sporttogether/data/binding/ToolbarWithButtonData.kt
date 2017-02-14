package ru.mail.sporttogether.data.binding

import android.databinding.ObservableBoolean
import android.databinding.ObservableField

/**
 * Created by bagrusss on 12.02.17
 */
class ToolbarWithButtonData {
    val buttonText = ObservableField<String>()
    val buttonIsVisible = ObservableBoolean(true)
}