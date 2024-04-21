package com.smaart.idrico.model

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

fun View.setOnSwipeListener(callback: () -> Unit) {
    val gestureDetector = GestureDetector(context, Swipe(callback))
    setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }
}

class Swipe(private val callback: () -> Unit) : GestureDetector.SimpleOnGestureListener() {
    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }
    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (e1 != null) {
            val diffX = e2.x - e1.x
            if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    callback.invoke()
                }
            }
        }
        Log.e("TEST","FLINGED")
        return super.onFling(e1, e2, velocityX, velocityY)
    }
    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        return true
    }
}

