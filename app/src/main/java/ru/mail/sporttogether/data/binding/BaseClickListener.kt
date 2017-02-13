package ru.mail.sporttogether.data.binding

import android.view.View

/**
 * Created by bagrusss on 12.02.17
 */
interface BaseClickListener {
    //nice, kotlin, i love you
    fun call(v: View, func: () -> Unit) {
        v.isEnabled = false
        func()
        v.isEnabled = true
    }
}