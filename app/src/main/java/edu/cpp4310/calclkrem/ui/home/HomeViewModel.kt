package edu.cpp4310.calclkrem.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Why did the chicken cross the road? To get to the other side!"
    }
    val text: LiveData<String> = _text
}