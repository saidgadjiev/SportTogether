package ru.mail.sporttogether.fragments

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.R
import ru.mail.sporttogether.data.binding.FillEventData
import ru.mail.sporttogether.data.binding.ToolbarWithButtonData
import ru.mail.sporttogether.databinding.FragmentFillEventBinding
import ru.mail.sporttogether.fragments.presenter.FillEventPresenter
import ru.mail.sporttogether.fragments.presenter.FillEventPresenterImpl
import ru.mail.sporttogether.fragments.view.FillEventView
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
        return binding.root
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

    private fun validate(): Boolean {
        var res = true
        /*val name = data.name.get()
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
        }

        val people = data.maxPeople.get()

        if (people.isNullOrEmpty()) {
            sportInputLayout.error = "Поле обязательно для заполнения"
            res = false
        }*/


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