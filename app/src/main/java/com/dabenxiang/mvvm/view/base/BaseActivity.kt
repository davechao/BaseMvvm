package com.dabenxiang.mvvm.view.base

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.dabenxiang.mvvm.BuildConfig
import com.dabenxiang.mvvm.R

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        windowManager?.let {
            val metrics = DisplayMetrics()
            it.defaultDisplay.getMetrics(metrics)
            metrics.density = metrics.ydpi / 160
            metrics.densityDpi = metrics.ydpi.toInt()
            metrics.scaledDensity = metrics.density
            resources.configuration.densityDpi = metrics.densityDpi
            resources.configuration.fontScale = 1f
            baseContext.resources.updateConfiguration(resources.configuration, metrics)
        }

        setContentView(getLayoutId())
    }

    open fun getLayoutId(): Int {
        return -1
    }

    override fun getResources(): Resources {
        val overrideConfiguration = baseContext.resources.configuration
        if (overrideConfiguration.fontScale != 1f) {
            overrideConfiguration.fontScale = 1f
            val context = createConfigurationContext(overrideConfiguration)
            return context.resources
        }
        return super.getResources()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (newConfig.fontScale != 1f) {
            resources
        }
        super.onConfigurationChanged(newConfig)
    }

}