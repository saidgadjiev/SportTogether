package ru.mail.sporttogether.fragments.adapter.holders

import android.app.Activity
import android.view.View
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import ru.mail.sporttogether.data.binding.event.SearchSetDateData
import ru.mail.sporttogether.databinding.ItemEventSearchSetDateBinding
import ru.mail.sporttogether.net.models.DateInterval
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.utils.DateUtils
import rx.Observable
import rx.subjects.PublishSubject
import java.util.*

/**
 * Created by root on 13.02.17.
 */
class SetDateHolder(val v: View) : AbstractSearchItemHolder(v), DatePickerDialog.OnDateSetListener {
    val binding: ItemEventSearchSetDateBinding = ItemEventSearchSetDateBinding.bind(v)
    val data : SearchSetDateData = SearchSetDateData()
    val intervalObservable: PublishSubject<DateInterval> = PublishSubject.create()

    init {
        binding.listener = this
        data.isDateSetted.set(false)
        binding.data = data
        render(Calendar.getInstance(), DateUtils.nextMonthCalendar())
    }

    override fun onBind(e: Event) {

    }

    fun render(startDate: Calendar, endDate: Calendar) {
        val sb = StringBuilder("События от ")
        sb.append(DateUtils.toDayMonthString(startDate.time))
                .append("  до ")
                .append(DateUtils.toDayMonthString(endDate.time))

        data.interval.set(sb.toString())
        data.isDateSetted.set(true)
        intervalObservable.onNext(DateInterval(startDate.time, endDate.time))
    }

    fun getIntervalObservable(): Observable<DateInterval> {
        return intervalObservable
    }

    override fun onItemClicker() {
        val now = Calendar.getInstance()
        val dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        )
        dpd.minDate = now
        dpd.maxDate = DateUtils.nextMonthCalendar()
        dpd.setStartTitle("От")
        dpd.setEndTitle("До")
        dpd.show((v.context as Activity).fragmentManager, "Datepickerdialog")
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int, yearEnd: Int, monthOfYearEnd: Int, dayOfMonthEnd: Int) {
        val startDate = Calendar.getInstance()
        startDate.set(Calendar.YEAR, year)
        startDate.set(Calendar.MONTH, monthOfYear)
        startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val endDate = Calendar.getInstance()
        endDate.set(Calendar.YEAR, yearEnd)
        endDate.set(Calendar.MONTH, monthOfYearEnd)
        endDate.set(Calendar.DAY_OF_MONTH, dayOfMonthEnd)

        render(DateUtils.startOfDay(startDate), DateUtils.endOfDay(endDate))
    }

    companion object {
        val TAG = "#MY " + SetDateHolder::class.java.simpleName
    }
}