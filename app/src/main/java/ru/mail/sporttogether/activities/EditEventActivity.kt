package ru.mail.sporttogether.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.mikepenz.materialize.util.KeyboardUtil
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.presenter.EditEventActivityPresenter
import ru.mail.sporttogether.activities.presenter.EditEventActivityPresenterImp
import ru.mail.sporttogether.activities.view.EditEventView
import ru.mail.sporttogether.data.binding.event.EditEventData
import ru.mail.sporttogether.data.binding.event.EditEventListener
import ru.mail.sporttogether.databinding.ActivityEditEventMinimalBinding
import ru.mail.sporttogether.mvp.PresenterActivity
import ru.mail.sporttogether.net.models.Event

class EditEventActivity :
        PresenterActivity<EditEventActivityPresenter>(),
        EditEventView,
        EditEventListener {

    private lateinit var binding: ActivityEditEventMinimalBinding
    private lateinit var event: Event
    private val data = EditEventData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_event_minimal)
        binding.data = data

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        presenter = EditEventActivityPresenterImp(this)


        intent.getParcelableExtra<Event>(KEY_EVENT)?.let {
            event = it
            data.name.set(event.name)
            data.peopleCoint.set(event.maxPeople.toString())
            data.description.set(event.description)
        }
    }

    override fun onPause() {
        super.onPause()
        KeyboardUtil.hideKeyboard(this)
    }

    override fun onStart() {
        super.onStart()
        binding.listener = this
    }

    override fun onStop() {
        super.onStop()
        binding.listener = null
    }

    override fun cancelEventClicked() {
        presenter.cancelEvent(event)
    }

    override fun saveEventClicked() {
        presenter.updateEvent(event)
    }

    override fun dateTimeClicked() {

    }

    override fun eventEdited() {
        showToast(R.string.event_edited)
        finish()
    }

    override fun eventCancelled() {
        showToast(R.string.event_canceled)
        finish()
    }

    override fun onError() {
        showToast(R.string.error)
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
