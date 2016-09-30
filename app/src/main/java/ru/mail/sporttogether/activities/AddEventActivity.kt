package ru.mail.sporttogether.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import ru.mail.sporttogether.R

class AddEventActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
    }

    companion object {
        
        fun start(c: Context, lng: Double, lat: Double) {
            val intent = Intent(c, AddEventActivity::class.java)
            intent.putExtra(KEY_LNG, lng)
            intent.putExtra(KEY_LAT, lat)
            c.startActivity(intent)
        }

        private val KEY_LNG = "lng"
        private val KEY_LAT = "lng"
    }
}
