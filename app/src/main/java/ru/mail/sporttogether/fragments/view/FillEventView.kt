package ru.mail.sporttogether.fragments.view

import ru.mail.sporttogether.data.binding.ToolbarWithButtonListener
import ru.mail.sporttogether.mvp.IView

/**
 * Created by bagrusss on 14.02.17
 */
interface FillEventView :
        IView,
        ToolbarWithButtonListener {
    fun updateAddress(address: String)
}