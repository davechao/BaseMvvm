package com.dabenxiang.mvvm.view.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.dabenxiang.mvvm.R
import com.dabenxiang.mvvm.model.api.ApiResult.*
import com.dabenxiang.mvvm.view.base.BaseFragment
import timber.log.Timber

class SplashFragment : BaseFragment() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.postResult.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> Timber.d(it.result?.post?.title)
                is Error -> onApiError(it.throwable)
                else -> {
                }
            }
        })

        viewModel.getPostById("1")
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_splash
    }
}