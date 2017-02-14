package ru.mail.sporttogether.fragments.presenter

import ru.mail.sporttogether.mvp.MapPresenter


/**
 * Created by bagrusss on 12.02.17
 */
abstract class SelectAddressFragmentPresenter : MapPresenter() {

    abstract fun getUserImgUrl(): String
    abstract fun updateLocation(lat: Double, lng: Double)
    abstract fun saveAddress(address: String)

}