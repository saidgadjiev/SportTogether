package ru.mail.sporttogether.data.binding.items

import android.databinding.ObservableField

/**
 * Created by bagrusss on 07.02.17
 */
class TwoButtonItemData {
    val button1Text = ObservableField<String>()
    val button2Text = ObservableField<String>()

    val nameText = ObservableField<String>()
    val dateText = ObservableField<String>()
    val description = ObservableField<String>()
}