package com.example.memogame.utils

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.fragment.app.FragmentActivity
import com.example.memogame.enams.DifficultyEnum

fun Int.toDifficulty():DifficultyEnum{
    return when(this){
        1->DifficultyEnum.EASY
        2->DifficultyEnum.MEDIUM
        else->DifficultyEnum.HARD
    }
}

fun Int.pxToDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun String.myLog()=Log.d("TTT",this)
fun Context.createDialog(@LayoutRes layoutRes:Int, @StyleRes styleRes:Int, cancelable:Boolean=false): Dialog {
    val dialog= Dialog(this,styleRes)
    dialog.setContentView(layoutRes)
    dialog.show()
    dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    dialog.setCancelable(cancelable)

    return dialog
}

fun FragmentActivity.statusBarTRANSPARENT()=this.apply{
    if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
    }
    if (Build.VERSION.SDK_INT >= 19) {
//            SYSTEM_UI_FLAG_LAYOUT_STABLE
        this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    if (Build.VERSION.SDK_INT >= 21) {
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        this.window.statusBarColor = Color.TRANSPARENT
    }
    this.window.statusBarColor = Color.TRANSPARENT
}

private fun FragmentActivity.setWindowFlag(bits: Int, on: Boolean) {
    val win = this.window
    val winParams = win.attributes
    if (on) {
        winParams.flags = winParams.flags or bits
    } else {
        winParams.flags = winParams.flags and bits.inv()
    }
    win.attributes = winParams
}

fun FragmentActivity.setStatusBar(view: View):Int{
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        // Получаем размер статус бара
        val statusBarHeight = resources.getDimensionPixelSize(resourceId)

        // Устанавливаем отступ сверху для корневого элемента макета
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,  // ширина в пикселях
            statusBarHeight // высота в пикселях
        )
        view.layoutParams = params
        return statusBarHeight
    }
    return 0
}
