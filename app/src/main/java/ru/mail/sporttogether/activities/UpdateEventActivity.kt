package ru.mail.sporttogether.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.mikepenz.materialdrawer.util.KeyboardUtil
import ru.mail.sporttogether.R
import ru.mail.sporttogether.databinding.ActivityAddEventBinding
import ru.mail.sporttogether.mvp.presenters.event.UpdateEventPresenter
import ru.mail.sporttogether.mvp.presenters.event.UpdateEventPresenterImpl
import ru.mail.sporttogether.mvp.views.event.IUpdateEventView

class UpdateEventActivity :
        PresenterActivity<UpdateEventPresenter>(),
        IUpdateEventView {


    private lateinit var binding: ActivityAddEventBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = UpdateEventPresenterImpl(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_event)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        KeyboardUtil.hideKeyboard(this)
    }

    companion object {
        @JvmStatic
        fun start(c: Context) {
            val intent = Intent(c, AddEventActivity::class.java)
            c.startActivity(intent)
        }
    }
}
