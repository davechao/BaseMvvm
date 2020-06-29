package com.dabenxiang.mvvm.model.pref

import com.google.gson.Gson

class Pref(private val gson: Gson, preferenceFileName: String, isDebug: Boolean) :
    AbstractPref(preferenceFileName, isDebug) {

    private val ellipsizeKeyPref = BooleanPref("ELLIPSIZE_KEY")
    private val recordTimestampPref = LongPref("RECORD_TIMESTAMP")
    private val downloadIdPref = LongPref("DOWNLOAD_ID")

    var disableEllipsize: Boolean
        get() = ellipsizeKeyPref.get()
        set(value) = ellipsizeKeyPref.set(value)

    var recordTimestamp: Long
        get() = recordTimestampPref.get()
        set(value) = recordTimestampPref.set(value)

    var downloadId: Long
        get() = downloadIdPref.get()
        set(value) = downloadIdPref.set(value)
}
