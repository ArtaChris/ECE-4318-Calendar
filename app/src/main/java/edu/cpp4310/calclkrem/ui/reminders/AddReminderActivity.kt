package edu.cpp4310.calclkrem.ui.reminders

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
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

        // Hide Radio Group & Linear Layouts initially
        val rg: View = findViewById(R.id.addReminder_RadioGroup)
        rg.visibility = GONE
        hideAllLinearLayouts()

        // Get current time
        val c = Calendar.getInstance()

        // Initialize buttons
        val setTime: Button = findViewById(R.id.addReminder_SetTimeButton)
        val setDate: Button = findViewById(R.id.addReminder_SetDateButton)

        // Set initial text for time
        val hourOfDay = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        var hour = hourOfDay
        val time = StringBuilder()
        hour = if(!DateFormat.is24HourFormat(this) and (hourOfDay > 12)){
            hourOfDay - 12
        } else if(!DateFormat.is24HourFormat(this) and (hourOfDay == 0)){
            12
        } else hourOfDay
        time.append(hour)
        time.append(":")
        if(minute < 10){
            time.append("0")
        }
        time.append(minute)
        if(!DateFormat.is24HourFormat(this)){
            time.append(" ")
            if(hourOfDay < 12) time.append("A.M.")
            else time.append("P.M.")
        }
        setTime.text = time

        // Set initial text for date
        var format: SimpleDateFormat = SimpleDateFormat("EEEE, MMMM d")
        val dateSet = format.format(c.time)
        setDate.text = dateSet

        // Open time picker on SetTimeButton click
        setTime.setOnClickListener(View.OnClickListener {
            val timePicker = TimePickerFragment()
            timePicker.show(supportFragmentManager, "time picker")
        })

        // Open date picker on SetDateButton click
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

    fun onCheckboxClicked(view: View){
        // Check if checkbox is checked
        val checked = (view as CheckBox).isChecked
        val rg: View = findViewById(R.id.addReminder_RadioGroup)

        // Show Radio Group if checked, hide if not
        if(checked) {
            rg.visibility = VISIBLE
            val rbDOW: RadioButton = findViewById<RadioButton>(R.id.addReminder_RBDOW)
            val rbNOD: RadioButton = findViewById<RadioButton>(R.id.addReminder_RBNOD)
            val rbNOW: RadioButton = findViewById<RadioButton>(R.id.addReminder_RBNOW)
            val rbNOM: RadioButton = findViewById<RadioButton>(R.id.addReminder_RBNOM)
            val rbNOY: RadioButton = findViewById<RadioButton>(R.id.addReminder_RBNOY)
            when((rbDOW.isChecked || rbNOD.isChecked || rbNOM.isChecked || rbNOW.isChecked || rbNOY.isChecked)){
                rbDOW.isChecked ->{
                    radioSelect(rbDOW)
                }
                rbNOD.isChecked ->{
                    radioSelect(rbNOD)
                }
                rbNOM.isChecked ->{
                    radioSelect(rbNOM)
                }
                rbNOW.isChecked ->{
                    radioSelect(rbNOW)
                }
                rbNOY.isChecked ->{
                    radioSelect(rbNOY)
                }
            }
        }
        else {
            rg.visibility = GONE
            hideAllLinearLayouts()
        }
    }

    fun radioSelect(v: View){
        hideAllLinearLayouts()
        if(v is RadioButton){
            val checked = v.isChecked
            when(v.id){
                R.id.addReminder_RBDOW ->{
                    if(checked) (findViewById<View>(R.id.addReminder_LinearLayoutDOW)).visibility = VISIBLE
                }
                R.id.addReminder_RBNOD ->{
                    if(checked) (findViewById<View>(R.id.addReminder_LinearLayoutNOD)).visibility = VISIBLE
                }
                R.id.addReminder_RBNOW ->{
                    if(checked) (findViewById<View>(R.id.addReminder_LinearLayoutNOW)).visibility = VISIBLE
                }
                R.id.addReminder_RBNOM ->{
                    if(checked) (findViewById<View>(R.id.addReminder_LinearLayoutNOM)).visibility = VISIBLE
                }
                R.id.addReminder_RBNOY ->{
                    if(checked) (findViewById<View>(R.id.addReminder_LinearLayoutNOY)).visibility = VISIBLE
                }
            }
        }
    }

    private fun hideAllLinearLayouts(){
        (findViewById<View>(R.id.addReminder_LinearLayoutDOW)).visibility = GONE
        (findViewById<View>(R.id.addReminder_LinearLayoutNOD)).visibility = GONE
        (findViewById<View>(R.id.addReminder_LinearLayoutNOW)).visibility = GONE
        (findViewById<View>(R.id.addReminder_LinearLayoutNOM)).visibility = GONE
        (findViewById<View>(R.id.addReminder_LinearLayoutNOY)).visibility = GONE
    }
}