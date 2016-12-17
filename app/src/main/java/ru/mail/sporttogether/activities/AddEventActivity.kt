package ru.mail.sporttogether.activities

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
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
import ru.mail.sporttogether.adapter.CategoriesAdapter
import ru.mail.sporttogether.data.binding.event.EventData
import ru.mail.sporttogether.data.binding.event.EventListener
import ru.mail.sporttogether.data.binding.tasks.AddTasksData
import ru.mail.sporttogether.data.binding.tasks.AddTasksListener
import ru.mail.sporttogether.databinding.ActivityAddEventBinding
import ru.mail.sporttogether.databinding.AddingTasksBinding
import ru.mail.sporttogether.databinding.DateTimePickerBinding
import ru.mail.sporttogether.mvp.presenters.event.AddEventPresenter
import ru.mail.sporttogether.mvp.presenters.event.AddEventPresenterImpl
import ru.mail.sporttogether.mvp.views.event.IAddEventView
import ru.mail.sporttogether.net.models.Category
import ru.mail.sporttogether.net.models.Task
import ru.mail.sporttogether.utils.DateUtils
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.*

class AddEventActivity :
        PresenterActivity<AddEventPresenter>(),
        IAddEventView,
        EventListener {

    private lateinit var binding: ActivityAddEventBinding

    private val data = EventData()
    private lateinit var lat: String

    private lateinit var lng: String
    private val handler = Handler()
    private var eventId = 0L
    private var settedDate: GregorianCalendar = GregorianCalendar()
    private lateinit var pickDateText: TextView

    private var addTasksDialog: AddTasksDialog? = null

    private lateinit var categoryAutocomplete: AutoCompleteTextView
    private var categoriesArray: ArrayList<Category> = ArrayList()
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var loadingCategoriesProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AddEventPresenterImpl(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_event)
        binding.data = data


        val intent = intent
        eventId = intent.getLongExtra(KEY_ID, 0L)
        if (eventId != 0L) {
            data.resultVisibility.set(true)
            data.mainDataVisibility.set(false)
            return
        }
        with(intent) {
            lat = getDoubleExtra(KEY_LAT, 0.0).toString()
            lng = getDoubleExtra(KEY_LNG, 0.0).toString()
        }
        setupCoordinates()

        pickDateText = binding.pickDateText
        initPickDate()

        addTasksDialog = AddTasksDialog(this)

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

    private fun initPickDate() {
        pickDateText.text = DateUtils.format(settedDate)

        val mPickDateBtn = binding.pickDateButton

        mPickDateBtn.setOnClickListener {
            Log.d("#MY " + javaClass.simpleName, "on pick date btn click")
            val alertDialog = AlertDialog.Builder(this).create()
            val datepickerDialogViewBinding = DateTimePickerBinding.inflate(LayoutInflater.from(this), null, false)
            val datepickerDialogView = datepickerDialogViewBinding.root
            val datePickerSetBtn = datepickerDialogViewBinding.datePickerSetBtn
            val pickDateText = binding.pickDateText

            datePickerSetBtn.setOnClickListener {
                Log.d("#MY " + javaClass.simpleName, "on set btn click")
                val datepicker = datepickerDialogViewBinding.datePicker
                val timepicker = datepickerDialogViewBinding.timePicker
                settedDate = GregorianCalendar(
                        datepicker.year,
                        datepicker.month,
                        datepicker.dayOfMonth,
                        timepicker.currentHour,
                        timepicker.currentMinute)
                pickDateText.text = DateUtils.format(settedDate)
                alertDialog.hide()
            }
            alertDialog.setView(datepickerDialogView)
            alertDialog.show()
        }
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

    var subscription: Subscription? = null

    override fun onStart() {
        super.onStart()
        binding.listener = this
        binding.tasksListener = addTasksDialog?.binding?.listener

    }

    override fun onStop() {
        super.onStop()
        subscription?.unsubscribe()
        binding.listener = null
        binding.tasksListener = null

    }

    override fun onPause() {
        super.onPause()
        KeyboardUtil.hideKeyboard(this)
    }

    private fun setupCoordinates() {
        data.lat.set(lat)
        data.lng.set(lng)
    }

    override fun onAddButtonClicked() {
        if (data.resultVisibility.get()) {
            presenter.sendResult(eventId, binding.description.text.toString())
            return
        }

        val name = binding.eventName.text.toString()
        val nameCategory: String = binding.categoryAutocomplete.text.toString()
        Log.d("#MY " + javaClass.simpleName, "category name : " + nameCategory)

        if (nameCategory == "") {
            Toast.makeText(this, "Вид спорта не задан", Toast.LENGTH_SHORT).show()
            return
        }
        val maxPeople = Integer.parseInt(binding.eventMaxPeople.text.toString())
        Log.d("#MY " + javaClass.simpleName, "max people : " + maxPeople)
        Log.d("#MY " + javaClass.simpleName, "lat : " + lat)
        Log.d("#MY " + javaClass.simpleName, "lon : " + lng)
        Log.d("#MY " + javaClass.simpleName, "tasks count : " + addTasksDialog?.tasks?.size)

        presenter.addEventClicked(name,
                nameCategory,
                settedDate.time,
                lat.toDouble(),
                lng.toDouble(),
                binding.description.text.toString(),
                maxPeople,
                addTasksDialog!!.tasks,
                binding.addMeNow.isChecked
        )
    }

    override fun onEventAdded(name: String) {
        showToast(R.string.event_added)
        handler.postDelayed({
            finish()
        }, 300)
    }

    override fun showAddError(errorText: String) {
        showToast(errorText)
    }

    override fun resultSended() {
        showToast("ok")
        handler.postDelayed({
            finish()
        }, 300)
    }

    companion object {

        @JvmStatic
        fun start(c: Context, lng: Double, lat: Double) {
            val intent = Intent(c, AddEventActivity::class.java)
            intent.putExtra(KEY_LNG, lng)
            intent.putExtra(KEY_LAT, lat)
            c.startActivity(intent)
        }

        @JvmStatic
        fun startForResultEvent(c: Context, id: Long) {
            val intent = Intent(c, AddEventActivity::class.java)
            intent.putExtra(KEY_ID, id)
            c.startActivity(intent)
        }

        @JvmStatic private val KEY_ID = "ID"
        @JvmStatic private val KEY_LNG = "lng"
        @JvmStatic private val KEY_LAT = "lat"
    }

    class AddTasksDialog(val context: AddEventActivity) : AddTasksListener {
        var dialog: AlertDialog = AlertDialog.Builder(this.context).create()
        var binding: AddingTasksBinding = AddingTasksBinding.inflate(LayoutInflater.from(this.context), null, false)
        val tasks = ArrayList<Task>()

        init {
            this.dialog.setView(this.binding.root)
            this.binding.listener = this
            this.binding.data = AddTasksData()
        }

        override fun onOpenTasksClicked() {
            Log.d("#MY " + javaClass.simpleName, "open tasks clicked!")
            this.dialog.show()
        }

        override fun onCloseTasksClicked() {
            this.dialog.hide()
        }

        override fun onAddTaskClicked() {
            Log.d("#MY " + javaClass.simpleName, "add task clicked!")
            val taskMessage = this.binding.addTasksEdit.text.toString()
            if (taskMessage.length < 3 && taskMessage.length > 31) {
                context.showToast("Введенная задача некорректна")
                return
            }
            tasks.add(Task(null, taskMessage, null, null))
            this.binding.data.tasks.set(toTasksString())
            this.binding.addTasksEdit.text.clear()
        }

        override fun onRemoveTaskClicked() {
            if (tasks.isNotEmpty()) {
                tasks.removeAt(tasks.size - 1)
            } else {
                context.showToast("список задач пуст")
            }
            this.binding.data.tasks.set(toTasksString())
        }

        fun toTasksString(): String {
            val sb = StringBuilder("")
            var i = 1
            tasks.forEach {
                sb.append(i).append(". ").append(it.message).append('\n')
                i++
            }
            val finalString = sb.toString()
            if (finalString.isNotEmpty())
                return finalString.substring(0, finalString.length - 1)
            else
                return ""//убираю конечный перевод строки
        }

    }
}
