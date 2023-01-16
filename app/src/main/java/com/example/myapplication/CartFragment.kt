package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.CartAdapter
import com.example.myapplication.adapter.CartOnlineViewItem
import com.example.myapplication.adapter.CartViewItem
import com.example.myapplication.databinding.FragmentCartBinding
import com.example.myapplication.repository.FirestoreRepository
import com.example.myapplication.util.GenCart
import com.example.myapplication.util.getLoading
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class CartFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var cartRecycleView: RecyclerView
    private lateinit var cartImageList: MutableList<CartViewItem>
    private var cartImageOnlineList: MutableList<CartOnlineViewItem> = mutableListOf<CartOnlineViewItem>()
    private var firebaseRepository = FirestoreRepository()
    private var genCart = GenCart()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_cart, container, false)
        return FragmentCartBinding.inflate(layoutInflater).root
    }

    @SuppressLint("SetTextI18n", "DetachAndAttachSameFragment")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        // get user login email
        mAuth.currentUser?.email
        // get user login uid
        mAuth.currentUser?.uid

        cartRecycleView = view.findViewById<RecyclerView>(R.id.recycleView);
        cartImageList = mutableListOf<CartViewItem>()
        fetchCartDataFromDatabase()

        // Sum Price
        var sumPrice = 0.0
        val checkoutBtn = view.findViewById<Button>(R.id.checkoutBtn)

        firebaseRepository.getSavedCart()
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    sumPrice += (document.data)["Price"] as Double * (document.data)["Quantity"] as Double
                }
                checkoutBtn.text = "CHECK OUT (฿$sumPrice)"

                if(sumPrice != 0.0) {
                    checkoutBtn.setOnClickListener {
                        genCart.clearCart(context)
                        loadingSuccess()
                    }
                }

            }
            .addOnFailureListener { e -> Log.w(TAG, "Error getting documents", e) }

    }

    private fun fetchCartDataFromDatabase() {
        firebaseRepository.getSavedCart().get().addOnSuccessListener { documents ->
            cartImageOnlineList.clear()
            for (document in documents) {
                cartImageOnlineList.add(document.toObject(CartOnlineViewItem::class.java))
            }
            cartRecycleView.adapter = CartAdapter(cartImageOnlineList)
        }
    }

    private fun loadingSuccess() {
        val dialog = getLoading()
        dialog.show()

        val handler = Handler()
        val runnable = Runnable {
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }

        dialog.setOnDismissListener(DialogInterface.OnDismissListener {
            handler.removeCallbacks(
                runnable
            )
        })

        handler.postDelayed(runnable, 2000)
    }

}