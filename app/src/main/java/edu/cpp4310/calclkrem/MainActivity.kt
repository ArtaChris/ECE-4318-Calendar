package edu.cpp4310.calclkrem

import android.content.ClipData
import android.os.Bundle
import android.widget.Adapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.cpp4310.calclkrem.ui.reminders.ReminderItem
import java.util.*

class MainActivity : AppCompatActivity() {
    val remindersList: ArrayList<ReminderItem> = ArrayList<ReminderItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun buildRemindersRecyclerView(){
        val mRecyclerView: RecyclerView = findViewById(R.id.reminders_RecyclerView)
        mRecyclerView.setHasFixedSize(true)
        val mLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
        //val mExampleList = ArrayList<>
        //val mAdapter: RecyclerView.Adapter<Objects>

        mRecyclerView.layoutManager = mLayoutManager
        //mRecyclerView.adapter = mAdapter
    }
}