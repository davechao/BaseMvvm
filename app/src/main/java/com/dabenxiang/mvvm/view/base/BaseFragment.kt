package com.dabenxiang.mvvm.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kaopiz.kprogresshud.KProgressHUD
import com.dabenxiang.mvvm.extension.handleException
import com.dabenxiang.mvvm.model.api.ExceptionResult
import com.dabenxiang.mvvm.view.main.MainViewModel
import com.dabenxiang.mvvm.widget.utility.GeneralUtils.showToast
import timber.log.Timber

abstract class BaseFragment : Fragment() {

    open var mainViewModel: MainViewModel? = null
    var progressHUD: KProgressHUD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            mainViewModel = ViewModelProvider(it).get(MainViewModel::class.java)
        }
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

    abstract fun getLayoutId(): Int

    fun setupFinishAppListener() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finish()
        }
    }

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