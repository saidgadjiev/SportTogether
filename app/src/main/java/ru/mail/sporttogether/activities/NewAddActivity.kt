package ru.mail.sporttogether.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import ru.mail.sporttogether.R
import ru.mail.sporttogether.databinding.ActivityNewAddBinding
import ru.mail.sporttogether.fragments.FillEventFragment
import ru.mail.sporttogether.fragments.SelectAddressFragment

class NewAddActivity :
        AppCompatActivity() {

    private lateinit var binding: ActivityNewAddBinding
    private var step = STEP_ADDRESS

    private var lat = 0.0
    private var lng = 0.0
    private var isFullCreate = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_add)

        with(intent) {
            lat = getDoubleExtra(KEY_LAT, lat)
            lng = getDoubleExtra(KEY_LNG, lng)
            isFullCreate = getBooleanExtra(KEY_FULL, isFullCreate)
        }

        selectFragment(step)

    }

    fun selectFragment(step: Int) {

        val ft = supportFragmentManager.beginTransaction()
        when (step) {
            STEP_ADDRESS -> {
                ft.replace(R.id.container, SelectAddressFragment.newInstance(lat, lng))
            }
            STEP_FILL_INFO -> {
                ft.replace(R.id.container, FillEventFragment()).addToBackStack(null)
            }
            STEP_TASKS -> {

            }

        }
        ft.commit()
    }

    override fun onBackPressed() {
        --step
        super.onBackPressed()
    }

    fun nextStep() {
        ++step
        selectFragment(step)
    }

    companion object {
        @JvmStatic private val STEP_ADDRESS = 0
        @JvmStatic private val STEP_FILL_INFO = 1
        @JvmStatic private val STEP_TASKS = 2

        @JvmStatic private val KEY_LAT = "KEY_LAT"
        @JvmStatic private val KEY_LNG = "KEY_LNG"
        @JvmStatic private val KEY_FULL = "KEY_FULL"

        @JvmStatic
        fun startForResult(fragment: Fragment, lat: Double, lng: Double, reqCode: Int, isFullCreate: Boolean = true) {
            with(Intent(fragment.context, NewAddActivity::class.java)) {
                putExtra(KEY_LAT, lat)
                putExtra(KEY_LNG, lng)
                putExtra(KEY_FULL, isFullCreate)
                fragment.startActivityForResult(this, reqCode)
            }

        }
    }
}
