package edu.cpp4310.calclkrem.ui.reminders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RemindersViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Reminder related functionality will go here. To access or change this text, go to " +
                "java/edu.cpp4310.calclkrem/ui/notifications/NotificationsViewModel.kt."
    }
    val text: LiveData<String> = _text
}