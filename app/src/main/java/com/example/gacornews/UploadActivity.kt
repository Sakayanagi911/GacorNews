package com.example.gacornews

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UploadActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var selectedImageView: ImageView
    private lateinit var uploadButton: Button
    private lateinit var backButton: Button
    private lateinit var selectImageButton: Button
    private var imageUri: Uri? = null
    private val db = FirebaseFirestore.getInstance()
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        titleEditText = findViewById(R.id.titleEditText)
        contentEditText = findViewById(R.id.contentEditText)
        selectedImageView = findViewById(R.id.selectedImageView)
        uploadButton = findViewById(R.id.uploadButton)
        backButton = findViewById(R.id.backButton)
        selectImageButton = findViewById(R.id.selectImageButton)

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        backButton.setOnClickListener {
            finish() // Kembali ke activity sebelumnya
        }

        uploadButton.setOnClickListener {
            uploadNews()
        }
    }

    private fun uploadNews() {
        val title = titleEditText.text.toString().trim()
        val content = contentEditText.text.toString().trim()

        if (imageUri != null && title.isNotEmpty() && content.isNotEmpty()) {
            val fileReference = storageReference.child("news_images/${System.currentTimeMillis()}.png")
            fileReference.putFile(imageUri!!).addOnSuccessListener {
                fileReference.downloadUrl.addOnSuccessListener { uri ->
                    val news = News(title, content, uri.toString())
                    db.collection("news").add(news)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Berita berhasil di-upload", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Gagal mengupload berita: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Gagal mengupload gambar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            selectedImageView.setImageURI(imageUri)
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
