package ru.mail.sporttogether.data.binding

import android.databinding.ObservableBoolean
import android.databinding.ObservableField

/**
 * Created by bagrusss on 12.02.17
 */
class SelectAddressData {
    val isAddressLoading = ObservableBoolean(false)
    val address = ObservableField<String>()
    val userImage = ObservableField<String>()
}