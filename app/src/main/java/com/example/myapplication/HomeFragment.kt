package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.repository.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import com.example.myapplication.adapter.*
import com.example.myapplication.util.GlobalBox
import com.example.myapplication.util.getLoading
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var foodPager: ViewPager2
    private lateinit var foodImageList: MutableList<FoodViewItem>
    private var foodImageOnlineList: MutableList<FoodOnlineViewItem> = mutableListOf<FoodOnlineViewItem>()
    private var firebaseRepository = FirestoreRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_home, container, false)
        return FragmentHomeBinding.inflate(layoutInflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        // get user login email
        mAuth.currentUser?.email
        // get user login uid
        mAuth.currentUser?.uid

        // food recommended
        foodPager = view.findViewById<ViewPager2>(R.id.foodViewPager2)
        foodImageList = mutableListOf<FoodViewItem>()
        fetchFoodDataFromDatabase()

        //search event
        val etSearchInput = view.findViewById<EditText>(R.id.etSearchInput)
        etSearchInput.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (etSearchInput.text.isNotEmpty()) {
                    val filteredItem = foodImageOnlineList.filter {
                        it.ChefName.lowercase(Locale.getDefault()).contains(etSearchInput.text.toString()
                            .lowercase(Locale.getDefault())) or
                        it.FoodName.lowercase(Locale.getDefault()).contains(etSearchInput.text.toString()
                            .lowercase(Locale.getDefault()))
                    }
                    foodPager.adapter = FoodAdapter(filteredItem)
                } else {
                    foodPager.adapter = FoodAdapter(foodImageOnlineList)
                }
            }
        })

    }

    private fun fetchFoodDataFromDatabase() {
        firebaseRepository.getSavedRecommendedFood().get().addOnSuccessListener { documents ->
            foodImageOnlineList.clear()
            for (document in documents) {
                foodImageOnlineList.add(document.toObject(FoodOnlineViewItem::class.java))
            }
            foodPager.adapter = FoodAdapter(foodImageOnlineList)
        }
    }

}