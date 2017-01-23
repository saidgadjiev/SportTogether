package ru.mail.sporttogether.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import ru.mail.sporttogether.data.binding.OnReminderTimeListener
import ru.mail.sporttogether.databinding.FragmentSettingsBinding
import ru.mail.sporttogether.fragments.presenter.SettingsPresenter
import ru.mail.sporttogether.fragments.presenter.SettingsPresenterImpl
import ru.mail.sporttogether.mvp.PresenterFragment

/**
 * Created by root on 23.01.17.
 */
class SettingsFragment: PresenterFragment<SettingsPresenter>(), AdapterView.OnItemSelectedListener, OnReminderTimeListener {
    private lateinit var binding: FragmentSettingsBinding
    private var timeInMills: Long = 0L

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        presenter = SettingsPresenterImpl(this)
        binding.settingsReminderTimeSpinner.onItemSelectedListener = this
        binding.listener = this
        return binding.root
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.w("#MY SettingsFragment", "nothing selected")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("#MY SettingsFragment", "selected $position")
        timeInMills= (position + 1L) * 60L * 60L * 1000L
    }

    override fun save() {
        presenter.saveReminderTime(timeInMills)
    }
}