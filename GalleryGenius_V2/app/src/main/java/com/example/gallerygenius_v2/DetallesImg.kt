package com.example.gallerygenius_v2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DetallesImg : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var downloadButton: Button
    private lateinit var imageNameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_img)

        imageView = findViewById(R.id.imageView)
        downloadButton = findViewById(R.id.downloadButton)
        imageNameTextView = findViewById(R.id.imageNameTextView)

        // Obtener la URL de la imagen del intent
        val imageUrl = intent.getStringExtra("IMAGE_URL")
        val imageName = intent.getStringExtra("IMAGE_DESCRIPTION")

        // Cargar la imagen usando Glide
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).into(imageView)

            // Mostrar el nombre de la imagen
            imageNameTextView.text = imageName ?: "Descripcion de imagen desconocido" // Mostrar nombre o un mensaje por defecto

            // Configurar el botón de descarga
            downloadButton.setOnClickListener {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_WRITE_EXTERNAL_STORAGE)
                } else {
                    downloadImage(imageUrl)
                }
            }
        }

        // Configurar BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.search -> {
                    startActivity(Intent(this, BuscarImg::class.java))
                    true
                }
                R.id.add -> {
                    startActivity(Intent(this, Proximamente::class.java))
                    true
                }
                R.id.bookmark -> {
                    startActivity(Intent(this, Proximamente::class.java))
                    true
                }
                R.id.profile -> {
                    startActivity(Intent(this, Perfil::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun downloadImage(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    saveImageToDownloads(resource)
                }
            })
    }

    private fun saveImageToDownloads(bitmap: Bitmap) {
        // Obtener la carpeta de Descargas
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = "downloaded_image_${System.currentTimeMillis()}.jpg" // Cambia el nombre del archivo si es necesario
        val file = File(downloadsDir, fileName)

        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            Toast.makeText(this, "Imagen guardada en: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Toast.makeText(this, "Error al guardar la imagen: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1001
    }
}
