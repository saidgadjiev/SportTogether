package ru.mail.sporttogether.utils

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader

class DrawerImageLoader : AbstractDrawerImageLoader() {
    override fun set(imageView: ImageView, uri: Uri?, placeholder: Drawable) {
        Glide.with(imageView.context)
                .load(uri)
                .placeholder(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView)
    }

    override fun cancel(imageView: ImageView) {
        Glide.clear(imageView)
    }
}