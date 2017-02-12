package ru.mail.sporttogether.utils

import android.databinding.BindingAdapter
import android.databinding.BindingMethod
import android.databinding.BindingMethods
import android.view.View
import android.view.ViewGroup.MarginLayoutParams


/**
 * Created by bagrusss on 30.01.17
 */
@BindingMethods(
        BindingMethod(
                type = android.widget.ImageView::class,
                attribute = "app:srcCompat",
                method = "setImageDrawable"),
        BindingMethod(
                type = android.widget.LinearLayout::class,
                attribute = "android:layout_marginTop",
                method = "setMargin"))
object BindingUtils {

    @JvmStatic
    @BindingAdapter("android:layout_marginTop")
    fun setMargin(view: View, value: Float) {
        val layoutParams = view.layoutParams as MarginLayoutParams
        layoutParams.setMargins(layoutParams.leftMargin, Math.round(value),
                layoutParams.rightMargin, layoutParams.bottomMargin)
        view.layoutParams = layoutParams
    }
}
