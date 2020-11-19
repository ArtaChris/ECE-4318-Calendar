package edu.cpp4310.calclkrem.ui.clock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClockViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Clock related functionality will go here. To access or change this text, go to " +
                "java/edu.cpp4310.calclkrem/ui/dashboard/DashboardViewModel.kt."
    }
    val text: LiveData<String> = _text
}