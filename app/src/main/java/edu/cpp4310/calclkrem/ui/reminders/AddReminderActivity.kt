package edu.cpp4310.calclkrem.ui.reminders

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.format.DateFormat
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.google.android.material.chip.ChipGroup
import edu.cpp4310.calclkrem.DatePickerFragment
import edu.cpp4310.calclkrem.MyNotificationPublisher
import edu.cpp4310.calclkrem.R
import edu.cpp4310.calclkrem.TimePickerFragment
import java.text.SimpleDateFormat
import java.util.*


class AddReminderActivity: AppCompatActivity(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, View.OnClickListener {

    companion object{
        // Global Calendar variable which will be manipulated by the user
        val setCalendar: Calendar = Calendar.getInstance()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_reminder)
        this.supportActionBar?.hide()

        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Initialize notification channel
        val id = "calclkrem_remch"
        val name: CharSequence = "Reminders"
        val description = "Your reminders from CalClkRem!"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(id, name, importance)
        mChannel.description = description
        mNotificationManager.createNotificationChannel(mChannel)

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
        hour = when {
                    !android.text.format.DateFormat.is24HourFormat(this) and (hourOfDay > 12) -> hourOfDay - 12
                    hourOfDay == 0 -> 12
                    else -> hourOfDay
                }
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
        setCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        setCalendar.set(Calendar.MINUTE, minute)
        setCalendar.set(Calendar.SECOND, 0)
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
        setCalendar.set(Calendar.YEAR, year)
        setCalendar.set(Calendar.MONTH, month)
        setCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        setDate.text = date
    }

    override fun onClick(v: View?) {
        val today = Calendar.getInstance()
        when(v?.id){
            R.id.addReminder_SubmitButton -> {
                val titleTextBox: EditText = findViewById(R.id.addReminder_TitleTextBox)
                if (titleTextBox.text.isBlank()) {
                    val noTitleError = Toast.makeText(applicationContext, "Title cannot be blank!", Toast.LENGTH_LONG)
                    noTitleError.show()
                    return
                }
                val repeatingCheckBox: CheckBox = findViewById(R.id.addReminder_repeatingCheckBox)
                if (!repeatingCheckBox.isChecked) {
                    val timeUntil: Int = (setCalendar.timeInMillis.toInt() - today.timeInMillis.toInt())
                    if (timeUntil <= 0) {
                        val noTimeMachine = Toast.makeText(applicationContext, "Event cannot be in the past!", Toast.LENGTH_LONG)
                        noTimeMachine.show()
                        return
                    }
                    setReminder(false)
                    val reminderSet = Toast.makeText(applicationContext, "Reminder added!", Toast.LENGTH_SHORT)
                    reminderSet.show()
                    finish()
                    return
                }
                else{
                    val radioGroup: RadioGroup = findViewById(R.id.addReminder_RadioGroup)
                    val rbDOW: RadioButton = findViewById(R.id.addReminder_RBDOW)
                    val chipGroup : ChipGroup = findViewById(R.id.addReminder_ChipGroup)
                    val rbNOD: RadioButton = findViewById(R.id.addReminder_RBNOD)
                    val rbNOW: RadioButton = findViewById(R.id.addReminder_RBNOW)
                    val rbNOM: RadioButton = findViewById(R.id.addReminder_RBNOM)
                    val rbNOY: RadioButton = findViewById(R.id.addReminder_RBNOY)
                    if(radioGroup.checkedRadioButtonId == -1){
                        val selectSomething = Toast.makeText(applicationContext, "Please choose how this event repeats!", Toast.LENGTH_LONG)
                        selectSomething.show()
                        return
                    }
                    if(rbDOW.isChecked && chipGroup.checkedChipIds.isEmpty()){
                        val selectDays = Toast.makeText(applicationContext, "Please choose on what day this event repeats!", Toast.LENGTH_LONG)
                        selectDays.show()
                        return
                    }
                    if((rbNOD.isChecked && (findViewById<EditText>(R.id.addReminder_NOD_Input)).text.isBlank())
                            || (rbNOW.isChecked && (findViewById<EditText>(R.id.addReminder_NOW_Input)).text.isBlank())
                            || (rbNOM.isChecked && (findViewById<EditText>(R.id.addReminder_NOM_Input)).text.isBlank())
                            || (rbNOY.isChecked && (findViewById<EditText>(R.id.addReminder_NOY_Input)).text.isBlank())){
                        val inputSomething = Toast.makeText(applicationContext, "Please input how often this event repeats!", Toast.LENGTH_LONG)
                        inputSomething.show()
                    }
                    if((rbNOD.isChecked && (findViewById<EditText>(R.id.addReminder_NOD_Input)).text.toString() == "0")
                            || (rbNOW.isChecked && (findViewById<EditText>(R.id.addReminder_NOW_Input)).text.toString() == "0")
                            || (rbNOM.isChecked && (findViewById<EditText>(R.id.addReminder_NOM_Input)).text.toString() == "0")
                            || (rbNOY.isChecked && (findViewById<EditText>(R.id.addReminder_NOY_Input)).text.toString() == "0")){
                        val cannotBeZeroError = Toast.makeText(applicationContext, "Value cannot be zero!", Toast.LENGTH_LONG)
                        cannotBeZeroError.show()
                    }
                    setReminder(true)
                    val reminderSet = Toast.makeText(applicationContext, "Reminder added!", Toast.LENGTH_SHORT)
                    reminderSet.show()
                    finish()
                    return
                }
            }
        }
    }

    fun onCheckboxClicked(view: View){
        // Check if checkbox is checked
        val checked = (view as CheckBox).isChecked
        val rg: View = findViewById(R.id.addReminder_RadioGroup)

        // Show Radio Group and proper Linear Layout if checked, hide if not
        if(checked) {
            rg.visibility = VISIBLE
            val rbDOW: RadioButton = findViewById<RadioButton>(R.id.addReminder_RBDOW)
            val rbNOD: RadioButton = findViewById<RadioButton>(R.id.addReminder_RBNOD)
            val rbNOW: RadioButton = findViewById<RadioButton>(R.id.addReminder_RBNOW)
            val rbNOM: RadioButton = findViewById<RadioButton>(R.id.addReminder_RBNOM)
            val rbNOY: RadioButton = findViewById<RadioButton>(R.id.addReminder_RBNOY)
            when((rbDOW.isChecked || rbNOD.isChecked || rbNOM.isChecked || rbNOW.isChecked || rbNOY.isChecked)){
                rbDOW.isChecked -> {
                    radioSelect(rbDOW)
                }
                rbNOD.isChecked -> {
                    radioSelect(rbNOD)
                }
                rbNOM.isChecked -> {
                    radioSelect(rbNOM)
                }
                rbNOW.isChecked -> {
                    radioSelect(rbNOW)
                }
                rbNOY.isChecked -> {
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
                R.id.addReminder_RBDOW -> {
                    if (checked) (findViewById<View>(R.id.addReminder_LinearLayoutDOW)).visibility = VISIBLE
                }
                R.id.addReminder_RBNOD -> {
                    if (checked) (findViewById<View>(R.id.addReminder_LinearLayoutNOD)).visibility = VISIBLE
                }
                R.id.addReminder_RBNOW -> {
                    if (checked) (findViewById<View>(R.id.addReminder_LinearLayoutNOW)).visibility = VISIBLE
                }
                R.id.addReminder_RBNOM -> {
                    if (checked) (findViewById<View>(R.id.addReminder_LinearLayoutNOM)).visibility = VISIBLE
                }
                R.id.addReminder_RBNOY -> {
                    if (checked) (findViewById<View>(R.id.addReminder_LinearLayoutNOY)).visibility = VISIBLE
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

    private fun setReminder(isRepeating: Boolean){
        val today: Calendar = Calendar.getInstance()
        val timeUntil = (setCalendar.timeInMillis - today.timeInMillis)
        val titleTextBox: EditText = findViewById(R.id.addReminder_TitleTextBox)
        scheduleNotification(this, timeUntil, 1001, titleTextBox.text.toString(), isRepeating)
    }

    private fun scheduleNotification(context: Context, delay: Long, notificationId: Int, content: String, isRepeating: Boolean) {
        //delay is after how much time(in millis) from current time you want to schedule the notification
        val builder = NotificationCompat.Builder(context, "calclkrem_remch")
                .setContentTitle("Reminder")
                .setContentText(content)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setLargeIcon((context.getResources().getDrawable(R.drawable.ic_launcher) as BitmapDrawable).bitmap)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        val intent = Intent(context, AddReminderActivity::class.java)
        val activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        builder.setContentIntent(activity)
        val notification = builder.build()
        val notificationIntent = Intent(context, MyNotificationPublisher::class.java)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, notificationId)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val futureInMillis = SystemClock.elapsedRealtime() + delay
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if(!isRepeating) {
            alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis] = pendingIntent
        }
        else{
            val rbDOW: RadioButton = findViewById(R.id.addReminder_RBDOW)
            val rbNOD: RadioButton = findViewById(R.id.addReminder_RBNOD)
            val rbNOW: RadioButton = findViewById(R.id.addReminder_RBNOW)
            val rbNOM: RadioButton = findViewById(R.id.addReminder_RBNOM)
            val rbNOY: RadioButton = findViewById(R.id.addReminder_RBNOY)
            var interval: Long = 1000 * 60 * 60 * 24 // 1 day by default
            when(true){
                rbDOW.isChecked ->{
                    //Input later!
                }
                rbNOD.isChecked ->{
                    val input = (findViewById<EditText>(R.id.addReminder_NOD_Input).text.toString().toLong())
                    interval = input * 1000 * 60 * 60 * 24
                }
                rbNOW.isChecked ->{
                    val input = findViewById<EditText>(R.id.addReminder_NOW_Input).text.toString().toLong()
                    interval = input * 1000 * 60 * 60 * 24 * 7
                }
                rbNOM.isChecked ->{
                    val input = findViewById<EditText>(R.id.addReminder_NOM_Input).text.toString().toLong()
                    // Assume there are 30 days in a month, can be fixed with calendar input
                    interval = input * 1000 * 60 * 60 * 24 * 30
                }
                rbNOY.isChecked ->{
                    val input = (findViewById<EditText>(R.id.addReminder_NOY_Input).text.toString().toLong())
                    // Assume there are 365 days in a year
                    interval = input * 1000 * 60 * 60 * 24 * 365
                }
            }
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, setCalendar.timeInMillis, interval, pendingIntent)
            val setAlarm = Toast.makeText(applicationContext, "Set a timer at ${setCalendar.timeInMillis} every $interval", Toast.LENGTH_LONG)
            setAlarm.show()
        }
    }
}