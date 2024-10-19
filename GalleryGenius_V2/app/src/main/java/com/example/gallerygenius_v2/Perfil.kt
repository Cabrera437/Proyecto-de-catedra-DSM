package com.example.gallerygenius_v2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class Perfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val usernameTextView = findViewById<TextView>(R.id.emailTextView)
        val avatarImageView = findViewById<ImageView>(R.id.avatar)

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        // Configurar BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
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

        val userEmail = intent.getStringExtra("EMAIL")
        usernameTextView.text = userEmail ?: "" // Mostrar el correo o un valor por defecto
    }
}

