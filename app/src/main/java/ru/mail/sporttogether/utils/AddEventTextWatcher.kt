package ru.mail.sporttogether.utils

import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher

/**
 * Created by bagrusss on 16.02.17
 */
class AddEventTextWatcher(val inputLayout: TextInputLayout) : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        inputLayout.error = null
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }
}