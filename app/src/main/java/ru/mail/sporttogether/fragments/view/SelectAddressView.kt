package ru.mail.sporttogether.fragments.view

import com.google.android.gms.maps.GoogleMap
import ru.mail.sporttogether.mvp.IView

/**
 * Created by bagrusss on 12.02.17
 */
interface SelectAddressView : IView, GoogleMap.OnCameraIdleListener {

    fun updateAddress(address: String)
    fun onAddressSaved()

}