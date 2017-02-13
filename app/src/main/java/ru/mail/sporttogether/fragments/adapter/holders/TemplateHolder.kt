package ru.mail.sporttogether.fragments.adapter.holders

import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.view.View
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.AddEventActivity
import ru.mail.sporttogether.activities.EventDetailsActivity
import ru.mail.sporttogether.data.binding.items.TwoButtonItemData
import ru.mail.sporttogether.databinding.ItemTwoButtonBinding
import ru.mail.sporttogether.fragments.adapter.DeleteTemplateCallback
import ru.mail.sporttogether.fragments.adapter.presenters.TemplatesHolderPresenterImpl
import ru.mail.sporttogether.fragments.adapter.views.TemplateHolderView
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.utils.DateUtils
import ru.mail.sporttogether.widgets.ProgressDialogFragment

/**
 * Created by bagrusss on 07.02.17
 */
class TemplateHolder(v: View, private val callback: DeleteTemplateCallback, private val fm: FragmentManager) :
        AbstractPresenterHolder<TemplatesHolderPresenterImpl>(v),
        View.OnClickListener,
        TemplateHolderView {


    private val binding: ItemTwoButtonBinding = ItemTwoButtonBinding.bind(v)
    private val data = TwoButtonItemData()
    private lateinit var event: Event
    private var pos = 0

    init {
        binding.data = data
        binding.listener = this
        data.button2Text.set(itemView.context.getString(R.string.create))
        data.button1Text.set(itemView.context.getString(R.string.delete))
        presenter = TemplatesHolderPresenterImpl(this)
    }

    fun onBind(e: Event, position: Int) {
        data.nameText.set(e.name)
        data.dateText.set(DateUtils.longToDateTime(e.date))

        val descriptionText = e.description.let {
            if (it.isBlank()) {
                itemView.context.getString(R.string.no_description)
            } else {
                it
            }
        }

        data.description.set(descriptionText)
        event = e
        pos = position
    }

    override fun templateDeleted() {
        callback.onTemplateDeleted(pos)
    }

    override fun showProgressDialog() {
        ProgressDialogFragment.show(fm)
    }

    override fun hideProgressDialog() {
        ProgressDialogFragment.hide()
    }

    fun showConfirmDialog() {
        AlertDialog.Builder(itemView.context)
                .setCancelable(false)
                .setMessage(R.string.delete_template_message)
                .setPositiveButton(android.R.string.ok, { dialog, which ->
                    presenter.deleteTemplate(event.id)
                    dialog.cancel()
                })
                .setNegativeButton(android.R.string.cancel, { dialog, which ->
                    dialog.cancel()
                })
                .create()
                .show()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button1 -> {
                showConfirmDialog()
            }
            R.id.button2 -> {
                AddEventActivity.start(v.context, event)
            }
            R.id.constraint_root -> {
                EventDetailsActivity.startTemplate(v.context, event)
            }
        }
    }
}