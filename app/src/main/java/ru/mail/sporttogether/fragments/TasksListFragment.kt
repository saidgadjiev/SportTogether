package ru.mail.sporttogether.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import ru.mail.sporttogether.R
import ru.mail.sporttogether.data.binding.ToolbarWithButtonData
import ru.mail.sporttogether.databinding.FragmentAddTasksBinding
import ru.mail.sporttogether.fragments.adapter.TasksAdapter
import ru.mail.sporttogether.fragments.adapter.views.TasksListView
import ru.mail.sporttogether.fragments.presenter.TasksListPresenter
import ru.mail.sporttogether.fragments.presenter.TasksListPresenterImpl
import ru.mail.sporttogether.utils.DialogUtils

/**
 * Created by bagrusss on 14.02.17
 */
class TasksListFragment : AbstractListFragment<TasksListPresenter, TasksAdapter>(), TasksListView {

    private lateinit var binding: FragmentAddTasksBinding
    private val toolbarData = ToolbarWithButtonData()

    private var dialog: AlertDialog? = null
    private var isFirstDialogShow = true
    private lateinit var editText: EditText

    override fun getListPresenter() = TasksListPresenterImpl(this)

    override fun getAdapter() = TasksAdapter(getListPresenter())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddTasksBinding.inflate(inflater, container, false)
        binding.toolbarData = toolbarData
        binding.data = data

        toolbarData.buttonIsVisible.set(true)
        toolbarData.buttonText.set(getString(R.string.next))

        data.isFabVisible.set(true)
        data.emptyDrawable.set(ContextCompat.getDrawable(context, R.drawable.ic_templates))
        data.emptyText.set(getString(R.string.no_tasks))
        data.isEmpty.set(true)

        recyclerView = binding.includeList.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        listAdapter = getAdapter()
        recyclerView.adapter = listAdapter

        presenter = listAdapter.tasksPresenter

        setupToolbar(binding.includeToolbar.toolbar, getString(R.string.add_tasks))

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.toolbarListener = this
        binding.fabListener = this
    }

    override fun onStop() {
        binding.toolbarListener = null
        binding.fabListener = null
        super.onStop()
    }

    override fun taskAdded(task: String) {
        listAdapter.addTask(task)
        data.isEmpty.set(false)
    }

    override fun onClick(v: View) {
        v.isEnabled = false
        showAddTaskDialog()
        v.isEnabled = true
    }

    override fun hideFab() {
        binding.data.isFabVisible.set(false)
    }

    override fun showFab() {
        binding.data.isFabVisible.set(true)
    }

    override fun onNextClicked() {
        presenter.createEvent()
    }

    override fun finish() {
        hideProgressDialog()
        activity?.finish()
    }

    private fun showAddTaskDialog() {
        if (dialog == null) {
            editText = EditText(context)
            editText.maxLines = 1
            dialog = AlertDialog.Builder(context)
                    .setTitle(R.string.add_task)
                    .setView(editText)
                    .setPositiveButton(android.R.string.ok, { dialog, which ->
                        presenter.addTask(editText.text.toString())
                        editText.setText("")
                    })
                    .setNegativeButton(android.R.string.cancel, { dialog, which ->

                    })
                    .setCancelable(false)
                    .create()
        }
        dialog!!.show()
        if (isFirstDialogShow) {
            dialog!!.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            dialog!!.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            val pxPadding = DialogUtils.dpToPx(context, 24f)
            (editText.layoutParams as ViewGroup.MarginLayoutParams).setMargins(pxPadding, 0, pxPadding, 0)
            isFirstDialogShow = false
        }
        editText.requestFocus()
    }

}