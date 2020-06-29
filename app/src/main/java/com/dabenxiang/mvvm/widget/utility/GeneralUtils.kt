package com.dabenxiang.mvvm.widget.utility

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageInstaller
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.ImageUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.dabenxiang.mvvm.App
import com.dabenxiang.mvvm.BuildConfig
import com.dabenxiang.mvvm.PACKAGE_INSTALLED_ACTION
import com.dabenxiang.mvvm.TYPE_APK
import com.dabenxiang.mvvm.model.api.ApiRepository
import com.dabenxiang.mvvm.model.api.vo.error.ErrorItem
import com.dabenxiang.mvvm.model.api.vo.error.HttpExceptionItem
import com.dabenxiang.mvvm.model.pref.Pref
import com.dabenxiang.mvvm.view.main.MainActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object GeneralUtils : KoinComponent {

    private val pref: Pref by inject()

    private const val TIME_ZONE_OFFSET: Long = 1000 * 60 * 60 * 8  //UTC - 8 hours
    private const val MIN_CLICK_DELAY_TIME = 1000
    private var lastClickTime = 0L

    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

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

    fun getEllipsizeString(text: String, startIndex: Int = 2, endIndex: Int = 2): String {
        Timber.i("Disable ellipsize :${pref.disableEllipsize}")
        val disableEllipsize = pref.disableEllipsize
        if (disableEllipsize) return text

        var ellipsize = ""

        if (text.length == 2) {
            ellipsize = "${text[0]}*"
        } else {
            text.forEachIndexed { index, char ->
                ellipsize += if (index < startIndex || index >= text.length - endIndex) {
                    char.toString()
                } else {
                    "*"
                }
            }
        }
        return ellipsize
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val bytes = ImageUtils.bitmap2Bytes(bitmap, Bitmap.CompressFormat.PNG)
        return EncodeUtils.base64Encode2String(bytes)
    }

    fun Int.toDp(resources: Resources): Int {
        return (resources.displayMetrics.density * this).toInt()
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

    fun generatedRandomId(): String {
        return UUID.randomUUID().toString()
    }

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

    fun convertUtcByDateFormat(utcTime: String, pattern: String): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        val date = utcToDate(utcTime)
        return sdf.format(date)
    }

    fun utcToDate(utcTime: String): Date {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        return Date(sdf.parse(utcTime).time + TIME_ZONE_OFFSET)
    }

    fun isFastClick(clickDelay: Int = MIN_CLICK_DELAY_TIME): Boolean {
        val curClickTime = System.currentTimeMillis()
        var flag = false
        if ((curClickTime - lastClickTime) <= clickDelay) {
            flag = true
        } else {
            lastClickTime = curClickTime
        }
        return flag
    }

    fun getExceptionDetail(t: Throwable): String {
        return when (t) {
            is HttpException -> {
                val data = getHttpExceptionData(t)
                "$data, ${t.localizedMessage}"
            }
            else -> getStackTrace(t)
        }
    }

    fun getHttpExceptionData(httpException: HttpException): HttpExceptionItem {
        val oriResponse = httpException.response()

        val url = oriResponse?.raw()?.request?.url.toString()
        Timber.d("url: $url")

        val errorBody = oriResponse?.errorBody()
        val jsonStr = errorBody?.string()
        val type = object : TypeToken<ErrorItem>() {}.type

        val errorItem: ErrorItem = try {
            Gson().fromJson(jsonStr, type)
        } catch (e: Exception) {
            e.printStackTrace()
            ErrorItem(
                null,
                null,
                null
            )
        }
        Timber.d("errorItem: $errorItem")

        val responseBody = Gson().toJson(
            ErrorItem(
                errorItem.code,
                errorItem.message,
                null
            )
        )
            .toResponseBody(ApiRepository.MEDIA_TYPE_JSON.toMediaTypeOrNull())

        val rawResponse = okhttp3.Response.Builder()
            .code(httpException.code())
            .message(httpException.message())
            .protocol(Protocol.HTTP_1_1)
            .request(Request.Builder().url(url).build())
            .build()

        val response = Response.error<ErrorItem>(responseBody, rawResponse)

        val httpExceptionClone = HttpException(response)
        return HttpExceptionItem(
            errorItem,
            httpExceptionClone,
            url
        )
    }

    private fun getStackTrace(t: Throwable): String {
        val sw = StringWriter(256)
        val pw = PrintWriter(sw, false)
        t.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }

    fun installApk(context: Context, path: String) {
        var session: PackageInstaller.Session?
        try {
            val packageInstaller = context!!.packageManager.packageInstaller
            var sessionParams =
                PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
            var sessionId = packageInstaller.createSession(sessionParams)
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

    @Suppress(
        "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
        "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
    )
    fun deleteExternalFile(context: Context) {
        File(context.getExternalFilesDir(TYPE_APK)?.absolutePath).let {
            while (it.listFiles().iterator().hasNext()) {
                it.listFiles().iterator().next().delete()
            }
        }
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
}