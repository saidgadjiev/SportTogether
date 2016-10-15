package ru.mail.sporttogether.mvp.presenters.event

import ru.mail.sporttogether.mvp.presenters.IPresenter

/**
 * Created by bagrusss on 07.10.16.
 *
 */
interface AddEventPresenter : IPresenter {
    fun addEventClicked(name: String, categoryId: Long, lat: Double, lng: Double)

    fun searchCategory(category: String)

    fun loadCategories()
}