package com.demo.voice

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

@SuppressLint("Instantiatable")
internal class VoiceLevelView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val mGreen = ContextCompat.getDrawable(context, R.drawable.greenbar)!!
    private val mRed = ContextCompat.getDrawable(context, R.drawable.redbar)!!
    private val mBackgroundPaint: Paint
    private val mHeight: Int
    private val mWidth: Int
    private var mThreshold = 0
    private var mVol = 0

    init {
        mWidth = mGreen.intrinsicWidth
        minimumWidth = mWidth * 10
        mHeight = mGreen.intrinsicHeight
        minimumHeight = mHeight
        //Used to paint canvas background color
        mBackgroundPaint = Paint()
        mBackgroundPaint.color = Color.BLACK
    }

    fun setLevel(volume: Int, threshold: Int) {
        if (volume == mVol && threshold == mThreshold) return
        mVol = volume
        mThreshold = threshold
        // invalidate Call onDraw method and draw voice points
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPaint(mBackgroundPaint)
        for (i in 0..mVol) {
            val bar = if (i < mThreshold)
                mGreen
            else
                mRed
            bar.setBounds((10 - i) * mWidth, 0, (10 - i + 1) * mWidth, mHeight) //
            bar.draw(canvas)
        }
    }
}
