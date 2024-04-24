package com.smaart.idrico.model
import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import kotlin.math.abs

class Swipe(context: Context) : View.OnTouchListener {
    lateinit var formLayouts:MutableMap<String, LinearLayout>
    var currentLayout:String=""
    private val gestureDetector: GestureDetector
    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val diffX = e2.x - e1?.x!!
            val diffY = e2.y - e1.y
            if (abs(diffX) > abs(diffY)) {
                if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight()
                    } else {
                        onSwipeLeft()
                    }
                    return true
                }
            }
            return false
        }
    }
    fun onSwipeRight() {
        Log.e("TEST","RIGHT "+this.currentLayout)
    }
    fun onSwipeLeft() {
        Log.e("TEST","LEFT "+this.currentLayout)
    }
}
