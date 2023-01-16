package com.example.myapplication.util

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.CartFragment
import com.example.myapplication.R
import com.example.myapplication.repository.FirestoreRepository

class GenCart {
    private val firebaseRepository = FirestoreRepository()

    fun addCart(foodName: String, price: Double, imageUrl: String, context: Context) {
        val data = HashMap<String,Any>()
        var quantity = 1.0
        var checkData = ""
        data["FoodName"] = foodName
        data["ImageUrl"] = imageUrl
        data["Price"] = price
        data["Quantity"] = quantity

        firebaseRepository.getSavedCart()
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (foodName == document.id) {
                        quantity += (document.data)["Quantity"] as Double
                        checkData = document.id
                    }
                }
                if (checkData != "") {
                    firebaseRepository.getSavedCart().document(foodName).update("Quantity", quantity)
                } else {
                    firebaseRepository.getSavedCart().document(foodName).set(data)
                }
                Toast.makeText(context,"Add to Cart ",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context,"Fail to add",Toast.LENGTH_SHORT).show()
            }
    }

    @SuppressLint("DetachAndAttachSameFragment")
    fun clearCart(context: Context?) {
        firebaseRepository.getSavedCart()
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    firebaseRepository.getSavedCart().document(document.id).delete()
                }

                Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
                (context as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_cv, CartFragment())
                        .detach(CartFragment())
                        .attach(CartFragment())
                        .commit()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context,"Fail to Checkout",Toast.LENGTH_SHORT).show()
            }
    }

}