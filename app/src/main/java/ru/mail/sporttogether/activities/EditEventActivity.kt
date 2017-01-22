package ru.mail.sporttogether.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.presenter.EditEventActivityPresenter
import ru.mail.sporttogether.activities.presenter.EditEventActivityPresenterImp
import ru.mail.sporttogether.activities.view.EditEventView
import ru.mail.sporttogether.databinding.ActivityEditEventBinding
import ru.mail.sporttogether.net.models.Event

class EditEventActivity :
        PresenterActivity<EditEventActivityPresenter>(),
        EditEventView {

    private lateinit var binding: ActivityEditEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_event)

        presenter = EditEventActivityPresenterImp(this)
    }

    companion object {

        @JvmStatic val KEY_EVENT = "EVENT"

        @JvmStatic
        fun start(c: Context, e: Event) {
            val intent = Intent(c, EditEventActivity::class.java)
            intent.putExtra(KEY_EVENT, e)
            c.startActivity(intent)
        }
    }
}
