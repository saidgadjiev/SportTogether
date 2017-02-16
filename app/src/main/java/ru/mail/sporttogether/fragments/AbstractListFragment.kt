package ru.mail.sporttogether.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mail.sporttogether.data.binding.ListLayoutData
import ru.mail.sporttogether.databinding.LayoutListBinding
import ru.mail.sporttogether.mvp.IPresenter
import ru.mail.sporttogether.mvp.PresenterFragment

/**
 * Created by bagrusss on 07.02.17
 */
abstract class AbstractListFragment<P : IPresenter, A : RecyclerView.Adapter<*>> : PresenterFragment<P>() {
    private lateinit var binding: LayoutListBinding
    protected lateinit var recyclerView: RecyclerView

    protected val data = ListLayoutData()

    abstract fun getListPresenter(): P
    protected lateinit var listAdapter: A

    abstract fun getAdapter(): A


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LayoutListBinding.inflate(inflater, container, false)
        binding.data = data

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        listAdapter = getAdapter()
        recyclerView.adapter = listAdapter

        presenter = getListPresenter()
        return binding.root
    }


}