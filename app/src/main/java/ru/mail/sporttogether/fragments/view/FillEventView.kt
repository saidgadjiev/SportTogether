package ru.mail.sporttogether.fragments.view

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import ru.mail.sporttogether.data.binding.AddEventListener
import ru.mail.sporttogether.data.binding.ToolbarWithButtonListener
import ru.mail.sporttogether.mvp.IView
import ru.mail.sporttogether.net.CategoriesResponse

/**
 * Created by bagrusss on 14.02.17
 */
interface FillEventView :
        IView,
        AddEventListener,
        ToolbarWithButtonListener,
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    fun updateAddress(address: String)
    fun nextStep()
    fun onCategoriesLoaded(data: CategoriesResponse)
}