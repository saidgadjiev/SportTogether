package ru.mail.sporttogether.data.binding

import android.view.View

/**
 * Created by bagrusss on 12.02.17
 */
interface EventDetailListener {

    //nice, kotlin, i love you
    private fun call(v: View, func: () -> Unit) {
        v.isEnabled = false
        func()
        v.isEnabled = true
    }

    fun onFabClicked(v: View) {
        call(v, { onFabClicked() })
    }

    fun onUsersClicked(v: View) {
        call(v, { onUsersClicked() })
    }

    fun onFabClicked()
    fun onUsersClicked()
}