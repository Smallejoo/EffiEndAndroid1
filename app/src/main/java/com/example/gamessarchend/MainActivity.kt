package com.example.gamessarchend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private val TAG = "FirestoreData" // Consistent tag for logging

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = FirebaseFirestore.getInstance()

        // Reference to your 'games' collection
        db.collection("Games")
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Log.d(TAG, "No data found")
                    return@addOnSuccessListener
                }
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    // Example of accessing specific fields
                    val title = document.getString("Title")
                    val developer = document.getString("Developer")
                    Log.d(TAG, "Game title: $title, Developer: $developer")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }
}
