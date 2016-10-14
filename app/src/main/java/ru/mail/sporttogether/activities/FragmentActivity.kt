package ru.mail.sporttogether.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import ru.mail.sporttogether.R
import ru.mail.sporttogether.databinding.ActivityFragmentBinding
import ru.mail.sporttogether.fragments.events.EventsFragment

class FragmentActivity : AbstractActivity() {

    private lateinit var binding: ActivityFragmentBinding
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("#MY " + this.javaClass.simpleName, "in on create")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_fragment)
        toolbar = binding.toolbar
        setupToolbar(toolbar)
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, EventsFragment.newInstance())
                .commit()
    }
}
