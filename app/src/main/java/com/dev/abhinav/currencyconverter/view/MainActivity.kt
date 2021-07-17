package com.dev.abhinav.currencyconverter.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dev.abhinav.currencyconverter.databinding.ActivityMainBinding
import com.dev.abhinav.currencyconverter.helper.EndPoints
import com.dev.abhinav.currencyconverter.helper.Resource
import com.dev.abhinav.currencyconverter.helper.Utility
import com.dev.abhinav.currencyconverter.model.Rates
import com.dev.abhinav.currencyconverter.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var selectedItem1: String? = "AFN"
    private var selectedItem2: String? = "AFN"
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initSpinner()
        setUpClickListener()
    }

    private fun initSpinner() {
        val spinner1 = binding.spnFirstCountry
        spinner1.setItems( getAllCountries() )

        //hide key board when spinner shows
        spinner1.setOnClickListener {
            Utility.hideKeyboard(this)
        }
        spinner1.setOnItemSelectedListener { _, _, _, item ->
            val countryCode = getCountryCode(item.toString())
            val currencySymbol = getSymbol(countryCode)
            selectedItem1 = currencySymbol
            binding.txtFirstCurrencyName.text = selectedItem1
        }

        val spinner2 = binding.spnSecondCountry
        spinner2.setItems( getAllCountries() )
        //hide key board when spinner shows
        spinner2.setOnClickListener {
            Utility.hideKeyboard(this)
        }
        spinner2.setOnItemSelectedListener { _, _, _, item ->
            val countryCode = getCountryCode(item.toString())
            val currencySymbol = getSymbol(countryCode)
            selectedItem2 = currencySymbol
            binding.txtSecondCurrencyName.text = selectedItem2
        }
    }

    //private fun getCountryCode(countryName: String) = Locale.getISOCountries().find { Locale("", it).displayCountry == countryName }
    private fun getCountryCode(countryName: String): String {
        return Locale.getISOCountries().find { Locale("", it).displayCountry == countryName }.toString()
    }

    private fun getSymbol(countryCode: String?): String {
        val availableLocales = Locale.getAvailableLocales()
        for (i in availableLocales.indices) {
            if (availableLocales[i].country == countryCode) {
                return Currency.getInstance(availableLocales[i]).currencyCode
            }
        }
        return ""
    }

    private fun getAllCountries(): ArrayList<String> {
        val locales = Locale.getAvailableLocales()
        val countries = ArrayList<String>()
        for (locale in locales) {
            val country = locale.displayCountry
            if (country.trim { it <= ' ' }.isNotEmpty() && !countries.contains(country)) {
                countries.add(country)
            }
        }
        countries.sort()
        return countries
    }

    private fun setUpClickListener() {
        binding.btnConvert.setOnClickListener {
            val numberToConvert = binding.etFirstCurrency.text.toString()
            if (numberToConvert.isEmpty() || numberToConvert == "0") {
                Toast.makeText(applicationContext, "Input a value in the first text field, result will be shown in the second text field", Toast.LENGTH_LONG).show()
            } else if (!Utility.isNetworkAvailable(this)) {
                Toast.makeText(applicationContext, "You are not connected to the internet", Toast.LENGTH_LONG).show()
            } else {
                doConversion()
            }
        }
    }

    private fun doConversion() {
        //hide keyboard
        Utility.hideKeyboard(this)

        //make progress bar visible
        binding.prgLoading.visibility = View.VISIBLE

        //make button invisible
        binding.btnConvert.visibility = View.GONE

        //Get the data inputed
        val apiKey = EndPoints.API_KEY
        val from = selectedItem1.toString()
        val to = selectedItem2.toString()
        val amount = binding.etFirstCurrency.text.toString().toDouble()

        //do the conversion
        mainViewModel.getConvertedData(apiKey, from, to, amount)

        //observe for changes in UI
        observeUi()
    }

    private fun observeUi() {
        mainViewModel.data.observe(this, androidx.lifecycle.Observer {result ->
            when(result.status){
                Resource.Status.SUCCESS -> {
                    if (result.data?.status == "success"){
                        val map: Map<String, Rates>
                        map = result.data.rates
                        map.keys.forEach {
                            val rateForAmount = map[it]?.rate_for_amount
                            mainViewModel.convertedRate.value = rateForAmount
                            val formattedString = String.format("%,.2f", mainViewModel.convertedRate.value)
                            binding.etSecondCurrency.setText(formattedString)
                        }
                        binding.prgLoading.visibility = View.GONE
                        binding.btnConvert.visibility = View.VISIBLE
                    }
                    else if(result.data?.status == "fail"){
                        Toast.makeText(applicationContext,"Ooops! something went wrong, Try again", Toast.LENGTH_LONG).show()
                        binding.prgLoading.visibility = View.GONE
                        binding.btnConvert.visibility = View.VISIBLE
                    }
                }
                Resource.Status.ERROR -> {
                    Toast.makeText(applicationContext,  "Oopps! Something went wrong, Try again", Toast.LENGTH_LONG).show()
                    binding.prgLoading.visibility = View.GONE
                    binding.btnConvert.visibility = View.VISIBLE
                }
                Resource.Status.LOADING -> {
                    binding.prgLoading.visibility = View.VISIBLE
                    binding.btnConvert.visibility = View.GONE
                }
            }
        })
    }
}
