package ru.mail.sporttogether.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LayoutListBinding.inflate(inflater, container, false)
        presenter = TemplatesPresenterImpl()
        return binding.root
    }

}