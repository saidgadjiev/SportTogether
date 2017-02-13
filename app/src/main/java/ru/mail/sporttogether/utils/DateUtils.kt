package ru.mail.sporttogether.utils

import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Ivan on 15.11.2016
 */
object DateUtils {
    val ONE_MINUTE: Long = 60000L
    val ONE_MONTH: Long = 30 * 24 * 60 * 60000L

    fun nextMonthCalendar(): Calendar {
        val c = Calendar.getInstance()
        c.timeInMillis = c.timeInMillis + ONE_MONTH
        return c
    }

    fun toLongDateString(calendar: GregorianCalendar): String {
        val fmt = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        fmt.calendar = calendar
        val dateFormatted = fmt.format(calendar.time)
        return dateFormatted
    }

    fun toXLongDateString(date: Date): String {
        val fmt = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault())
        val dateFormatted = fmt.format(date)
        return dateFormatted
    }

    fun toDateWithoutYearString(date: Date): String {
        val fmt = SimpleDateFormat("HH:mm, dd MMM", Locale.getDefault())
        val dateFormatted = fmt.format(date)
        return dateFormatted
    }

    fun toDayMonthString(date: Date): String {
        val fmt = SimpleDateFormat("dd MMM", Locale.getDefault())
        val dateFormatted = fmt.format(date)
        return dateFormatted
    }

    fun toLongDayMonthString(date: Date): String {
        val fmt = SimpleDateFormat("dd MMMM", Locale.getDefault())
        val dateFormatted = fmt.format(date)
        return dateFormatted
    }

    fun longToDateTime(date: Long): String
            = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault()).format(Date(date))

    fun startOfDay(cal: Calendar): Calendar {
        cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY))
        cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE))
        cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND))
        cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND))
        return cal
    }

    fun endOfDay(cal: Calendar): Calendar {
        cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY))
        cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE))
        cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND))
        cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND))
        return cal
    }
}