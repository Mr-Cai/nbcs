package com.demo.voice


import android.media.MediaRecorder

import java.io.IOException

class SoundMeter {
    private var mRecorder: MediaRecorder? = null
    private var mEMA = 0.0
    val amplitude: Double
        get() = if (mRecorder != null)
            mRecorder!!.maxAmplitude / 2700.0
        else
            0.0

    /* val amplitudeEMA: Double
         get() {
             val amp = amplitude
             mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA
             return mEMA
         }*/

    fun start() {

        if (mRecorder == null) {

            mRecorder = MediaRecorder()
            mRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mRecorder!!.setOutputFile("/dev/null")

            try {
                mRecorder!!.prepare()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mRecorder!!.start()
            mEMA = 0.0
        }
    }

    fun stop() {
        if (mRecorder != null) {
            mRecorder!!.stop()
            mRecorder!!.release()
            mRecorder = null
        }
    }

    companion object {
        // This file is used to record voice
        private val EMA_FILTER = 0.6
    }
}
