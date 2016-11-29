package ru.mail.sporttogether.fragments

import ru.mail.sporttogether.net.models.Task

/**
 * Created by Ivan on 28.11.2016.
 */
interface CheckingTasks {
    fun checkTask(task: Task)
    fun uncheckTask(task: Task)
}