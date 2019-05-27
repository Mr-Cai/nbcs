package com.demo.voice

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.PowerManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : Activity() {
    /* constants */
    //MediaPlayer mp;
    internal var ran: Int = 0
    internal var mp = arrayOfNulls<MediaPlayer>(7)
    internal var r = Random()
    internal var aaa = false

    /**
     * running state
     */
    private var mRunning = false

    /**
     * config state
     */
    private var mThreshold: Int = 0

    private var mWakeLock: PowerManager.WakeLock? = null

    private val mHandler = Handler()

    /* References to view elements */
    private val mStatusView: TextView? = null
    private var mDisplay: SoundLevelView? = null

    /* sound data source */
    private var mSensor: SoundMeter? = null

    /****************** Define runnable thread again and again detect noise  */

    private val mSleepTask = Runnable {
        //Log.i("Noise", "runnable mSleepTask");

        start()
    }

    // Create runnable thread to Monitor Voice
    private val mPollTask = object : Runnable {
        override fun run() {

            val amp = mSensor!!.amplitude


            //Log.i("Noise", "runnable mPollTask");
            updateDisplay("For UCI Hackathon Videos check out our youtube channel https://www.youtube.com/channel/UCzVOOpeVLhc75DUzxdbDS0w or search for Awkward Pony", amp)

            if (amp > mThreshold) {
                callForHelp()
                aaa = true
                ///mp[0].start();
                //Log.i("Noise", "==== onCreate ===");
            }

            if (amp < mThreshold - 4 && aaa) {
                mSensor!!.stop()
                ran = r.nextInt(7)
                mp[ran]!!.start()
                aaa = false
                mSensor!!.start()
            }

            if (mp[ran]!!.isPlaying) {
                mThreshold = 80
            }
            if (!mp[ran]!!.isPlaying) {
                mThreshold = 6
            }

            // Runnable(mPollTask) will again execute after POLL_INTERVAL
            mHandler.postDelayed(this, POLL_INTERVAL.toLong())

        }
    }


    /**
     * Called when the activity is first created.
     */
    @SuppressLint("InvalidWakeLockTag")
    public override fun onCreate(savedInstanceState: Bundle?) {

        mp[0] = MediaPlayer.create(this, R.raw.darkkevin)
        mp[1] = MediaPlayer.create(this, R.raw.kevin1)
        mp[2] = MediaPlayer.create(this, R.raw.mrchow)
        mp[3] = MediaPlayer.create(this, R.raw.noah)
        mp[4] = MediaPlayer.create(this, R.raw.noah2)
        mp[5] = MediaPlayer.create(this, R.raw.noah3)
        mp[6] = MediaPlayer.create(this, R.raw.noah4)


        super.onCreate(savedInstanceState)

        // Defined SoundLevelView in main.xml file
        setContentView(R.layout.main)

        // Used to record voice
        mSensor = SoundMeter()
        mDisplay = findViewById(R.id.volume)

        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "MainActivity")
    }


    public override fun onResume() {
        super.onResume()
        //Log.i("Noise", "==== onResume ===");

        initializeApplicationConstants()
        mDisplay!!.setLevel(0, mThreshold)

        if (!mRunning) {
            mRunning = true
            start()
        }
    }

    public override fun onStop() {
        super.onStop()
        // Log.i("Noise", "==== onStop ===");

        //Stop noise monitoring
        stop()

    }

    private fun start() {
        //Log.i("Noise", "==== start ===");

        mSensor!!.start() //starts recording
        if (!mWakeLock!!.isHeld) {
            mWakeLock!!.acquire()
        }

        //Noise monitoring start
        // Runnable(mPollTask) will execute after POLL_INTERVAL
        mHandler.postDelayed(mPollTask, POLL_INTERVAL.toLong())
    }

    private fun stop() {
        Log.i("Noise", "==== Stop Noise Monitoring===")
        if (mWakeLock!!.isHeld) {
            mWakeLock!!.release()
        }
        mHandler.removeCallbacks(mSleepTask)
        mHandler.removeCallbacks(mPollTask)
        mSensor!!.stop()
        mDisplay!!.setLevel(0, 0)
        updateDisplay("stopped...", 0.0)
        mRunning = false

    }


    private fun initializeApplicationConstants() {
        // Set Noise Threshold
        mThreshold = 10

    }

    private fun updateDisplay(status: String, signalEMA: Double) {
        Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        //
        mDisplay!!.setLevel(signalEMA.toInt(), mThreshold)
    }


    private fun callForHelp() {

        //stop();

        // Show alert when noise thersold crossed
        //Toast.makeText(getApplicationContext(), "Noise Thersold Crossed, do here your stuff.",
        // Toast.LENGTH_LONG).show();
    }

    companion object {

        private val POLL_INTERVAL = 200
    }

}
