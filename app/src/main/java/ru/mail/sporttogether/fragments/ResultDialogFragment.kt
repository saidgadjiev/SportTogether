package ru.mail.sporttogether.fragments

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Toast
import com.mikepenz.materialize.util.KeyboardUtil
import ru.mail.sporttogether.R
import ru.mail.sporttogether.data.binding.DialogResultData
import ru.mail.sporttogether.databinding.ResultLayoutBinding
import ru.mail.sporttogether.fragments.presenter.ResultDialogPresenter
import ru.mail.sporttogether.fragments.presenter.ResultDialogPresenterImpl
import ru.mail.sporttogether.fragments.view.ResultView
import ru.mail.sporttogether.mvp.PresenterDialogFragment
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 22.01.17
 */
class ResultDialogFragment : PresenterDialogFragment<ResultDialogPresenter>(), ResultView {

    private lateinit var bodyBinding: ResultLayoutBinding
    private val data = DialogResultData()
    private lateinit var event: Event

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = LayoutInflater.from(context)
        bodyBinding = ResultLayoutBinding.inflate(inflater)
        bodyBinding.data = data

        arguments?.let {
            event = it.getParcelable(KEY_EVENT)
        }

        val dialog = AlertDialog.Builder(context)
                .setTitle(R.string.result)
                .setNegativeButton(android.R.string.cancel, { whichi, dialog ->

                })
                .setPositiveButton(android.R.string.ok, { whichi, dialog ->

                })
                .setView(bodyBinding.root)
                .create()
        presenter = ResultDialogPresenterImpl(this)
        return dialog
    }


    override fun onStart() {
        val dlg = dialog as AlertDialog
        dlg.setCancelable(false)
        dlg.setCanceledOnTouchOutside(false)
        super.onStart()

        dlg.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener {
            val res = data.resultText.get()
            if (!res.isNullOrEmpty()) {
                presenter.sendResult(res, event)
            }
        }
    }

    override fun onResultSent() {
        KeyboardUtil.hideKeyboard(activity)
        dismissAllowingStateLoss()
        Toast.makeText(context, R.string.result_sent, Toast.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic val TAG = "ResultDialog"
        @JvmStatic private val KEY_EVENT = "EVENt"

        @JvmStatic
        fun show(fm: FragmentManager, event: Event) {
            val dialog = ResultDialogFragment()
            val args = Bundle()
            args.putParcelable(KEY_EVENT, event)
            dialog.arguments = args
            dialog.show(fm, TAG)
        }
    }
}