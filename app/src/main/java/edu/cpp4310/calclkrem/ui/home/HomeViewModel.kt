package edu.cpp4310.calclkrem.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Calendar related functionality will go here. To access or change this text, go to"+
                " java/edu.cpp4310.calclkrem/ui/home/HomeViewModel.kt."
    }
    val text: LiveData<String> = _text
}