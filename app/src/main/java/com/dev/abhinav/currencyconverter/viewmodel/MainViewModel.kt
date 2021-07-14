package com.dev.abhinav.currencyconverter.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.abhinav.currencyconverter.helper.Resource
import com.dev.abhinav.currencyconverter.helper.SingleLiveEvent
import com.dev.abhinav.currencyconverter.model.ApiResponse
import com.dev.abhinav.currencyconverter.model.Rates
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel
@ViewModelInject constructor(private val mainRepo: MainRepo) : ViewModel() {
    private val _data = SingleLiveEvent<Resource<ApiResponse>>()

    val data = _data
    val convertedRate = MutableLiveData<Double>()

    fun getConvertedData(access_key: String, from: String, to: String, amount: Double) {
        viewModelScope.launch {
            mainRepo.getConvertedData(access_key, from, to, amount).collect {
                data.value = it
            }
        }
    }
}