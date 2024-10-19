package com.example.gallerygenius_v2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BuscarImg : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageList: MutableList<Image>
    private lateinit var adapter: ImageAdapter
    private lateinit var btnSearch: Button
    private lateinit var editTextSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_img)

        database = FirebaseDatabase.getInstance().getReference("images")

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        imageList = mutableListOf()
        adapter = ImageAdapter(imageList)
        recyclerView.adapter = adapter

        btnSearch = findViewById(R.id.btnSearch)
        editTextSearch = findViewById(R.id.editTextSearch)

        btnSearch.setOnClickListener {
            val keyword = editTextSearch.text.toString().trim()
            if (keyword.isNotEmpty()) {
                searchImages(keyword)
            } else {
                Toast.makeText(this, "Por favor ingrese un término de búsqueda", Toast.LENGTH_SHORT).show()
            }
        }

        loadImagesFromDatabase()

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


    private fun loadImagesFromDatabase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                imageList.clear()
                for (imageSnapshot in snapshot.children) {
                    val image = imageSnapshot.getValue(Image::class.java)
                    if (image != null) {
                        imageList.add(image)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BuscarImg, "Error al cargar imágenes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun searchImages(keyword: String) {
        val filteredImages = imageList.filter { image ->
            image.description.contains(keyword, ignoreCase = true)
        }
        adapter.updateList(filteredImages)
    }


}