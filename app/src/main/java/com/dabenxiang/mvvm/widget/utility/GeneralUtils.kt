package com.dabenxiang.mvvm.widget.utility

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.dabenxiang.mvvm.App
import java.text.SimpleDateFormat
import java.util.*

object GeneralUtils {

    private const val TIME_ZONE_OFFSET: Long = 1000 * 60 * 60 * 8

    fun hideKeyboard(activity: FragmentActivity) {
        val view = activity.currentFocus
        if (view != null) {
            val inputManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showKeyboard(context: Context, editText: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED)
    }

    fun readFromClipboard(): String {
        val clipboard = App.self.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (clipboard.hasPrimaryClip()) {
            val description = clipboard.primaryClipDescription
            val data = clipboard.primaryClip
            if (data != null && description != null && description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                return data.getItemAt(0).text.toString()
        }
        return ""
    }

    fun copyToClipboard(textToCopy: String) {
        val clipboard = App.self.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(null, textToCopy)
        clipboard.setPrimaryClip(clip)
    }

    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun generatedRandomId(): String {
        return UUID.randomUUID().toString()
    }

    fun Int.toDp(resources: Resources): Int {
        return (resources.displayMetrics.density * this).toInt()
    }

    fun convertUtcByDateFormat(utcTime: String, pattern: String): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        val date = utcToDate(utcTime)
        return sdf.format(date)
    }

    fun utcToDate(utcTime: String): Date {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        return Date(sdf.parse(utcTime).time + TIME_ZONE_OFFSET)
    }
}