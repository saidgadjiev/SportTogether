package ru.mail.sporttogether.fragments.view

import ru.mail.sporttogether.mvp.IView

/**
 * Created by bagrusss on 12.02.17
 */
interface SelectAddressView : IView, OnMa {

    fun updateAddress(address: String)

}