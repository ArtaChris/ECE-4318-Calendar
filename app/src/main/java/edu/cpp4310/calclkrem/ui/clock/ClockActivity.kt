package edu.cpp4310.calclkrem.ui.clock

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.media.RingtoneManager
import android.media.Ringtone
import android.os.*
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.Toast
import edu.cpp4310.calclkrem.R


class ClockActivity: AppCompatActivity() {
    var START_MILLI_SECONDS = 0L //Initialize timer to start if there is no input

    lateinit var countdown_timer : CountDownTimer
    var isRunning = false;  //Check if timer is running
    var millis = 0L
    private var StartTimeInMillis = 0L
    private var TimeLeftInMillis = 0L


    lateinit var vibratorService : Vibrator

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.set).setOnClickListener {
            getTime()
        }

        //start or pause timer when Start button is pressed
        findViewById<Button>(R.id.start).setOnClickListener{
            if (isRunning){
                pauseTimer()
            }
            else{
                startTimer()
            }
        }
        //Reset timer when "reset" button is pressed
        findViewById<Button>(R.id.reset).setOnClickListener {
            resetTimer()
        }

    }

    // Calculate time
    private fun getTime(){
        //Get time from user inputs
        val time = findViewById<EditText>(R.id.time_edit_text)
        val sec = findViewById<EditText>(R.id.seconds_edit_text)
        val hour = findViewById<EditText>(R.id.hours_edit_text)
        //Convert to string
        var hourValue = hour.text.toString()
        var timeValue = time.text.toString()
        var secondsValue = sec.text.toString()
        // If no input, get zero
        if (hourValue.isEmpty())
            hourValue = "0"
        if (timeValue.isEmpty())
            timeValue = "0"
        if (secondsValue.isEmpty())
            secondsValue = "0"
        // Calculate total time
        millis = (hourValue.toLong()*3600000) + (timeValue.toLong() * 60000L) + (secondsValue.toLong() * 1000L)
        setTime(millis)
    }

    private fun startTimer(){
        countdown_timer = object : CountDownTimer(TimeLeftInMillis, 1000)
        {
            override fun onFinish() {
                isRunning = false //return false when timer runs out
                alarmOn() //Sound alarm
            }
            override fun onTick(millisUntilFinished: Long) {
                //Update UI as the timer counts down
                TimeLeftInMillis = millisUntilFinished
                updateTextUI()
            }
        }
        countdown_timer.start() //Start timer
        isRunning = true   //Return true, so next time start button is pressed the timer is paused
        findViewById<Button>(R.id.start).text = "Pause" //change start button to display "pause"
        findViewById<Button>(R.id.reset).visibility = View.INVISIBLE //Reset button is invisible
    }

    private fun setTime(milliseconds: Long) {
        StartTimeInMillis = milliseconds
        resetTimer()
        // Close keyboard and clear text bars after time is set
        closeKeyboard()
        findViewById<EditText>(R.id.time_edit_text).setText("")
        findViewById<EditText>(R.id.seconds_edit_text).setText("")
        findViewById<EditText>(R.id.hours_edit_text).setText("")

    }
    // Reset timer
    private fun resetTimer(){
        TimeLeftInMillis = StartTimeInMillis
        updateTextUI()
        findViewById<Button>(R.id.reset).visibility = View.INVISIBLE
        findViewById<Button>(R.id.start).text = "Start"
    }
    //Pause Timer
    private fun pauseTimer(){
        val start = findViewById<Button>(R.id.start) //Pause timer when start button is clicked
        start.text = "Resume"
        countdown_timer.cancel()
        isRunning = false //Return false so next time start button is clicked timer resumes
        findViewById<Button>(R.id.reset).visibility = View.VISIBLE //Make reset button available
    }

    private fun updateTextUI() {
        //Convert milliseconds to hours, minutes, and seconds
        var hours = ((TimeLeftInMillis / (1000*60*60)) % 24)
        var minute = ((TimeLeftInMillis / (1000 * 60)) % 60)
        var seconds = (TimeLeftInMillis / 1000) % 60

        //Get first and second digit for minutes and seconds
        var firstMin = minute.toInt() / 10
        var secMin = minute.toInt() % 10
        var firstSec = seconds.toInt() / 10
        var secSec = seconds.toInt() % 10
        //Display time
        findViewById<TextView>(R.id.timer).text = "$hours:$firstMin$secMin:$firstSec$secSec"
    }

    private fun alarmOn(){

        val ringtone:Ringtone = defaultRingtone
        ringtone.play()

        //Vibrate
        vibratorService = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibratorService.vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE))
        }
        //Update buttons, when alarm goes off start button is not visible and stop button becomes visible to stop the alarm
        findViewById<Button>(R.id.start).visibility = View.INVISIBLE
        findViewById<Button>(R.id.stop).visibility = View.VISIBLE

        //Stop timer
        findViewById<Button>(R.id.stop).setOnClickListener {
            ringtone.stop()
            resetTimer()
            vibratorService.cancel() //Stop vibration
            //update buttons
            findViewById<Button>(R.id.start).visibility = View.VISIBLE
            findViewById<Button>(R.id.stop).visibility = View.INVISIBLE
        }
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    private val Context.defaultRingtone:Ringtone
        get() {
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            return RingtoneManager.getRingtone(this, uri)
        }

}