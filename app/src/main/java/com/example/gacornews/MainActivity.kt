// MainActivity.kt
package com.example.gacornews

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var uploadButton: FloatingActionButton
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firestore
        db = FirebaseFirestore.getInstance()

        // Initialize UI components
        newsRecyclerView = findViewById(R.id.recyclerView)
        uploadButton = findViewById(R.id.fab_add_news)
        logoutButton = findViewById(R.id.logoutButton)

        // Setup RecyclerView
        newsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Load news articles (implement this function as needed)
        loadNewsArticles()

        // Set click listener for the upload button
        uploadButton.setOnClickListener {
            // Start the UploadActivity for adding news
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }

        // Set click listener for logout button
        logoutButton.setOnClickListener {
            // Handle logout (implement logout logic)
            Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadNewsArticles() {
        // Here you can load the news articles from Firestore and display them in the RecyclerView
        db.collection("news")
            .get()
            .addOnSuccessListener { documents ->
                // Process the documents and update the RecyclerView adapter
                val newsList = documents.map { document ->
                    document.toObject(News::class.java) // Assuming your document structure matches the News class
                }
                // Update your adapter with the newsList
                // newsRecyclerView.adapter = NewsAdapter(newsList)
            }
            .addOnFailureListener { e ->
                Log.w("MainActivity", "Error getting documents: ", e)
                Toast.makeText(this, "Failed to load news articles", Toast.LENGTH_SHORT).show()
            }
    }
}
