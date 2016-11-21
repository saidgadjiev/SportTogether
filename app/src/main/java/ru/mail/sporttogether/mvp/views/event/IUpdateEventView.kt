package ru.mail.sporttogether.mvp.views.event

import android.widget.Toast
import ru.mail.sporttogether.mvp.views.IView

interface IUpdateEventView : IView {
    fun onResultUpdated() {
        showToast("Итог успешно добавлен!", Toast.LENGTH_SHORT)
    }
}