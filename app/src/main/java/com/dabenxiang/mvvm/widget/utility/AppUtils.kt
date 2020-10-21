package com.dabenxiang.mvvm.widget.utility

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.Build
import android.provider.Settings
import com.dabenxiang.mvvm.App
import com.dabenxiang.mvvm.BuildConfig
import com.dabenxiang.mvvm.PACKAGE_INSTALLED_ACTION
import com.dabenxiang.mvvm.view.main.MainActivity
import timber.log.Timber
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

object AppUtils {

    @SuppressLint("HardwareIds")
    fun getAndroidID(): String {
        return Settings.Secure.getString(
            App.applicationContext().contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun getVersionName(): String {
        return BuildConfig.VERSION_NAME
    }

    fun getDeviceName(): String {
        return Build.MODEL
    }

    fun getSystemProperty(key: String, defaultValue: String): String {
        var value = defaultValue
        try {
            value = Class.forName("android.os.SystemProperties")
                .getMethod("get", String::class.java)
                .invoke(null, key) as String
            if (value == "") {
                value = defaultValue
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return value
    }

    fun getAppVersion(context: Context): Long {
        val pkgManager = context.packageManager
        val pkgName = context.packageName
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
                pkgManager.getPackageInfo(pkgName, 0).longVersionCode
            }
            else -> {
                pkgManager.getPackageInfo(pkgName, 0).versionCode.toLong()
            }
        }
    }

    fun getMiUiVersion(): String? {
        return getSystemProperty("ro.miui.ui.version.name")
    }

    fun getSystemProperty(propName: String): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        Timber.d("getSystemProperty: $line")
        return line
    }

    fun installApk(context: Context, path: String) {
        val session: PackageInstaller.Session?
        try {
            val packageInstaller = context!!.packageManager.packageInstaller
            val sessionParams =
                PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
            val sessionId = packageInstaller.createSession(sessionParams)
            session = packageInstaller.openSession(sessionId)

            addApkToInstallSession(path, session)

            val intent = Intent(context, MainActivity::class.java)
            intent.action = PACKAGE_INSTALLED_ACTION

            val pendingIntent = PendingIntent.getActivity(context, 1010, intent, 0)
            session.commit(pendingIntent.intentSender)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun addApkToInstallSession(apkName: String, session: PackageInstaller.Session) {
        val packageInSession = session.openWrite("package", 0, -1)
        val buffer = ByteArray(65535)
        File(apkName).inputStream().let {
            it.buffered().let { input ->
                while (true) {
                    val sz = input.read(buffer)
                    if (sz <= 0) break
                    packageInSession.write(buffer, 0, sz)
                }
                input.close()
            }
            it.close()
            packageInSession.close()
        }
    }

}