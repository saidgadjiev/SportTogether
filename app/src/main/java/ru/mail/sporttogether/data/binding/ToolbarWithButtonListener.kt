package ru.mail.sporttogether.data.binding

import android.view.View

/**
 * Created by bagrusss on 12.02.17
 */
interface ToolbarWithButtonListener : BaseClickListener {
    fun onNextClicked(v: View) {
        call(v, { onNextClicked() })
    }

    fun onNextClicked()
}