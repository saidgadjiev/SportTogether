package ru.mail.sporttogether.utils

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import ru.mail.sporttogether.R
import android.util.TypedValue



/**
 * Created by bagrusss on 23.01.17
 */
object DialogUtils {
    private var angryDialog: AlertDialog? = null

    fun showAngryDialog(context: Context, okClicked: () -> Unit, cancelClicked: () -> Unit) {
        if (angryDialog == null) {
            angryDialog = AlertDialog.Builder(context)
                    .setPositiveButton(android.R.string.yes, { dialog, which ->
                        okClicked()
                        dialog.dismiss()
                    })
                    .setNegativeButton(android.R.string.no, { dialog, which ->
                        cancelClicked()
                        dialog.dismiss()
                    })
                    .setMessage(R.string.want_angry)
                    .create()
        } else {
            with(angryDialog!!) {
                getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                    okClicked()
                    dismiss()
                }
                getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                    cancelClicked()
                    dismiss()
                }
            }
        }
        angryDialog!!.show()
    }

    fun dpToPx(context: Context, dp: Float): Int {
        val r = context.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics).toInt()
    }
}