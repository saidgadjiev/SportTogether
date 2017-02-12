package ru.mail.sporttogether.fragments.view

import ru.mail.sporttogether.mvp.IView
import ru.mail.sporttogether.net.models.Event
import java.util.*

/**
 * Created by bagrusss on 05.02.17
 */
interface TemplatesView : IView {
    fun swapTemplates(templates: LinkedList<Event>)
}