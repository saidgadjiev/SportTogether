package ru.mail.sporttogether.fragments

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import com.jakewharton.rxbinding.widget.RxTextView
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.NewAddActivity
import ru.mail.sporttogether.adapter.CategoriesAdapter
import ru.mail.sporttogether.data.binding.FillEventData
import ru.mail.sporttogether.data.binding.ToolbarWithButtonData
import ru.mail.sporttogether.databinding.FragmentFillEventBinding
import ru.mail.sporttogether.fragments.presenter.FillEventPresenter
import ru.mail.sporttogether.fragments.presenter.FillEventPresenterImpl
import ru.mail.sporttogether.fragments.view.FillEventView
import ru.mail.sporttogether.net.CategoriesResponse
import ru.mail.sporttogether.net.models.Category
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.Subscriptions
import java.util.*
import java.util.regex.Pattern

/**
 * Created by bagrusss on 14.02.17
 */
class FillEventFragment : AbstractMapFragment<FillEventPresenter>(), FillEventView {

    private lateinit var binding: FragmentFillEventBinding
    private val data = FillEventData()
    private val toolbarData = ToolbarWithButtonData()

    private var time = 0L

    private val namePattern = Pattern.compile("([а-я]+[ |-])*[а-я]+", Pattern.CASE_INSENSITIVE)

    private lateinit var toolbar: Toolbar
    private lateinit var nameInputLayout: TextInputLayout
    private lateinit var sportInputLayout: TextInputLayout
    private lateinit var peopleInputLayout: TextInputLayout
    private lateinit var sportAutoComplete: AutoCompleteTextView

    private var sportSubscription = Subscriptions.empty()

    private lateinit var categoriesAdapter: CategoriesAdapter
    private var categoriesArray: ArrayList<Category> = ArrayList(5)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFillEventBinding.inflate(inflater, container, false)
        binding.addEventData = data
        binding.toolbarData = toolbarData

        toolbar = binding.toolbar
        setupToolbar(toolbar)
        toolbarData.buttonIsVisible.set(true)
        toolbarData.buttonText.set(getString(R.string.next))
        toolbar.title = getString(R.string.add_event)


        presenter = FillEventPresenterImpl(this)

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(presenter)


        nameInputLayout = binding.include.nameInputLayout
        sportInputLayout = binding.include.sportTextInput
        peopleInputLayout = binding.include.peopleTextInput
        sportAutoComplete = binding.include.editSport

        sportSubscription = RxTextView.textChangeEvents(sportAutoComplete)
                .filter { e -> e.count() == 3 }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { e ->
                    presenter.loadCategoriesBySubname(data.sport.get())
                }


        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        categoriesAdapter = CategoriesAdapter(context, android.R.layout.select_dialog_item, categoriesArray)
        sportAutoComplete.setAdapter(categoriesAdapter)

    }

    override fun onStart() {
        super.onStart()
        binding.toolbarListener = this
    }

    override fun onStop() {
        binding.toolbarListener = null
        super.onStop()
    }

    override fun updateAddress(address: String) {
        data.address.set(address)
    }

    override fun nextStep() {
        val act = activity
        if (isAdded && act is NewAddActivity) {
            act.nextStep()
        }
    }

    override fun onCategoriesLoaded(data: CategoriesResponse) {
        categoriesAdapter.clear()
        categoriesAdapter.addAll(data)
    }


    private fun validate(): Boolean {
        var res = true
        val name = data.name.get()
        if (name.isNullOrEmpty()) {
            nameInputLayout.error = "Поле обязательно для заполнения"
            res = false
        } else {
            val matcher = namePattern.matcher(name)
            if (!matcher.matches()) {
                nameInputLayout.error = "Допустимы русские буквы, пробелы и дефис"
                res = false
            }
        }

        val sport = data.sport.get()
        if (sport.isNullOrEmpty()) {
            sportInputLayout.error = "Поле обязательно для заполнения"
            res = false
        } else {
            val matcher = namePattern.matcher(sport)
            if (!matcher.matches()) {
                sportInputLayout.error = "Допустимы русские буквы, пробелы и дефис"
                res = false
            }
        }

        val people = data.maxPeople.get()
        if (people.isNullOrEmpty()) {
            peopleInputLayout.error = "Поле обязательно для заполнения"
            res = false
        }


        return res
    }

    override fun onNextClicked() {
        if (validate()) {
            presenter.fillEvent(
                    data.name.get(),
                    data.sport.get(),
                    data.maxPeople.get().toInt(),
                    data.description.get(),
                    time,
                    data.joinToEvent.get(),
                    data.needAddTemplate.get())
        }
    }


}