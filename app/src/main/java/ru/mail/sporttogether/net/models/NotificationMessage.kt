package ru.mail.sporttogether.net.models

/**
 * Created by Ivan on 15.11.2016.
 */
class NotificationMessage {
    var type: Int = -1
    var title: String = ""
    var message: String = ""

    fun isValid(): Boolean {
        return title.isNotEmpty() && message.isNotEmpty()
    }

    companion object NotificationMessage {
        val EVENT_CANCELED = 0 //напоминание после отмены обытия
        val EVENT_RESULT = 1 //напоминание после добавления итога события
        val EVENT_REMIND = 2 //напоминание за час
    }

}
