package com.example.gacornews

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var logoutButton: Button
    private lateinit var fabAddNews: FloatingActionButton
    private val newsList = mutableListOf<News>()
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var newsListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        newsAdapter = NewsAdapter(newsList) { news -> deleteNews(news) }
        recyclerView.adapter = newsAdapter

        logoutButton = findViewById(R.id.logoutButton)
        fabAddNews = findViewById(R.id.fab_add_news)

        logoutButton.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Berhasil logout", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        fabAddNews.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }

        observeNewsUpdates()
    }

    private fun observeNewsUpdates() {
        newsListener = db.collection("news").addSnapshotListener { snapshots, e ->
            if (e != null) {
                Toast.makeText(this, "Gagal memuat berita: ${e.message}", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if (snapshots != null) {
                newsList.clear()
                for (document in snapshots) {
                    val news = document.toObject(News::class.java).copy(id = document.id)
                    newsList.add(news)
                }
                newsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun deleteNews(news: News) {
        db.collection("news").document(news.id).delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Berita berhasil dihapus", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal menghapus berita: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        newsListener?.remove()
    }
}
