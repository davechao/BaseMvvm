package com.dabenxiang.mvvm.view.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dabenxiang.mvvm.extension.handleException
import com.dabenxiang.mvvm.model.api.ExceptionResult
import com.dabenxiang.mvvm.view.main.MainViewModel
import com.dabenxiang.mvvm.widget.utility.GeneralUtils.showToast
import com.kaopiz.kprogresshud.KProgressHUD
import timber.log.Timber

abstract class BaseDialogFragment : DialogFragment() {

    open var mainViewModel: MainViewModel? = null
    var progressHUD: KProgressHUD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            mainViewModel = ViewModelProvider(it).get(MainViewModel::class.java)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressHUD = KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            if (isFullLayout()) {
                dialog!!.window!!.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
                )
                dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            } else {
                val widthPixels = (resources.displayMetrics.widthPixels * 0.8).toInt()
                dialog!!.window!!.setLayout(widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
                dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    abstract fun isFullLayout(): Boolean

    abstract fun getLayoutId(): Int

    fun onApiError(throwable: Throwable) {
        Timber.e("onApiError: $throwable")
        when (val errorHandler =
            throwable.handleException { ex -> mainViewModel?.processException(ex) }) {
            is ExceptionResult.HttpError -> {
                val errorItem = errorHandler.httpExceptionItem.errorItem

                val code = errorItem.code
                val message = errorItem.message
                val details = errorItem.details

                Timber.d("code: $code")
                Timber.d("message: $message")
                Timber.d("details: $details")

                showToast(requireContext(), "$message")
            }
            is ExceptionResult.Crash -> {
                showToast(requireContext(), "${errorHandler.throwable}")
            }
            is ExceptionResult.ApiError -> {
                val apiException = errorHandler.apiException
                val code = apiException.code
                val msg = apiException.msg

                Timber.d("code: $code")
                Timber.d("message: $msg")

                showToast(requireContext(), "$msg")
            }
        }
    }
}