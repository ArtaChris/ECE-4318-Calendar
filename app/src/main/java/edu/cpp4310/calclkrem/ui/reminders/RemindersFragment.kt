package edu.cpp4310.calclkrem.ui.reminders

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.cpp4310.calclkrem.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RemindersFragment : Fragment(), View.OnClickListener {

    private lateinit var remindersViewModel: RemindersViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        remindersViewModel =
                ViewModelProvider(this).get(RemindersViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_reminders, container, false)
        val addRem: FloatingActionButton = root.findViewById(R.id.reminders_FloatingActionButton)
        addRem.setOnClickListener(this)
        return root
    }

    override fun onClick(v: View?)
    {
        when (v?.id){
            R.id.reminders_FloatingActionButton ->{
                val intent = Intent(activity, AddReminderActivity::class.java)
                activity?.startActivity(intent)
            }
        }
    }
}