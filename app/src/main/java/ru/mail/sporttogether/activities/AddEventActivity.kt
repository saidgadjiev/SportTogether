package ru.mail.sporttogether.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.jakewharton.rxbinding.widget.RxTextView
import com.mikepenz.materialdrawer.util.KeyboardUtil
import ru.mail.sporttogether.R
import ru.mail.sporttogether.activities.presenter.AddEventPresenter
import ru.mail.sporttogether.activities.presenter.AddEventPresenterImpl
import ru.mail.sporttogether.activities.view.AddEventView
import ru.mail.sporttogether.adapter.AddTaskAdapter
import ru.mail.sporttogether.adapter.CategoriesAdapter
import ru.mail.sporttogether.data.binding.event.ButtonListener
import ru.mail.sporttogether.data.binding.event.DatePickerListener
import ru.mail.sporttogether.data.binding.event.EventData
import ru.mail.sporttogether.data.binding.tasks.OpenTasksListener
import ru.mail.sporttogether.databinding.ActivityAddEventBinding
import ru.mail.sporttogether.databinding.AddingTasksBinding
import ru.mail.sporttogether.databinding.DateTimePickerBinding
import ru.mail.sporttogether.mvp.PresenterActivity
import ru.mail.sporttogether.mvp.PresenterFragment
import ru.mail.sporttogether.net.models.Category
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.Task
import ru.mail.sporttogether.utils.DateUtils
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.*

class AddEventActivity :
        PresenterActivity<AddEventPresenter>(),
        AddEventView,
        ButtonListener,
        DatePickerListener {

    private lateinit var binding: ActivityAddEventBinding

    private val data = EventData()

    private val handler = Handler()
    private var eventId = 0L
    private var settedDate: GregorianCalendar? = null
    private lateinit var pickDateText: TextView
    private lateinit var event: Event

    private var addingTasksDialog: AddingTasksDialog? = null
    private var datePickerDialog: AlertDialog? = null
    private var datepickerDialogViewBinding: DateTimePickerBinding? = null

    private lateinit var categoryAutocomplete: AutoCompleteTextView
    private var categoriesArray: ArrayList<Category> = ArrayList()
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var loadingCategoriesProgressBar: ProgressBar
    var subscription: Subscription? = null


    inner class AddingTasksDialog(val context: AddEventActivity): OpenTasksListener {
        var dialog: AlertDialog = AlertDialog.Builder(context)
                .setNegativeButton("Очистить", { dialogInterface, i ->
                    context.binding.addingTasksCount.text = "0"
                    clearTasks()
                    dialogInterface.cancel()
                })
                .setPositiveButton("Готово", { dialogInterface, i ->
                    context.binding.addingTasksCount.text = Integer.toString(addTaskAdapter.getTasks().size)
                    dialogInterface.cancel()
                })
                .setCancelable(false)
                .create()
        var binding: AddingTasksBinding = AddingTasksBinding.inflate(layoutInflater, null, false)
        val addTaskAdapter: AddTaskAdapter = AddTaskAdapter()

        init {
            this.dialog.setView(this.binding.root)

            binding.addingTasksRecyclerView.adapter = addTaskAdapter
            binding.addingTasksRecyclerView.layoutManager = LinearLayoutManager(context)
            val addingTasksBtn = binding.addingTasksBtn
            addingTasksBtn.setOnClickListener {
                if (addTaskAdapter.itemCount >= 5) {
                    context.showToast("максимум 5 задач у одного события")
                } else {
                    val editField = binding.addingTasksEditField
                    if (editField.text.toString().isNotBlank()) {
                        addTaskAdapter.addTask(editField.text.toString())
                        editField.text.clear()
                    }
                }
            }
        }

        override fun openTasks() {
            this.dialog.show()
        }

        fun clearTasks() {
            addTaskAdapter.clearTasks()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AddEventPresenterImpl(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_event)
        binding.data = data


        with(intent) {
            event = getParcelableExtra(KEY_EVENT)
            presenter.onCreate(savedInstanceState, event)
        }
        eventId = intent.getLongExtra(KEY_ID, 0L)
        if (eventId != 0L) {
            data.resultVisibility.set(true)
            data.mainDataVisibility.set(false)
            return
        }
        setupCoordinates()

        pickDateText = binding.pickDateText
        binding.datePickerListener = this

        addingTasksDialog = AddingTasksDialog(this)
        binding.openTasksListener = addingTasksDialog!!

        categoriesAdapter = CategoriesAdapter(this, android.R.layout.select_dialog_item, categoriesArray)
        categoryAutocomplete = binding.categoryAutocomplete
        categoryAutocomplete.setAdapter(categoriesAdapter)
        loadingCategoriesProgressBar = binding.categoryAutocompleteProgressBar
        subscription = RxTextView.textChangeEvents(categoryAutocomplete)
                .filter { e -> e.count() == 3 }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { e ->
                    Log.d("#MY " + javaClass.simpleName, "start loading categories. subname : " + e.text())
                    visibleCategoryProgressBar()
                    presenter.loadCategoriesBySubname(e.text().toString())
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.datePickerListener = null
        binding.openTasksListener = null
        binding.listener = null
    }

    override fun updateAddress(textAddress: String) {
        Log.d(TAG, "setted address : " + textAddress)
        data.address.set(textAddress)
    }

    override fun openDatePicker() {
        val alertDialog = AlertDialog.Builder(this).create()
        datepickerDialogViewBinding = DateTimePickerBinding.inflate(LayoutInflater.from(this))
        val datePickerDialogView = datepickerDialogViewBinding!!.root
        val datePickerSetBtn = datepickerDialogViewBinding!!.datePickerSetBtn
        val pickDateText = binding.pickDateText
        datepickerDialogViewBinding!!.datePicker.minDate = Date().time - DateUtils.ONE_MINUTE
        datepickerDialogViewBinding!!.datePicker.maxDate = Date().time + DateUtils.ONE_MONTH

        datePickerSetBtn.setOnClickListener {
            val datepicker = datepickerDialogViewBinding!!.datePicker
            val timepicker = datepickerDialogViewBinding!!.timePicker
            GregorianCalendar.getInstance().timeInMillis
            settedDate = GregorianCalendar(
                    datepicker.year,
                    datepicker.month,
                    datepicker.dayOfMonth,
                    timepicker.currentHour,
                    timepicker.currentMinute)
            val settedTime = settedDate!!.timeInMillis
            if (settedTime > Date().time) {
                pickDateText.text = DateUtils.toLongDateString(settedDate!!)
                alertDialog.hide()
            } else {
                showToast(getString(R.string.cant_create_event_in_past))
            }
        }
        alertDialog.setView(datePickerDialogView)
        alertDialog.show()

    }

    override fun onCategoriesReady(categories: ArrayList<Category>) {

    }

    override fun onCategoriesLoaded(categories: ArrayList<Category>) {
        categoriesAdapter.clear()
        categoriesAdapter.addAll(categories)
    }

    override fun visibleCategoryProgressBar() {
        loadingCategoriesProgressBar.visibility = View.VISIBLE
    }

    override fun invisibleCategoryProgressBar() {
        loadingCategoriesProgressBar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        binding.listener = this
        binding.openTasksListener = addingTasksDialog!!
//        binding.tasksListener = addingTasksDialog?.binding?.listener
    }

    override fun onStop() {
        super.onStop()
        subscription?.unsubscribe()
        binding.listener = null
        binding.openTasksListener = null
//        binding.tasksListener = null
    }

    override fun onPause() {
        super.onPause()
        KeyboardUtil.hideKeyboard(this)
    }

    private fun setupCoordinates() {

    }

    override fun onButtonClicked() {
        binding.addButton.isClickable = false

        val nameCategory: String = binding.categoryAutocomplete.text.toString()
        if (nameCategory == "") {
            unsuccessAddition("Вид спорта не задан")
            return
        }

        val maxPeople: Int
        try {
            maxPeople = Integer.parseInt(binding.eventMaxPeople.text.toString())
        } catch (e: NumberFormatException) {
            unsuccessAddition("Количество людей не задано")
            return
        }

        if (maxPeople < 2) {
            unsuccessAddition("Количество людей должно быть больше двух")
            return
        }

        if (settedDate == null) {
            unsuccessAddition("Дата события не задана")
            return
        }

        event.date = settedDate?.time?.time ?: 0
        event.description = binding.description.text.toString()
        event.maxPeople = maxPeople
        event.tasks = addingTasksDialog!!.addTaskAdapter.getTasks() as ArrayList<Task>
        event.category.name = nameCategory
        event.isJoined = binding.addMeNow.isChecked

        presenter.addEventClicked(event, binding.addMeNow.isChecked)

    }

    private fun unsuccessAddition(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        binding.addButton.isClickable = true
    }

    override fun onEventAdded(event: Event) {
        showToast(R.string.event_added)
        val intent = Intent()
        intent.putExtra(KEY_EVENT, event)
        setResult(RESULT_OK, intent)
        handler.postDelayed({
            finish()
        }, 300)

    }

    override fun showAddError(errorText: String) {
        showToast(errorText)
    }

    override fun resultSended() {
        showToast(android.R.string.ok)
        handler.postDelayed({
            finish()
        }, 300)
    }

    companion object {
        val TAG = "#MY " + AddEventActivity::class.java.simpleName

//        @JvmStatic
//        fun startForResultEvent(c: Context, id: Long) {
//            val intent = Intent(c, AddEventActivity::class.java)
//            intent.putExtra(KEY_ID, id)
//            c.startActivity(intent)
//        }

        @JvmStatic
        fun startForResult(f: PresenterFragment<*>, event: Event, code: Int) {
            with(Intent(f.context, AddEventActivity::class.java)) {
                putExtra(KEY_EVENT, event)
                f.startActivityForResult(this, code)
            }
        }

        @JvmStatic private val KEY_ID = "ID"
        @JvmStatic val KEY_EVENT = "event"
    }
}
