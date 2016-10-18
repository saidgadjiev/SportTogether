package ru.mail.sporttogether.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import ru.mail.sporttogether.R
import ru.mail.sporttogether.data.binding.event.EventData
import ru.mail.sporttogether.data.binding.event.EventListener
import ru.mail.sporttogether.databinding.ActivityAddEventBinding
import ru.mail.sporttogether.mvp.presenters.event.AddEventPresenter
import ru.mail.sporttogether.mvp.presenters.event.AddEventPresenterImpl
import ru.mail.sporttogether.mvp.views.event.IAddEventView
import ru.mail.sporttogether.net.models.Category
import java.util.*

class AddEventActivity :
        PresenterActivity<AddEventPresenter>(),
        IAddEventView,
        EventListener,
        AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityAddEventBinding
    private val data = EventData()

    private lateinit var lat: String
    private lateinit var lng: String

    private val handler = Handler()
    private lateinit var categoryText: Spinner
    private lateinit var arrayAdapter: ArrayAdapter<Category>
    private var selectedCategory = -1L

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
        arrayAdapter = ArrayAdapter(this, android.R.layout.select_dialog_item)
        categoryText = binding.categorySpinner
        presenter.loadCategories()
        categoryText.adapter = arrayAdapter
    }

    override fun onCategoriesReady(categories: ArrayList<Category>) {
        arrayAdapter.clear()
        arrayAdapter.addAll(categories)
    }

    override fun onStart() {
        super.onStart()
        binding.categorySpinner.onItemSelectedListener = this
        binding.listener = this
    }

    override fun onStop() {
        super.onStop()
        binding.categorySpinner.onItemSelectedListener = null
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
        if (selectedCategory != -1L) {
            val name = binding.eventName.text.toString()
            presenter.addEventClicked(name, selectedCategory, lat.toDouble(), lng.toDouble())
        } else showToast("Please, select category")
    }

    override fun onEventAdded(name: String) {
        showToast(R.string.event_added)
        handler.postDelayed({
            finish()
        }, 1000)
    }

    override fun showAddError(errorText: String) {
        showToast(errorText)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedCategory = position.toLong()
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
