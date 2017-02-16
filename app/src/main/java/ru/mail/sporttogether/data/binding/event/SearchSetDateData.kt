package ru.mail.sporttogether.data.binding.event

import android.databinding.ObservableBoolean
import android.databinding.ObservableField

/**
 * Created by root on 13.02.17.
 */
class SearchSetDateData {
    val isDateSetted = ObservableBoolean()
    val interval = ObservableField<String>()

    companion object {
        val TAG = SearchSetDateData::class.java.simpleName
    }
}