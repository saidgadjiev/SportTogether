package ru.mail.sporttogether.fragments

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import ru.mail.sporttogether.R
import ru.mail.sporttogether.fragments.adapter.TemplatesAdapter
import ru.mail.sporttogether.fragments.presenter.TemplatesPresenter
import ru.mail.sporttogether.fragments.presenter.TemplatesPresenterImpl
import ru.mail.sporttogether.fragments.view.TemplatesView
import ru.mail.sporttogether.net.models.Event
import java.util.*

/**
 * Created by bagrusss on 05.02.17
 */
class TemplatesListFragment : AbstractListFragment<TemplatesPresenter, TemplatesAdapter>(), TemplatesView {


    override fun getListPresenter() = TemplatesPresenterImpl(this)

    override fun getAdapter() = TemplatesAdapter(childFragmentManager)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        data.emptyDrawable.set(ContextCompat.getDrawable(context, R.drawable.ic_templates))
        data.emptyText.set(getString(R.string.no_templates))
        presenter.loadTemplates()
    }

    override fun swapTemplates(templates: LinkedList<Event>) {
        data.isEmpty.set(templates.isEmpty())
        listAdapter.swapTemplates(templates)
    }

}