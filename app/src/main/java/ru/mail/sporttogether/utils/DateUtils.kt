package ru.mail.sporttogether.utils

import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Ivan on 15.11.2016.
 */
class DateUtils {
    companion object DateUtils {
        fun format(calendar: GregorianCalendar): String {
            val fmt = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            fmt.setCalendar(calendar)
            val dateFormatted = fmt.format(calendar.getTime())
            return dateFormatted
        }
    }
}