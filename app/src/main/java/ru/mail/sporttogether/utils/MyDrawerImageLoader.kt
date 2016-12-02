package ru.mail.sporttogether.utils

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.squareup.picasso.Picasso

class MyDrawerImageLoader : AbstractDrawerImageLoader() {
    override fun set(imageView: ImageView?, uri: Uri?, placeholder: Drawable?) {
        Picasso.with(imageView?.context).load(uri).placeholder(placeholder).into(imageView)
    }

    override fun cancel(imageView: ImageView?) {
        Picasso.with(imageView?.context).cancelRequest(imageView)
    }
}