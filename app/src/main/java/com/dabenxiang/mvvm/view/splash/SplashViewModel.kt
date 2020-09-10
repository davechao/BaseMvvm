package com.dabenxiang.mvvm.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dabenxiang.mvvm.model.vo.CardItem
import com.dabenxiang.mvvm.view.base.BaseViewModel

class SplashViewModel : BaseViewModel() {

    private val _cardLiveData = MutableLiveData<CardItem>()
    val cardLiveData: LiveData<CardItem> = _cardLiveData

    fun getCardData() {
        val cardItem = CardItem(
            "A day wandering through the sandhills in Shark Fin Cove, and a few of the sights I saw",
            "Davenport, California",
            "December 2018"
        )
        _cardLiveData.value = cardItem
    }
}