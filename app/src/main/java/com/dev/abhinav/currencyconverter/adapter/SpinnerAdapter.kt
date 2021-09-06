package com.dev.abhinav.currencyconverter.adapter

import android.annotation.SuppressLint
import android.content.Context
import com.dev.abhinav.currencyconverter.model.SpinnerItem
import android.widget.ArrayAdapter
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.dev.abhinav.currencyconverter.R
import android.widget.TextView

class SpinnerAdapter(context: Context?, resource: Int, list: List<SpinnerItem?>) : ArrayAdapter<Any?>(context!!, resource, list as List<Any?>) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val retView = LayoutInflater.from(context).inflate(R.layout.custom_spinner, parent, false)
        return initView(position, retView!!, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val retView = LayoutInflater.from(context).inflate(R.layout.custom_spinner, parent, false)
        return initView(position, retView!!, parent)
    }

    private fun initView(position: Int, convertView: View, parent: ViewGroup): View {
        val imageViewFlag = convertView.findViewById<ImageView>(R.id.flag_image)
        val textViewCountry = convertView.findViewById<TextView>(R.id.country_name)
        val textViewCode = convertView.findViewById<TextView>(R.id.country_code)
        val currentItem = getItem(position) as SpinnerItem?
        if (currentItem != null) {
            imageViewFlag.setImageResource(currentItem.flag)
            textViewCountry.text = currentItem.countryName
            textViewCode.text = currentItem.code
        }
        return convertView
    }
}