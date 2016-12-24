package ru.mail.sporttogether.utils

import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Ivan on 15.11.2016.
 */
class DateUtils {
    companion object DateUtils {
        fun toLongDateString(calendar: GregorianCalendar): String {
            val fmt = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            fmt.setCalendar(calendar)
            val dateFormatted = fmt.format(calendar.getTime())
            return dateFormatted
        }

        fun toXLongDateString(date: Date): String {
            val fmt = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault())
            val dateFormatted = fmt.format(date)
            return dateFormatted
        }
    }
}