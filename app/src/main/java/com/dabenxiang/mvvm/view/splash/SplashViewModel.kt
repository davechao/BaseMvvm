package com.dabenxiang.mvvm.view.splash

import androidx.hilt.lifecycle.ViewModelInject
import com.apollographql.apollo.ApolloClient
import com.dabenxiang.mvvm.view.base.BaseViewModel

class SplashViewModel @ViewModelInject constructor(
    private var apolloClient: ApolloClient
) : BaseViewModel()