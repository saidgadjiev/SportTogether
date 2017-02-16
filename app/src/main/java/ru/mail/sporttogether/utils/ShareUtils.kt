package ru.mail.sporttogether.utils

import android.content.Intent
import android.net.Uri
import ru.mail.sporttogether.net.models.Event


/**
 * Created by bagrusss on 22.01.17
 */
object ShareUtils {
    val PARAM_LAT = "latitude"
    val PARAM_LNG = "longtitude"
    val PARAM_ID = "id"
    val PARAMS: MutableList<String> = arrayOf(PARAM_ID, PARAM_LAT, PARAM_LNG).toMutableList()

    fun buildShareString(event: Event)
            = StringBuilder(event.name).append('\n')
                                       .append('\n')
                                       .append(event.result ?: event.description)
                                       .append('\n')
                                       .append('\n')
                                       .append(buildLink(event))
                                       .toString()

    fun buildLink(event: Event): String {
        val builder = Uri.Builder()
        builder.scheme("http")
                .authority("p30281.lab1.stud.tech-mail.ru")
                .appendPath("app")
                .appendQueryParameter(PARAM_LAT, event.lat.toString())
                .appendQueryParameter(PARAM_LNG, event.lng.toString())
                .appendQueryParameter(PARAM_ID, event.id.toString())
        return builder.build().toString()
    }

    fun createShareIntent(event: Event, title:String): Intent? {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, buildShareString(event))
        sendIntent.type = "text/plain"
        return Intent.createChooser(sendIntent, title)
    }
}