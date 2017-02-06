package ru.mail.sporttogether.fragments

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.R
import ru.mail.sporttogether.data.binding.ListLayoutData
import ru.mail.sporttogether.databinding.LayoutListBinding
import ru.mail.sporttogether.fragments.presenter.TemplatesPresenter
import ru.mail.sporttogether.fragments.presenter.TemplatesPresenterImpl
import ru.mail.sporttogether.fragments.view.TemplatesView
import ru.mail.sporttogether.mvp.PresenterFragment

/**
 * Created by bagrusss on 05.02.17
 */
class TemplatesFragment : PresenterFragment<TemplatesPresenter>(), TemplatesView {

    protected lateinit var binding: LayoutListBinding
    private val data = ListLayoutData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LayoutListBinding.inflate(inflater, container, false)
        binding.data = data
        data.emptyDrawable.set(ContextCompat.getDrawable(context, R.drawable.ic_templates))
        data.emptyText.set(getString(R.string.empty))
        presenter = TemplatesPresenterImpl()
        return binding.root
    }

}