package com.example.gallerygenius_v2

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageList: MutableList<Image>
    private lateinit var adapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        // Verificar si el usuario está autenticado
        if (auth.currentUser == null) {
            // Si no hay usuario autenticado, redirigir a la página de inicio de sesión
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish() // Termina MainActivity para que no vuelva atrás
            return // Salir del método onCreate
        }

        database = FirebaseDatabase.getInstance().getReference("images")

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        imageList = mutableListOf()
        adapter = ImageAdapter(imageList)
        recyclerView.adapter = adapter

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
                    // Navegar a la pantalla del perfil y pasar el correo electrónico
                    val userEmail = auth.currentUser?.email // Obtener el correo
                    val intent = Intent(this, Perfil::class.java).apply {
                        putExtra("EMAIL", userEmail) // Pasar el correo al perfil
                    }
                    startActivity(intent)
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
                Toast.makeText(this@MainActivity, "Error al cargar imágenes", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
