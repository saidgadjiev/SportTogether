package ru.mail.sporttogether.fragments.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mail.sporttogether.R
import ru.mail.sporttogether.fragments.adapter.holders.AbstractSearchItemHolder
import ru.mail.sporttogether.fragments.adapter.holders.SearchItemHolder
import ru.mail.sporttogether.fragments.adapter.holders.SetDateHolder
import ru.mail.sporttogether.net.models.DateInterval
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.utils.DateUtils
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

/**
 * Created by Ivan on 20.10.2016
 */
class EventsSearchAdapter : RecyclerView.Adapter<AbstractSearchItemHolder>() {
    private var events: MutableList<Event>? = null
    private var filteredEvents: MutableList<Event>? = null
    private var subscription: Subscription? = null

    override fun getItemCount() = (filteredEvents?.size ?: 0) + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) SET_DATE_TYPE else SEARCH_ITEM_TYPE
    }

    fun swap(events: MutableList<Event>) {
        this.events = events
        filter(Calendar.getInstance().time, DateUtils.nextMonthCalendar().time)
//        this.filteredEvents = events.filter { item ->
//            item.date < DateUtils.nextMonthCalendar().timeInMillis
//        }.toMutableList()
//        notifyDataSetChanged()
    }

    fun filter(startDate: Date, endDate: Date) {
        Log.d(TAG, "filter events")
        Log.d(SetDateHolder.TAG, "start date " + DateUtils.toXLongDateString(startDate) + " long = " + startDate.time)
        Log.d(SetDateHolder.TAG, "end date " + DateUtils.toXLongDateString(endDate) + " long = " + endDate.time)

        this.filteredEvents = events!!.filter { item ->
            Log.d(TAG, "item name : " + item.name + " long " + item.date + "is filtered : " + (item.date > startDate.time && item.date < endDate.time))
            item.date > startDate.time && item.date < endDate.time
        }.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractSearchItemHolder {
        var holder: AbstractSearchItemHolder? = null
        when(viewType) {
            SET_DATE_TYPE -> {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.item_event_search_set_date, parent, false)
                holder = SetDateHolder(view)
                if (subscription == null) {
                    Log.d(TAG, "subscribe on date change")
                    subscription = (holder as SetDateHolder).getIntervalObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Subscriber<DateInterval>() {
                                override fun onNext(interval: DateInterval) {
                                    Log.d(TAG, "in on next interval " + interval)
                                    filter(interval.startDate, interval.endDate)
                                }

                                override fun onError(e: Throwable?) {

                                }

                                override fun onCompleted() {
                                }

                            })
                }
            }
            else -> {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.item_event_search, parent, false)
                holder = SearchItemHolder(view)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: AbstractSearchItemHolder, position: Int) {
        when (position) {
            0 -> {}
            else -> {
                filteredEvents?.let {
                    holder.onBind(it[position - 1])
                }
            }
        }

    }

    companion object {
        val TAG = "#MY " + EventsSearchAdapter::class.java.simpleName

        private val SET_DATE_TYPE = 0
        private val SEARCH_ITEM_TYPE = 1
    }
}