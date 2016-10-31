package ru.mail.sporttogether.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import com.jakewharton.rxbinding.widget.RxTextView
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
        EventListener {
    private lateinit var binding: ActivityAddEventBinding

    private val data = EventData()
    private lateinit var lat: String

    private lateinit var lng: String
    private val handler = Handler()

    private lateinit var categorySpinner: Spinner
    private lateinit var arrayAdapter: ArrayAdapter<Category>

    private lateinit var categoryAutocomplete: AutoCompleteTextView
    private lateinit var categoriesAdapter: ArrayAdapter<Category>
    private lateinit var loadingCategoriesProgressBar: ProgressBar


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
        categorySpinner = binding.categorySpinner
        presenter.loadCategories()
        categorySpinner.adapter = arrayAdapter

        categoriesAdapter = ArrayAdapter(this, android.R.layout.select_dialog_item)
        categoryAutocomplete = binding.categoryAutocomplete
        loadingCategoriesProgressBar = binding.categoryAutocompleteProgressBar
        categoryAutocomplete.setAdapter(categoriesAdapter)

        RxTextView.textChangeEvents(categoryAutocomplete)
                .filter {e -> e.count() >= 3}
                .subscribe { e ->
                    visibleCategoryProgressBar()
                    Log.d("#MY " + javaClass.simpleName, "start loading categories. subname : " + e.text().toString())
                    presenter.loadCategoriesBySubname(e.text().toString())
                    categoryAutocomplete.dismissDropDown()
                }
    }

    override fun onCategoriesReady(categories: ArrayList<Category>) {
        arrayAdapter.clear()
        arrayAdapter.addAll(categories)
        binding.categorySpinner.setSelection(0)
    }

    override fun onCategoriesLoaded(categories: ArrayList<Category>) {
        Log.d("#MY " + javaClass.simpleName, "in activity update adapter. Categories size : " + categories.size)
        categories.forEach{e -> Log.d("#MY " + javaClass.simpleName, e.toString())}
        categoriesAdapter.clear()
        categoriesAdapter.addAll(categories)
        categoryAutocomplete.setAdapter(categoriesAdapter)
        categoryAutocomplete.showDropDown()
        Log.d("#MY " + javaClass.simpleName, "size of autocomplete after adding : " + categoryAutocomplete.length())

//        Handler().postDelayed({
//            Log.d("#MY " + javaClass.simpleName, "when show dropdown categories size : " + categoriesAdapter.count)
//            invisibleCategoryProgressBar()
//        }, 1000)
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
        presenter.addEventClicked(name,
                (categorySpinner.selectedItem as Category).id ?: 0,
                lat.toDouble(),
                lng.toDouble(),
                binding.description.text.toString())
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
