package ru.mail.sporttogether.widgets

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import ru.mail.sporttogether.R
import ru.mail.sporttogether.databinding.DialogCatAnimationBinding

/**
 * Created by bagrusss on 19.01.17
 */
class CatProgressDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DialogCatAnimationBinding.inflate(inflater, container, false)
        showCat(binding.catView)
        return binding.root
    }

    private fun showCat(iv: ImageView) {
        Glide.with(iv.context)
                .load(R.drawable.cat)
                .asGif()
                .into(iv)
    }

    companion object {
        @JvmStatic val TAG = "CatProgressDialog"

        @JvmStatic
        fun showDialog(fragmentManager: FragmentManager) {
            val fragment = fragmentManager.findFragmentByTag(TAG)
            if (fragment is CatProgressDialog) {
                fragment.show(fragmentManager, TAG)
                return
            }
            CatProgressDialog().show(fragmentManager, TAG)
        }

        @JvmStatic
        fun hide(fragmentManager: FragmentManager) {
            val fragment = fragmentManager.findFragmentByTag(TAG)
            if (fragment is CatProgressDialog) {
                fragment.dismissAllowingStateLoss()
                return
            }
        }
    }
}