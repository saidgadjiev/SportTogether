package ru.mail.sporttogether.utils

import android.content.Intent
import ru.mail.sporttogether.dagger.modules.RetrofitModule
import ru.mail.sporttogether.net.models.Event


/**
 * Created by bagrusss on 22.01.17
 */
object ShareUtils {

    fun buildShareString(event: Event)
            = StringBuilder(event.name).append('\n')
                                       .append('\n')
                                       .append(event.category.name)
                                       .append('\n')
                                       .append('\n')
                                       .append(event.description)
                                       .append('\n')
                                       .append('\n')
                                       .append(RetrofitModule.BASE_URL)
                                       .append("event/")
                                       .append(event.id)
                                       .toString()

    fun createShareIntent(event: Event, title:String): Intent? {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, buildShareString(event))
        sendIntent.type = "text/plain"
        return Intent.createChooser(sendIntent, title)
    }
}