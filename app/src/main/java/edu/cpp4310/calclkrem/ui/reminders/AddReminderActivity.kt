package edu.cpp4310.calclkrem.ui.reminders

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import edu.cpp4310.calclkrem.DatePickerFragment
import edu.cpp4310.calclkrem.R
import edu.cpp4310.calclkrem.TimePickerFragment
import java.text.SimpleDateFormat
import java.util.*

class AddReminderActivity: AppCompatActivity(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_reminder)
        this.supportActionBar?.hide()

        val c = Calendar.getInstance()

        val setTime: Button = findViewById(R.id.addReminder_SetTimeButton)
        val setDate: Button = findViewById(R.id.addReminder_SetDateButton)

        // Set initial text for time
        val hourOfDay = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        var hour = hourOfDay
        val time = StringBuilder()
        hour = if(!android.text.format.DateFormat.is24HourFormat(this) and (hourOfDay > 12)){
            hourOfDay - 12
        } else if(!android.text.format.DateFormat.is24HourFormat(this) and (hourOfDay == 0)){
            12
        } else hourOfDay
        time.append(hour)
        time.append(":")
        if(minute < 10){
            time.append("0")
        }
        time.append(minute)
        if(!android.text.format.DateFormat.is24HourFormat(this)){
            time.append(" ")
            if(hourOfDay < 12) time.append("A.M.")
            else time.append("P.M.")
        }
        setTime.text = time

        // Set initial text for date
        var format: SimpleDateFormat = SimpleDateFormat("EEEE, MMMM d")
        val dateSet = format.format(c.time)
        setDate.text = dateSet

        setTime.setOnClickListener(View.OnClickListener {
            val timePicker = TimePickerFragment()
            timePicker.show(supportFragmentManager, "time picker")
        })

        setDate.setOnClickListener(View.OnClickListener {
            val datePicker = DatePickerFragment()
            datePicker.show(supportFragmentManager, "date picker")
        })
    }

    // Set text after selecting time
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val setTime: Button = findViewById(R.id.addReminder_SetTimeButton)
        var hour = hourOfDay
        val time = StringBuilder()
        hour = if(!android.text.format.DateFormat.is24HourFormat(this) and (hourOfDay > 12)){
            hourOfDay - 12
        } else hourOfDay
        time.append(hour)
        time.append(":")
        if(minute < 10){
            time.append("0")
        }
        time.append(minute)
        if(!android.text.format.DateFormat.is24HourFormat(this)){
            time.append(" ")
            if(hourOfDay < 12) time.append("A.M.")
            else time.append("P.M.")
        }
        setTime.text = time
    }

    // Set text after selecting date
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val setDate: Button = findViewById(R.id.addReminder_SetDateButton)
        val format: SimpleDateFormat = SimpleDateFormat("EEEE, MMMM d")
        var c = Calendar.getInstance()
        c.set(year, month, dayOfMonth)
        val date = StringBuilder()
        date.append(format.format(c.time))
        val today = Calendar.getInstance()
        if(c.get(Calendar.YEAR) != today.get(Calendar.YEAR)){
            date.append(", ")
            date.append(year)
        }
        setDate.text = date
    }
}