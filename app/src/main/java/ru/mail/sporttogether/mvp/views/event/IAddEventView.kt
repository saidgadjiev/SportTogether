package ru.mail.sporttogether.mvp.views.event

import ru.mail.sporttogether.mvp.views.IView
import ru.mail.sporttogether.net.models.Category
import java.util.*

/**
 * Created by bagrusss on 15.10.16.
 *
 */
interface IAddEventView : IView {
    fun onEventAdded(name: String)

    fun showAddError(errorText: String)

    fun onCategoriesReady(categories: ArrayList<Category>)

    fun onCategoriesLoaded(categories: ArrayList<Category>)

    fun visibleCategoryProgressBar()
    fun invisibleCategoryProgressBar()

    fun resultSended()
}