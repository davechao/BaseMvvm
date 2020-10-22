package com.dabenxiang.mvvm.view.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.dabenxiang.mvvm.R
import com.dabenxiang.mvvm.view.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_splash
    }
}