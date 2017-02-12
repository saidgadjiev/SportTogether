package ru.mail.sporttogether.utils

import android.databinding.BindingMethod
import android.databinding.BindingMethods

/**
 * Created by bagrusss on 30.01.17
 */
@BindingMethods(BindingMethod(type = android.widget.ImageView::class, attribute = "app:srcCompat", method = "setImageDrawable"))
class BindingUtils
