package ru.mail.sporttogether.widgets

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import ru.mail.sporttogether.R

/**
 * Created by bagrusss on 19.01.17
 */
class ProgressDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = ProgressDialog(activity)
        dialog.isIndeterminate = true
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)


        val string = arguments?.let {
            args ->
            if (args.containsKey(MESSAGE_KEY)) {
                val id = args.getInt(MESSAGE_KEY)
                getString(id)
            } else if (arguments.containsKey(MESSAGE_TEXT_KEY)) {
                arguments.getString(MESSAGE_TEXT_KEY)
            } else {
                getString(R.string.please_wait)
            }
        } ?: getString(R.string.please_wait)

        dialog.setMessage(string)

        isCancelable = false

        return dialog
    }

    companion object {
        private val MESSAGE_KEY = "message"
        private val MESSAGE_TEXT_KEY = "message_text"

        @JvmStatic
        private var dialogSpinner: ProgressDialogFragment? = null

        @JvmStatic
        fun show(fm: FragmentManager, @StringRes msg: Int = R.string.please_wait) {
            if (dialogSpinner == null) {
                try {
                    dialogSpinner = newInstance(msg)
                    dialogSpinner!!.show(fm, null)
                } catch (t: Throwable) {

                }
            }
        }

        @JvmStatic
        fun hide() {
            dialogSpinner?.let {
                try {
                    it.dismissAllowingStateLoss()
                    dialogSpinner = null
                } catch (t: Throwable) {

                }
            }
        }

        @JvmStatic
        @JvmOverloads fun newInstance(message: Int = R.string.please_wait): ProgressDialogFragment {
            val fragment = ProgressDialogFragment()
            val arguments = Bundle()
            arguments.putInt(MESSAGE_KEY, message)
            fragment.arguments = arguments
            return fragment
        }

        @JvmStatic
        fun newInstance(messageString: String): ProgressDialogFragment {
            val fragment = ProgressDialogFragment()
            val arguments = Bundle()
            arguments.putString(MESSAGE_TEXT_KEY, messageString)
            fragment.arguments = arguments
            return fragment
        }
    }
}