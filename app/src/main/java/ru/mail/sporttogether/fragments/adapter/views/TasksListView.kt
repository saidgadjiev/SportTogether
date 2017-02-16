package ru.mail.sporttogether.fragments.adapter.views

import android.view.View
import ru.mail.sporttogether.data.binding.ToolbarWithButtonListener

/**
 * Created by bagrusss on 14.02.17
 */
interface TasksListView : ToolbarWithButtonListener, View.OnClickListener {
    fun taskAdded(task: String)
    fun hideFab()
    fun showFab()
}