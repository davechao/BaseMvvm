package com.dabenxiang.mvvm.widget.utility

import android.content.Context
import android.os.Environment
import android.os.StatFs
import timber.log.Timber
import java.io.File

object FileUtils {

    private const val TYPE_APK = "apk"

    fun getAvailableSpace(context: Context): Long {
        val fs = StatFs(getAppPath(context))
        return fs.availableBytes / 1024 / 1024
    }

    fun checkFileExists(context: Context, fileName: String?): Boolean {
        Timber.d("${FileUtils::class.simpleName}_checkFileExists_fileName: $fileName")
        return try {
            File(getFilePath(context, fileName)).exists()
        } catch (e: Exception) {
            false
        }
    }

    fun getFilePath(context: Context, fileName: String?): String {
        return context.getExternalFilesDir(TYPE_APK)?.absolutePath?.plus("/")
            .plus(fileName)
    }

    private fun getAppPath(context: Context): String {
        return when (Environment.MEDIA_MOUNTED) {
            Environment.getExternalStorageState() -> {
                Environment.getExternalStorageDirectory().toString()
            }
            else -> context.filesDir.toString()
        }
    }
}
