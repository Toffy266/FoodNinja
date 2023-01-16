package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R

class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val foodName = view.findViewById<TextView>(R.id.foodName)
    private val imageUrl = view.findViewById<ImageView>(R.id.foodImg)
    private val priceFood = view.findViewById<TextView>(R.id.priceFood)
    private val quantityFood = view.findViewById<TextView>(R.id.quantityFood)
    private val context = view.context

    @SuppressLint("SetTextI18n")
    fun bind(cartViewItem: CartOnlineViewItem) {
        Glide.with(context).load(cartViewItem.ImageUrl).into(imageUrl)
        foodName.text = cartViewItem.FoodName
        priceFood.text = "฿${cartViewItem.Price}"
        quantityFood.text = "x${cartViewItem.Quantity}"
    }
}