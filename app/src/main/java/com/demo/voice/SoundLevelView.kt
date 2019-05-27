package com.demo.voice

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View

@SuppressLint("Instantiatable")
internal class SoundLevelView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val mGreen: Drawable
    private val mRed: Drawable
    private val mBackgroundPaint: Paint

    private val mHeight: Int
    private val mWidth: Int

    private var mThreshold = 0
    private var mVol = 0


    init {
        mGreen = context.resources.getDrawable(
                R.drawable.greenbar)
        mRed = context.resources.getDrawable(
                R.drawable.redbar)
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

    public override fun onDraw(canvas: Canvas) {

        canvas.drawPaint(mBackgroundPaint)

        for (i in 0..mVol) {
            val bar: Drawable
            if (i < mThreshold)
                bar = mGreen
            else
                bar = mRed

            bar.setBounds((10 - i) * mWidth, 0, (10 - i + 1) * mWidth, mHeight) //
            bar.draw(canvas)
        }
    }
}
