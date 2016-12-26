package ru.mail.sporttogether.mvp.views.event

import ru.mail.sporttogether.mvp.views.IView
import ru.mail.sporttogether.net.models.Category
import ru.mail.sporttogether.net.models.Event
import java.util.*

/**
 * Created by bagrusss on 15.10.16.
 *
 */
interface IAddEventView : IView {
    fun onEventAdded(event: Event)

    fun showAddError(errorText: String)

    fun onCategoriesReady(categories: ArrayList<Category>)

    fun onCategoriesLoaded(categories: ArrayList<Category>)

    fun visibleCategoryProgressBar()
    fun invisibleCategoryProgressBar()

    fun resultSended()

    fun updateAddress(textAddress: String)
}