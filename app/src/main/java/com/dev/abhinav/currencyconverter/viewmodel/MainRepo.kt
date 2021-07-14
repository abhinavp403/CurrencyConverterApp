package com.dev.abhinav.currencyconverter.viewmodel

import com.dev.abhinav.currencyconverter.helper.Resource
import com.dev.abhinav.currencyconverter.model.ApiResponse
import com.dev.abhinav.currencyconverter.network.ApiDataSource
import com.dev.abhinav.currencyconverter.network.BaseDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepo
@Inject constructor(private val apiDataSource: ApiDataSource) : BaseDataSource() {

    suspend fun getConvertedData(access_key: String, from: String, to: String, amount: Double): Flow<Resource<ApiResponse>> {
        return flow {
            emit(safeApiCall { apiDataSource.getConvertedRate(access_key, from, to, amount) })
        }.flowOn(Dispatchers.IO)
    }

}