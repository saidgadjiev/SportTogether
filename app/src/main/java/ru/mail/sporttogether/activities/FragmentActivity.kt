package ru.mail.sporttogether.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.Toolbar
import ru.mail.sporttogether.R
import ru.mail.sporttogether.databinding.ActivityFragmentBinding

class FragmentActivity : AbstractActivity() {

    private lateinit var binding: ActivityFragmentBinding
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fragment)
        toolbar = binding.toolbar
        setupToolbar(toolbar)
    }
}
