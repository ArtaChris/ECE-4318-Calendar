package edu.cpp4310.calclkrem.ui.reminders

import java.util.*

class ReminderItem {
    companion object{
        var calendar: Calendar = Calendar.getInstance()
        var title: String = ""
    }

    fun ReminderItem(c: Calendar, t: String){
        calendar = c
        title = t
    }

    fun getCalendar(): Calendar{
        return calendar
    }

    fun getTitle(): String{
        return title
    }
}