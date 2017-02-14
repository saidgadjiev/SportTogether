package ru.mail.sporttogether.data.binding

import android.view.View

/**
 * Created by bagrusss on 14.02.17
 */
interface AddEventListener : BaseClickListener {

    fun onDateViewClicked(v: View) {
        call(v, { onDateViewClicked() })
    }

    fun onDateViewClicked()

}