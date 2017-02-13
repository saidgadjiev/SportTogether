package ru.mail.sporttogether.activities

import android.os.Bundle

import ru.mail.sporttogether.R
import ru.mail.sporttogether.mvp.NewAddEventPresenter
import ru.mail.sporttogether.mvp.NewAddEventView
import ru.mail.sporttogether.mvp.PresenterActivity

class NewAddActivity :
        PresenterActivity<NewAddEventPresenter>(),
        NewAddEventView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_add)
    }
}
