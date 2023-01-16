package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.MapFragment
import com.example.myapplication.R
import com.example.myapplication.util.GenCart
import com.example.myapplication.util.GlobalBox

class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val chefName = view.findViewById<TextView>(R.id.chefName)
    private val foodName = view.findViewById<TextView>(R.id.foodName)
    private val toppingFood = view.findViewById<TextView>(R.id.toppingFood)
    private val imageUrl = view.findViewById<ImageView>(R.id.imageUrl)
    private val buyBtn = view.findViewById<Button>(R.id.buyBtn)
    private val locationBtn = view.findViewById<Button>(R.id.locationBtn)
    private val genCart = GenCart()
    private val context = view.context

    @SuppressLint("SetTextI18n")
    fun bind(foodViewItem: FoodOnlineViewItem) {
        Glide.with(context).load(foodViewItem.ImageUrl).into(imageUrl)
        chefName.text = foodViewItem.ChefName
        foodName.text = foodViewItem.FoodName
        toppingFood.text = "Topping: " + foodViewItem.Topping
        buyBtn.text = "฿ " + foodViewItem.Price.toString()

        buyBtn.setOnClickListener {
            genCart.addCart(foodViewItem.FoodName, foodViewItem.Price, foodViewItem.ImageUrl, context)
        }


       locationBtn.setOnClickListener {
           GlobalBox.latitude = foodViewItem.Latitude
           GlobalBox.longitude = foodViewItem.Longitude

           (context as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
               replace(R.id.fragment_cv, MapFragment())
                   .addToBackStack("HomeFragment")
                   .commit()
           }
       }


    }

}