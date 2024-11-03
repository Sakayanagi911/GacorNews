package com.example.gacornews

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UploadActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var selectImageButton: Button
    private lateinit var uploadButton: Button
    private lateinit var selectedImageView: ImageView
    private lateinit var backButton: Button // New back button

    private lateinit var storage: FirebaseStorage
    private lateinit var firestore: FirebaseFirestore
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        titleEditText = findViewById(R.id.titleEditText)
        contentEditText = findViewById(R.id.contentEditText)
        selectImageButton = findViewById(R.id.selectImageButton)
        uploadButton = findViewById(R.id.uploadButton)
        selectedImageView = findViewById(R.id.selectedImageView)
        backButton = findViewById(R.id.backButton) // Initialize back button

        storage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()

        selectImageButton.setOnClickListener {
            selectImage()
        }

        uploadButton.setOnClickListener {
            uploadNewsArticle()
        }

        // Set click listener for back button
        backButton.setOnClickListener {
            onBackPressed() // Finish the current activity
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImageUri = uri
                    selectedImageView.setImageURI(uri)
                }
            }
        }

    private fun uploadNewsArticle() {
        val title = titleEditText.text.toString()
        val content = contentEditText.text.toString()

        if (title.isNotEmpty() && content.isNotEmpty() && selectedImageUri != null) {
            val storageRef: StorageReference = storage.reference.child("images/${System.currentTimeMillis()}.jpg")

            Log.d("UploadActivity", "Uploading image to: $storageRef")

            storageRef.putFile(selectedImageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    Log.d("UploadActivity", "Image uploaded successfully")
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val newsArticle = News(title, content, uri.toString())
                        uploadToFirestore(newsArticle)
                    }.addOnFailureListener { e ->
                        Log.e("UploadActivity", "Failed to get download URL", e)
                        Toast.makeText(this, "Failed to get image URL", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { e ->
                    Log.e("UploadActivity", "Failed to upload image", e)
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadToFirestore(newsArticle: News) {
        firestore.collection("news").add(newsArticle)
            .addOnSuccessListener {
                Log.d("UploadActivity", "News article uploaded successfully")
                Toast.makeText(this, "News article uploaded", Toast.LENGTH_SHORT).show()
                finish() // Close this activity after successful upload
            }.addOnFailureListener { e ->
                Log.e("UploadActivity", "Failed to upload news article", e)
                Toast.makeText(this, "Failed to upload news article", Toast.LENGTH_SHORT).show()
            }
    }
}
