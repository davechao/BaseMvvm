package com.dabenxiang.mvvm.widget.utility

import android.graphics.Bitmap
import android.util.LruCache

object LruCacheUtils {

    private var lruCache: LruCache<String, Bitmap>

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        lruCache = LruCache(cacheSize)
    }

    fun putLruCache(key: String, bitmap: Bitmap) {
        lruCache.put(key, bitmap)
    }

    fun getLruCache(key: String): Bitmap? {
        return lruCache.get(key)
    }

}