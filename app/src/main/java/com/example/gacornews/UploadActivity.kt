package com.example.gacornews

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class UploadActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var selectImageButton: Button
    private lateinit var selectedImageView: ImageView
    private lateinit var uploadButton: Button

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        titleEditText = findViewById(R.id.titleEditText)
        contentEditText = findViewById(R.id.contentEditText)
        selectImageButton = findViewById(R.id.selectImageButton)
        selectedImageView = findViewById(R.id.selectedImageView)
        uploadButton = findViewById(R.id.uploadButton)

        selectImageButton.setOnClickListener {
            openGallery()
        }

        uploadButton.setOnClickListener {
            uploadNews()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            selectedImageView.setImageURI(imageUri)
        }
    }

    private fun uploadNews() {
        val title = titleEditText.text.toString().trim()
        val content = contentEditText.text.toString().trim()

        if (title.isEmpty() || content.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Harap isi semua kolom dan pilih gambar", Toast.LENGTH_SHORT).show()
            return
        }

        val storageReference = FirebaseStorage.getInstance().reference.child("news_images/${UUID.randomUUID()}")

        imageUri?.let { uri ->
            storageReference.putFile(uri)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
                        // TODO: Save news data to Firebase Database (title, content, image URL)
                        Toast.makeText(this, "Berita berhasil diupload", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal mengupload gambar", Toast.LENGTH_SHORT).show()
                }
        }
    }

    companion object {
        private const val REQUEST_CODE_SELECT_IMAGE = 100
    }
}
