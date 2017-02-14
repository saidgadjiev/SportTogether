package ru.mail.sporttogether.activities.presenter

import android.os.Bundle
import ru.mail.sporttogether.mvp.IPresenter
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 07.10.16.
 *
 */
interface AddEventPresenter : IPresenter {

    fun addEventClicked(event: Event, addMeNow: Boolean, addTemplate: Boolean = false)

    fun searchCategory(category: String)

    fun loadCategoriesBySubname(subname: String)

    fun sendResult(id: Long, result: String)

    fun onCreate(args: Bundle?, event: Event) {
        super.onCreate(args)
    }

    fun updateAddress()

}