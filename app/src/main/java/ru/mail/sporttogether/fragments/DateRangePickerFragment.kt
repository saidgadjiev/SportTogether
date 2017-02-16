//package ru.mail.sporttogether.fragments
//
//import android.os.Bundle
//import android.support.v4.app.DialogFragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.Window
//import android.view.WindowManager
//import android.widget.Button
//import android.widget.DatePicker
//import android.widget.TabHost
//
//import com.matdroid.daterangepicker.R
//
//
///**
// * A DialogFragment that show 2 date pickers.
// * Yesid Lazaro @ingyesid
//
// */
//class DateRangePickerFragment : DialogFragment(), View.OnClickListener {
//
//    private var onDateRangeSelectedListener: OnDateRangeSelectedListener? = null
//
//    private var tabHost: TabHost? = null
//    private var startDatePicker: DatePicker? = null
//    private var endDatePicker: DatePicker? = null
//    private var butSetDateRange: Button? = null
//    internal var is24HourMode: Boolean = false
//
//    fun initialize(callback: OnDateRangeSelectedListener,
//                   is24HourMode: Boolean) {
//        onDateRangeSelectedListener = callback
//        this.is24HourMode = is24HourMode
//    }
//
//
//    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        val root = inflater!!.inflate(R.layout.date_range_picker, container, false)
//        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
//        tabHost = root.findViewById(R.id.tabHost) as TabHost
//        butSetDateRange = root.findViewById(R.id.but_set_time_range) as Button
//        startDatePicker = root.findViewById(R.id.start_date_picker) as DatePicker
//        endDatePicker = root.findViewById(R.id.end_date_picker) as DatePicker
//        butSetDateRange!!.setOnClickListener(this)
//        tabHost!!.findViewById(R.id.tabHost)
//        tabHost!!.setup()
//        val startDatePage = tabHost!!.newTabSpec("start")
//        startDatePage.setContent(R.id.start_date_group)
//        startDatePage.setIndicator(getString(R.string.title_tab_start_date))
//
//        val endDatePage = tabHost!!.newTabSpec("end")
//        endDatePage.setContent(R.id.end_date_group)
//        endDatePage.setIndicator(getString(R.string.ttile_tab_end_date))
//
//        tabHost!!.addTab(startDatePage)
//        tabHost!!.addTab(endDatePage)
//        return root
//
//    }
//
//    override fun onStart() {
//        super.onStart()
//        if (dialog == null)
//            return
//        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
//    }
//
//
//    fun setOnDateRangeSelectedListener(callback: OnDateRangeSelectedListener) {
//        this.onDateRangeSelectedListener = callback
//    }
//
//    override fun onClick(v: View) {
//        dismiss()
//        onDateRangeSelectedListener!!.onDateRangeSelected(startDatePicker!!.dayOfMonth, startDatePicker!!.month, startDatePicker!!.year,
//                endDatePicker!!.dayOfMonth, endDatePicker!!.month, endDatePicker!!.year)
//    }
//
//    interface OnDateRangeSelectedListener {
//        fun onDateRangeSelected(startDay: Int, startMonth: Int, startYear: Int, endDay: Int, endMonth: Int, endYear: Int)
//    }
//
//    companion object {
//
//        fun newInstance(callback: OnDateRangeSelectedListener, is24HourMode: Boolean): DateRangePickerFragment {
//            val dateRangePickerFragment = DateRangePickerFragment()
//            dateRangePickerFragment.initialize(callback, is24HourMode)
//            return dateRangePickerFragment
//        }
//    }
//
//}// Required empty public constructor