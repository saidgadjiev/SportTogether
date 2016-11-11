package ru.mail.sporttogether.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import android.widget.Toast
import com.jakewharton.rxbinding.widget.RxTextView
import com.mikepenz.materialdrawer.util.KeyboardUtil
import ru.mail.sporttogether.R
import ru.mail.sporttogether.adapter.CategoriesAdapter
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
        EventListener {

    private lateinit var binding: ActivityAddEventBinding

    private val data = EventData()
    private lateinit var lat: String

    private lateinit var lng: String
    private val handler = Handler()
    private var eventId = 0L

    private lateinit var categoryAutocomplete: AutoCompleteTextView
    private var categoriesArray: ArrayList<Category> = ArrayList()
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var loadingCategoriesProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AddEventPresenterImpl(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_event)
        binding.data = data

        val intent = intent

        eventId = intent.getLongExtra(KEY_ID, 0L)
        if (eventId != 0L) {
            data.resultVisibility.set(true)
            data.mainDataVisibility.set(false)
            return
        }

        with(intent) {
            lat = getDoubleExtra(KEY_LAT, 0.0).toString()
            lng = getDoubleExtra(KEY_LNG, 0.0).toString()
        }
        setupCoordinates()
        presenter.loadCategories()


//        categoriesArray.add(Category(1, "cat1"))
//        categoriesArray.add(Category(2, "category"))
//        categoriesArray.add(Category(3, "my_category"))
//        categoriesArray.add(Category(4, "test_c"))
//        categoriesArray.add(Category(5, "cag"))
        categoriesAdapter = CategoriesAdapter(this, android.R.layout.select_dialog_item, categoriesArray)
        categoryAutocomplete = binding.categoryAutocomplete
        categoryAutocomplete.setAdapter(categoriesAdapter)
//        categoriesAdapter = ArrayAdapter(this, android.R.layout.select_dialog_item)
//        categoriesAdapter.setNotifyOnChange(true)
        loadingCategoriesProgressBar = binding.categoryAutocompleteProgressBar
//        categoryAutocomplete.setAdapter(categoriesAdapter)

        RxTextView.textChangeEvents(categoryAutocomplete)
                .filter { e -> e.count() == 3 }
                .subscribe { e ->
                    Log.d("#MY " + javaClass.simpleName, "start loading categories. subname : " + e.text())
                    visibleCategoryProgressBar()
                    presenter.loadCategoriesBySubname(e.text().toString())
                }
    }

    override fun onCategoriesReady(categories: ArrayList<Category>) {

    }

    override fun onCategoriesLoaded(categories: ArrayList<Category>) {
        Log.d("#MY " + javaClass.simpleName, "in activity update adapter. Categories size : " + categoriesArray.size)
        categories.forEach { e -> Log.d("#MY " + javaClass.simpleName, "loaded category : " + e.name) }
        categoriesAdapter.clear()
        categoriesAdapter.addAll(categories)
    }

    override fun visibleCategoryProgressBar() {
        loadingCategoriesProgressBar.visibility = View.VISIBLE
    }

    override fun invisibleCategoryProgressBar() {
        loadingCategoriesProgressBar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        binding.listener = this
    }

    override fun onStop() {
        super.onStop()
        binding.listener = null
    }

    override fun onPause() {
        super.onPause()
        KeyboardUtil.hideKeyboard(this)
    }

    private fun setupCoordinates() {
        data.lat.set(lat)
        data.lng.set(lng)
    }

    override fun onAddButtonClicked() {
        if (data.resultVisibility.get()) {
            presenter.sendResult(eventId, binding.description.text.toString())
            return
        }
        val datepicker = binding.datePicker
        val timepicker = binding.timePicker
        val calendar = GregorianCalendar(
                datepicker.year,
                datepicker.month,
                datepicker.dayOfMonth,
                timepicker.currentHour,
                timepicker.currentMinute)
        val name = binding.eventName.text.toString()
        val nameCategory: String = binding.categoryAutocomplete.text.toString()
        Log.d("#MY " + javaClass.simpleName, "category name : " + nameCategory)

        if (nameCategory == "") {
            Toast.makeText(this, "Категория не задана", Toast.LENGTH_SHORT).show()
            return
        }
        val maxPeople = Integer.parseInt(binding.eventMaxPeople.text.toString())
        Log.d("#MY " + javaClass.simpleName, "max people : " + maxPeople)
        Log.d("#MY " + javaClass.simpleName, "lat : " + lat)
        Log.d("#MY " + javaClass.simpleName, "lon : " + lng)

        presenter.addEventClicked(name,
                nameCategory,
                calendar.time,
                lat.toDouble(),
                lng.toDouble(),
                binding.description.text.toString(),
                maxPeople)
    }

    override fun onEventAdded(name: String) {
        showToast(R.string.event_added)
        handler.postDelayed({
            finish()
        }, 300)
    }

    override fun showAddError(errorText: String) {
        showToast(errorText)
    }

    override fun resultSended() {
        showToast("ok")
        handler.postDelayed({
            finish()
        }, 300)
    }

    companion object {

        @JvmStatic
        fun start(c: Context, lng: Double, lat: Double) {
            val intent = Intent(c, AddEventActivity::class.java)
            intent.putExtra(KEY_LNG, lng)
            intent.putExtra(KEY_LAT, lat)
            c.startActivity(intent)
        }

        @JvmStatic
        fun startForResultEvent(c: Context, id: Long) {
            val intent = Intent(c, AddEventActivity::class.java)
            intent.putExtra(KEY_ID, id)
            c.startActivity(intent)
        }

        @JvmStatic private val KEY_ID = "ID"
        @JvmStatic private val KEY_LNG = "lng"
        @JvmStatic private val KEY_LAT = "lat"
    }
}
