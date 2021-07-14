package com.dev.abhinav.currencyconverter.helper

import com.dev.abhinav.currencyconverter.BuildConfig

class EndPoints {
    companion object {
        const val BASE_URL = "https://api.getgeoapi.com/api/v2/currency/"
        const val API_KEY = BuildConfig.API_KEY
        const val CONVERT_URL = "convert"
    }
}