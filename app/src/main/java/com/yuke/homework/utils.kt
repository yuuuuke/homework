package com.yuke.homework

import android.app.Activity
import android.content.res.Resources
import android.view.View
import android.view.Window
import android.view.WindowManager

fun dp2px(dpValue: Float): Int {
    return (0.5f + dpValue * Resources.getSystem()
        .displayMetrics.density).toInt()
}

fun setStatusTextColorDark(activity: Activity) {
    activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    val clazz: Class<out Window?> = activity.window.javaClass
    try {
        var darkModeFlag = 0
        val layoutParams =
            Class.forName("android.view.MiuiWindowManager\$LayoutParams")
        val field =
            layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
        darkModeFlag = field.getInt(layoutParams)
        val extraFlagField = clazz.getMethod(
            "setExtraFlags",
            Int::class.javaPrimitiveType,
            Int::class.javaPrimitiveType
        )
        extraFlagField.invoke(activity.window, darkModeFlag, darkModeFlag)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    try {
        val lp: WindowManager.LayoutParams = activity.getWindow().getAttributes()
        val darkFlag =
            WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
        val meizuFlags =
            WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
        darkFlag.isAccessible = true
        meizuFlags.isAccessible = true
        val bit = darkFlag.getInt(null)
        var value = meizuFlags.getInt(lp)
        value = value or bit
        meizuFlags.setInt(lp, value)
        activity.getWindow().setAttributes(lp)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}