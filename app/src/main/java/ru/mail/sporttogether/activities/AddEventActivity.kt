package ru.mail.sporttogether.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import ru.mail.sporttogether.R
import ru.mail.sporttogether.data.binding.event.EventData
import ru.mail.sporttogether.data.binding.event.EventListener
import ru.mail.sporttogether.databinding.ActivityAddEventBinding
import ru.mail.sporttogether.mvp.presenters.event.AddEventPresenter
import ru.mail.sporttogether.mvp.presenters.event.AddEventPresenterImpl
import ru.mail.sporttogether.mvp.views.event.IAddEventView
import ru.mail.sporttogether.net.models.Category
import java.util.*

class AddEventActivity : PresenterActivity<AddEventPresenter>(), IAddEventView, EventListener {

    private lateinit var binding: ActivityAddEventBinding
    private val data = EventData()

    private lateinit var lat: String
    private lateinit var lng: String

    private val handler = Handler()
    private lateinit var categoryText: AutoCompleteTextView
    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AddEventPresenterImpl(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_event)
        binding.data = data
        with(intent) {
            lat = getDoubleExtra(KEY_LAT, 0.0).toString()
            lng = getDoubleExtra(KEY_LNG, 0.0).toString()
        }
        setupCoordinates()
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice)
        categoryText = binding.category
        presenter.loadCategories()
    }

    override fun onCategoriesReady(categories: ArrayList<Category>) {
        arrayAdapter.clear()
        arrayAdapter.addAll(categories)
    }

    override fun onStart() {
        super.onStart()
        binding.listener = this
    }

    override fun onStop() {
        super.onStop()
        binding.listener = null
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    private fun setupCoordinates() {
        data.lat.set(lat)
        data.lng.set(lng)
    }

    override fun onAddButtonClicked() {
        val name = binding.eventName.text.toString()
        val category = binding.category.text.toString()
        presenter.addEventClicked(name, 10, lat.toDouble(), lng.toDouble())
    }

    override fun onEventAdded(name: String) {
        showToast("added")
        handler.postDelayed({
            finish()
        }, 1000)
    }

    override fun showAddError(errorText: String) {
        showToast(errorText)
    }

    companion object {

        @JvmStatic
        fun start(c: Context, lng: Double, lat: Double) {
            val intent = Intent(c, AddEventActivity::class.java)
            intent.putExtra(KEY_LNG, lng)
            intent.putExtra(KEY_LAT, lat)
            c.startActivity(intent)
        }

        @JvmStatic private val KEY_LNG = "lng"
        @JvmStatic private val KEY_LAT = "lat"
    }
}
