package ru.mail.sporttogether.mvp.presenters.event

import ru.mail.sporttogether.mvp.presenters.IPresenter
import java.util.*

/**
 * Created by bagrusss on 07.10.16.
 *
 */
interface AddEventPresenter : IPresenter {

    fun addEventClicked(name: String,
                        categoryName: String,
                        date: Date,
                        lat: Double,
                        lng: Double,
                        description: String = "",
                        maxPeople: Int)

    fun searchCategory(category: String)

    fun loadCategories()

    fun loadCategoriesBySubname(subname: String)

}