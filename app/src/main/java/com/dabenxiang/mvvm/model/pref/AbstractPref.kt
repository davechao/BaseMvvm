package com.dabenxiang.mvvm.model.pref

import android.content.Context
import android.content.SharedPreferences
import com.dabenxiang.mvvm.App
import com.dabenxiang.mvvm.widget.utility.CryptUtils

abstract class AbstractPref protected constructor(preferenceFileName: String, isDebug: Boolean) {

    var prefs: SharedPreferences = when {
        isDebug -> {
            App.self
                .getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE)
        }
        else -> {
            CryptUtils.getEncryptedPref(App.self, preferenceFileName)
        }
    }

    open inner class BooleanPref @JvmOverloads constructor(
        private val key: String,
        private val defaultValue: Boolean = false
    ) {
        fun get(): Boolean {
            return prefs.getBoolean(key, defaultValue)
        }

        fun set(value: Boolean) {
            val editor = prefs.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        fun remove() {
            val editor = prefs.edit()
            editor.remove(key)
            editor.apply()
        }
    }

    inner class IntPref @JvmOverloads constructor(
        private val key: String,
        private val defaultValue: Int = 0
    ) {
        fun get(): Int {
            return prefs.getInt(key, defaultValue)
        }

        fun set(value: Int) {
            val editor = prefs.edit()
            editor.putInt(key, value)
            editor.apply()
        }

        fun remove() {
            val editor = prefs.edit()
            editor.remove(key)
            editor.apply()
        }
    }

    inner class FloatPref @JvmOverloads constructor(
        private val key: String,
        private val defaultValue: Float = 0f
    ) {
        fun get(): Float {
            return prefs.getFloat(key, defaultValue)
        }

        fun set(value: Float) {
            val editor = prefs.edit()
            editor.putFloat(key, value)
            editor.apply()
        }

        fun remove() {
            val editor = prefs.edit()
            editor.remove(key)
            editor.apply()
        }
    }

    inner class LongPref(private val key: String) {
        fun get(): Long {
            return prefs.getLong(key, 0)
        }

        fun set(value: Long) {
            val editor = prefs.edit()
            editor.putLong(key, value)
            editor.apply()
        }

        fun remove() {
            val editor = prefs.edit()
            editor.remove(key)
            editor.apply()
        }
    }

    inner class StringPref @JvmOverloads constructor(
        private val key: String,
        private val defaultValue: String = ""
    ) {
        fun get(): String? {
            return prefs.getString(key, defaultValue)
        }

        fun set(value: String) {
            val editor = prefs.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun remove() {
            val editor = prefs.edit()
            editor.remove(key)
            editor.apply()
        }
    }
}
