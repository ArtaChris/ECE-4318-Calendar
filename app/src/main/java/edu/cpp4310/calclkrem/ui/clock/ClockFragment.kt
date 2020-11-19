package edu.cpp4310.calclkrem.ui.clock

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.cpp4310.calclkrem.R
import edu.cpp4310.calclkrem.ui.reminders.AddReminderActivity

class ClockFragment : Fragment(), View.OnClickListener {

    private lateinit var clockViewModel: ClockViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        clockViewModel =
                ViewModelProvider(this).get(ClockViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val addClk: FloatingActionButton = root.findViewById(R.id.clock_FloatingActionButton)
        addClk.setOnClickListener(this)
        return root
    }

    override fun onClick(v: View?)
    {
        when (v?.id){
            R.id.clock_FloatingActionButton ->{
                val intent = Intent(activity, ClockActivity::class.java)
                activity?.startActivity(intent)
            }
        }
    }
}