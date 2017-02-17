package ru.mail.sporttogether.fragments

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import com.jakewharton.rxbinding.widget.RxTextView
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.NewAddActivity
import ru.mail.sporttogether.adapter.CategoriesAdapter
import ru.mail.sporttogether.data.binding.FillEventData
import ru.mail.sporttogether.data.binding.ToolbarWithButtonData
import ru.mail.sporttogether.databinding.FragmentFillEventBinding
import ru.mail.sporttogether.fragments.presenter.FillEventPresenter
import ru.mail.sporttogether.fragments.presenter.FillEventPresenterImpl
import ru.mail.sporttogether.fragments.view.FillEventView
import ru.mail.sporttogether.net.CategoriesResponse
import ru.mail.sporttogether.net.models.Category
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.utils.AddEventTextWatcher
import ru.mail.sporttogether.utils.DateUtils
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.Subscriptions
import java.util.*
import java.util.regex.Pattern

/**
 * Created by bagrusss on 14.02.17
 */
class FillEventFragment : AbstractMapFragment<FillEventPresenter>(), FillEventView {

    private lateinit var binding: FragmentFillEventBinding
    private val data = FillEventData()
    private val toolbarData = ToolbarWithButtonData()

    private var time = 0L

    private val namePattern = Pattern.compile("([а-я]+[ |-])*[а-я]+", Pattern.CASE_INSENSITIVE)

    private lateinit var toolbar: Toolbar
    private lateinit var nameInputLayout: TextInputLayout
    private lateinit var sportInputLayout: TextInputLayout
    private lateinit var peopleInputLayout: TextInputLayout

    private lateinit var sportAutoComplete: AutoCompleteTextView
    private lateinit var nameEdit: EditText
    private lateinit var peopleEdit: EditText

    private var sportSubscription = Subscriptions.empty()

    private lateinit var categoriesAdapter: CategoriesAdapter
    private var categoriesArray: ArrayList<Category> = ArrayList(5)

    private var year = 0
    private var month = 0
    private var day = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFillEventBinding.inflate(inflater, container, false)
        binding.addEventData = data
        binding.toolbarData = toolbarData

        toolbar = binding.toolbar
        setupToolbar(toolbar)
        toolbarData.buttonIsVisible.set(true)
        toolbarData.buttonText.set(getString(R.string.next))
        toolbar.title = getString(R.string.add_event)


        presenter = FillEventPresenterImpl(this)

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(presenter)


        nameInputLayout = binding.include.nameInputLayout
        sportInputLayout = binding.include.sportTextInput
        peopleInputLayout = binding.include.peopleTextInput

        sportAutoComplete = binding.include.editSport
        nameEdit = binding.include.editName
        peopleEdit = binding.include.editPeople

        nameEdit.addTextChangedListener(AddEventTextWatcher(nameInputLayout))
        sportAutoComplete.addTextChangedListener(AddEventTextWatcher(sportInputLayout))
        peopleEdit.addTextChangedListener(AddEventTextWatcher(peopleInputLayout))

        sportSubscription = RxTextView.textChangeEvents(sportAutoComplete)
                .filter { e -> e.count() == 3 }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { e ->
                    presenter.loadCategoriesBySubname(data.sport.get())
                }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        categoriesAdapter = CategoriesAdapter(context, android.R.layout.select_dialog_item, categoriesArray)
        sportAutoComplete.setAdapter(categoriesAdapter)

        arguments?.let {
            val event = it.getParcelable<Event>(KEY_EVENT)
            event?.let { event ->
                data.sport.set(event.category.name)
                data.description.set(event.description)
                data.maxPeople.set(event.maxPeople.toString())
                data.name.set(event.name)
                data.needAddTemplate.set(false)
                binding.include.addEventSwitch.visibility = View.GONE
            }
        }

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR, 2)
        time = calendar.timeInMillis
        data.date.set(DateUtils.longToDateTime(calendar.timeInMillis))
    }

    override fun onStart() {
        super.onStart()
        binding.toolbarListener = this
        binding.listener = this
    }

    override fun onStop() {
        binding.toolbarListener = null
        binding.listener = null
        super.onStop()
    }

    override fun updateAddress(address: String) {
        data.address.set(address)
    }

    override fun onDateViewClicked() {
        showDateDialog()
    }

    private var currentDate = 0L
    private lateinit var lastCalendar: Calendar

    private fun showDateDialog() {
        val calendar = Calendar.getInstance()
        val dateDialog = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
        currentDate = calendar.timeInMillis
        dateDialog.minDate = calendar
        dateDialog.setVersion(DatePickerDialog.Version.VERSION_2)
        lastCalendar = calendar.clone() as Calendar
        lastCalendar.add(Calendar.MONTH, 1)
        dateDialog.maxDate = lastCalendar
        dateDialog.show(activity.fragmentManager, "dateDialog")
    }


    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        this.year = year
        month = monthOfYear
        day = dayOfMonth
        lastCalendar.set(Calendar.YEAR, year)
        lastCalendar.set(Calendar.MONTH, monthOfYear)
        lastCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        showTimeDialog()
    }

    fun showTimeDialog() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog.newInstance(this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true)
        val selectedDate = lastCalendar.timeInMillis
        if (currentDate > selectedDate) {
            timePickerDialog.setMinTime(calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE) + 1,
                    calendar.get(Calendar.SECOND))
        }
        timePickerDialog.show(activity.fragmentManager, "timeDialog")
    }

    override fun onTimeSet(view: TimePickerDialog, hourOfDay: Int, minute: Int, second: Int) {
        val calendar = Calendar.getInstance()
        Log.w("timeDialog", "date before set $year $month $day $hourOfDay $minute $second")

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        time = calendar.timeInMillis
        Log.w("timeDialog", DateUtils.longToDateTime(time))
        Log.w("timeDialog", "date after set $year $month $day $hourOfDay $minute $second")

        data.date.set(DateUtils.longToDateTime(time))
    }


    override fun nextStep() {
        val act = activity
        if (isAdded && act is NewAddActivity) {
            act.nextStep()
        }
    }

    override fun onCategoriesLoaded(data: CategoriesResponse) {
        categoriesAdapter.clear()
        categoriesAdapter.addAll(data)
    }


    private fun validate(): Boolean {
        var res = true
        val name = data.name.get()
        if (name.isNullOrEmpty()) {
            nameInputLayout.error = "Поле обязательно для заполнения"
            res = false
        } else {
            val matcher = namePattern.matcher(name)
            if (!matcher.matches()) {
                nameInputLayout.error = "Допустимы русские буквы, пробелы и дефис"
                res = false
            }
        }

        val sport = data.sport.get()
        if (sport.isNullOrEmpty()) {
            sportInputLayout.error = "Поле обязательно для заполнения"
            res = false
        } else {
            val matcher = namePattern.matcher(sport)
            if (!matcher.matches()) {
                sportInputLayout.error = "Допустимы русские буквы, пробелы и дефис"
                res = false
            }
        }

        val people = data.maxPeople.get()
        if (people.isNullOrEmpty()) {
            peopleInputLayout.error = "Поле обязательно для заполнения"
            res = false
        }

        return res
    }

    override fun onNextClicked() {
        if (validate()) {
            presenter.fillEvent(
                    data.name.get(),
                    data.sport.get(),
                    data.maxPeople.get().toInt(),
                    data.description.get() ?: "Описание отсутствует",
                    data.address.get(),
                    time,
                    data.joinToEvent.get(),
                    data.needAddTemplate.get())
        }
    }

    companion object {

        @JvmStatic private val KEY_EVENT = "KEY_EVENT"

        @JvmStatic
        fun newInstance(event: Event?): FillEventFragment {
            val fragment = FillEventFragment()
            val args = Bundle()
            args.putParcelable(KEY_EVENT, event)
            fragment.arguments = args
            return fragment
        }
    }


}