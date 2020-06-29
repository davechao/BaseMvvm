package com.dabenxiang.mvvm.widget.log

import timber.log.Timber

class DebugLogTree : Timber.DebugTree() {

    private val name = "MVVM"
    private val formatMsg = "%s: %s"

    /**
     * Log format: (ClassName:lineNumber): [Thread Name] message
     */
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val logFormatMsg = StringBuilder("[")
            .append(Thread.currentThread().name)
            .append(" Thread")
            .append("] ")
            .append(message)
            .toString()

        val msg = String.format(formatMsg, tag, logFormatMsg)

        super.log(priority, name, msg, t)
    }

    override fun createStackElementTag(element: StackTraceElement): String? {
        return "(${element.fileName}:${element.lineNumber})"
    }
}
