# Gacor News App

Gacor News adalah aplikasi (BELUM JADI) berita sederhana yang memungkinkan pengguna untuk melihat, menambahkan, mengedit, dan menghapus berita dengan gambar. Aplikasi ini menggunakan Firebase Firestore untuk penyimpanan data dan Firebase Storage untuk menyimpan gambar.

## Fitur

- **Menampilkan Daftar Berita**: Pengguna dapat melihat daftar berita yang diambil dari Firestore.
- **Menambahkan Berita**: Pengguna dapat menambahkan berita baru lengkap dengan gambar.
- **Menghapus Berita**: Pengguna dapat menghapus berita yang tidak diinginkan.
- **Logout**: Pengguna dapat keluar dari aplikasi.

## Teknologi yang Digunakan

- Android SDK
- Kotlin
- Firebase Firestore
- Firebase Storage
- Glide (untuk pemuatan gambar)

## Persyaratan

- Android Studio
- Akun Firebase untuk mengatur Firestore dan Storage

## Instalasi

1. **Clone repository** ini ke mesin lokal Anda:

   ```bash
   git clone https://github.com/username/gacor-news.git

## Instalasi

1. **Buka project di Android Studio.**

2. **Konfigurasi Firebase:**
   - Buat project baru di Firebase Console.
   - Tambahkan aplikasi Android Anda di project Firebase.
   - Salin file `google-services.json` ke direktori `app/` dalam project Anda.

3. **Tambahkan dependensi Firebase di file `build.gradle` (Module: app):**

   ```kotlin
   implementation("com.google.firebase:firebase-firestore-ktx:24.0.0")
   implementation("com.google.firebase:firebase-storage-ktx:24.0.0")
   implementation("com.google.firebase:firebase-auth-ktx:24.0.0")
   implementation("com.github.bumptech.glide:glide:4.12.0")

4. **Sinkronkan project Anda.

## Cara Menggunakan

1. Jalankan aplikasi di emulator atau perangkat Android.
2. Login menggunakan akun Firebase (atau buat sistem login sesuai kebutuhan).
3. Lihat berita yang ditampilkan.
4. Tambah berita baru dengan menekan tombol '+'.
5. Edit atau hapus berita yang ada dengan opsi yang tersedia.

