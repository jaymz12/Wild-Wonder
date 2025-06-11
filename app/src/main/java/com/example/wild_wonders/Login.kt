package com.example.wild_wonders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class Login : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var userLoginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        userLoginBtn = findViewById(R.id.loginButton)

        val imageViewBack = findViewById<ImageView>(R.id.back)

        val regUsername = intent.getStringExtra("Username")
        val regPassword = intent.getStringExtra("Password")

        imageViewBack.setOnClickListener {
            startActivity(
                Intent(
                    this@Login,
                    MainActivity::class.java
                )
            )
        }

        userLoginBtn.setOnClickListener(){
            val user = username.text.toString()
            val password = password.text.toString()

            if (user == regUsername && password == regPassword){
                Toast.makeText(this@Login, "Login Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@Login, MapsActivity::class.java))
            }else {
                Toast.makeText(this@Login, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}