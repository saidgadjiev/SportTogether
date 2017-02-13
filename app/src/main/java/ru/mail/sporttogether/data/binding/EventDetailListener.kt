package ru.mail.sporttogether.data.binding

import android.view.View

/**
 * Created by bagrusss on 12.02.17
 */
interface EventDetailListener : BaseClickListener {
    
    fun onFabClicked(v: View) {
        call(v, { onFabClicked() })
    }

    fun onUsersClicked(v: View) {
        call(v, { onUsersClicked() })
    }

    fun onFabClicked()
    fun onUsersClicked()
}