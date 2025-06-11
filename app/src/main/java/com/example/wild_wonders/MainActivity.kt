package com.example.wild_wonders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var userRegisterBtn: Button
    private lateinit var userLoginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        userRegisterBtn = findViewById(R.id.signup)
        userLoginBtn = findViewById(R.id.login)

        userRegisterBtn.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    Signup::class.java
                )
            )
        }
        userLoginBtn.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    Login::class.java
                )
            )
        }
    }


}