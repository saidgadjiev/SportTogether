package ru.mail.sporttogether.adapter

import android.content.Context
import android.widget.ArrayAdapter
import ru.mail.sporttogether.net.models.Category

/**
 * Created by Ivan on 31.10.2016.
 */
class CategoriesAdapter(context: Context?, resource: Int, objects: MutableList<Category>?) : ArrayAdapter<Category>(context, resource, objects) {
}