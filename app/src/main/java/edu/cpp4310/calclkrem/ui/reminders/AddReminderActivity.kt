package edu.cpp4310.calclkrem.ui.reminders

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.cpp4310.calclkrem.R

class AddReminderActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_reminder)
        this.supportActionBar?.hide()
    }
}