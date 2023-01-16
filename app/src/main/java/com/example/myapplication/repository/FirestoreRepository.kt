package com.example.myapplication.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class FirestoreRepository {
    private val firestoreDB: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun getSavedRecommendedFood(): CollectionReference {
        return firestoreDB.collection("RecommendedFood")
    }

    fun getSavedCart(): CollectionReference {
        return firestoreDB.collection("Cart")
    }
}