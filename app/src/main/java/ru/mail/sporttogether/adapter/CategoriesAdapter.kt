package ru.mail.sporttogether.adapter

import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import ru.mail.sporttogether.net.models.Category
import java.util.*

/**
 * Created by Ivan on 31.10.2016.
 */
class CategoriesAdapter(mContext: Context?, resource: Int, private var fullList: ArrayList<Category>) :
        ArrayAdapter<Category>(mContext, resource, fullList),
        Filterable {
    override fun clear() {
        fullList.clear()
        mOriginalValues.clear()
    }

    override fun addAll(collection: MutableCollection<out Category>?) {
        collection?.forEach { el -> add(el) }
        notifyDataSetChanged()
    }

    override fun add(item: Category?) {
        if (item != null) {
            fullList.add(item)
            mOriginalValues.clear()
        }
    }

    fun getFullList(): List<Category> {
        return fullList
    }

    private var mOriginalValues: ArrayList<Category> = ArrayList(fullList)
    private var mFilter: CategoryFilter? = null

    override fun getCount(): Int {
        return this.fullList.size
    }

    override fun getItem(position: Int): Category {
        return fullList[position]
    }

    override fun getFilter(): Filter {
        if (mFilter == null) {
            mFilter = CategoryFilter()
        }
        return mFilter!!
    }

    inner class CategoryFilter : Filter() {
        override fun performFiltering(prefix: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            if (mOriginalValues.isEmpty()) {
                mOriginalValues = ArrayList(fullList)
            }

            if (prefix == null || prefix.length === 0) {
                val list = ArrayList<Category>(mOriginalValues)
                filterResults.values = list
                filterResults.count = list.size
            } else {
                val prefixString = prefix.toString()

                val values = mOriginalValues
                val count = values.size

                val newValues = ArrayList<Category>(count)

                for (i in 0..count - 1) {
                    val item = values[i]
                    if (item.name.contains(prefixString)) {
                        newValues.add(item)
                    }
                }

                filterResults.values = newValues
                filterResults.count = newValues.size
                newValues.forEach { el -> Log.d("#MY " + javaClass.simpleName, "filtered : " + el.name)}
            }

            return filterResults
        }

        override fun publishResults(prefix: CharSequence?, filterResults: FilterResults) {
            if(filterResults.values != null){
                fullList = filterResults.values as ArrayList<Category>
            }else{
                fullList = ArrayList<Category>()
            }
            if (filterResults.count > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }

    }
}