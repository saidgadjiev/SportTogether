package ru.mail.sporttogether.mvp.presenters.event

import android.os.Bundle
import ru.mail.sporttogether.mvp.presenters.IPresenter
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 07.10.16.
 *
 */
interface AddEventPresenter : IPresenter {

    fun addEventClicked(event: Event, addMeNow: Boolean)

    fun searchCategory(category: String)

    fun loadCategories()

    fun loadCategoriesBySubname(subname: String)

    fun sendResult(id: Long, result: String)

    fun onCreate(args: Bundle?, event: Event) {
        super.onCreate(args)
    }

}