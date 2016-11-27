package ru.mail.sporttogether.data.binding.tasks

/**
 * Created by Ivan on 27.11.2016.
 */
interface AddTasksListener {
    fun onAddTaskClicked()
    fun onRemoveTaskClicked()
    fun onOpenTasksClicked()
    fun onCloseTasksClicked()
}