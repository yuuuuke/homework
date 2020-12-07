package com.yuke.homework.view.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.lang.reflect.InvocationTargetException
import kotlin.math.abs

class AutoSwipeRefreshLayout : SwipeRefreshLayout {
    private var xDown = 0f
    private var yDown = 0f
    private var xDistance = 0f
    private var yDistance = 0f

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
        init()
    }

    private fun init() {
        setColorSchemeColors(Color.parseColor("#0888ff"))
        setDistanceToTriggerSync(200)
    }

    fun autoRefresh() {
        try {
            val mCircleView =
                SwipeRefreshLayout::class.java.getDeclaredField("mCircleView")
            mCircleView.isAccessible = true
            val progress = mCircleView[this] as View
            progress.visibility = View.VISIBLE
            val animatorX =
                ObjectAnimator.ofFloat(progress, "scaleX", *floatArrayOf(0.0f, 1.0f))
            val animatorY =
                ObjectAnimator.ofFloat(progress, "scaleY", *floatArrayOf(0.0f, 1.0f))
            animatorX.duration = 500L
            animatorY.duration = 500L
            animatorX.start()
            animatorY.start()
            val setRefreshing =
                SwipeRefreshLayout::class.java.getDeclaredMethod(
                    "setRefreshing",
                    java.lang.Boolean.TYPE,
                    java.lang.Boolean.TYPE
                )
            setRefreshing.isAccessible = true
            setRefreshing.invoke(this, true, true)
        } catch (var6: NoSuchFieldException) {
            var6.printStackTrace()
        } catch (var7: IllegalAccessException) {
            var7.printStackTrace()
        } catch (var8: NoSuchMethodException) {
            var8.printStackTrace()
        } catch (var9: InvocationTargetException) {
            var9.printStackTrace()
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            0 -> {
                xDown = ev.x
                yDown = ev.y
            }
            2 -> {
                xDistance = abs(ev.x - xDown)
                yDistance = abs(ev.y - yDown)
            }
        }
        return if (xDistance < yDistance) super.onInterceptTouchEvent(ev) else false
    }
}