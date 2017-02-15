package ru.mail.sporttogether.fragments

import android.os.Bundle
import android.support.design.widget.TextInputLayout
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
        val matcher = namePattern.matcher(data.name.get())
        if (!matcher.matches()) {
            nameInputLayout.error = "В названии допустимы только русские буквы, пробелы и \'-\'"
            return false
        }
        return true
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