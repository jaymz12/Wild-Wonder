package com.example.wild_wonders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class Signup : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var userRegisterBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        username = findViewById(R.id.userName)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirmPass)
        userRegisterBtn = findViewById(R.id.signup_btn)

        val imageViewBack = findViewById<ImageView>(R.id.back)

        imageViewBack.setOnClickListener {
            startActivity(
                Intent(
                    this@Signup,
                    MainActivity::class.java
                )
            )
        }

        userRegisterBtn.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val name = username.text.toString().trim { it <= ' ' }
        val pass = password.text.toString().trim { it <= ' ' }
        val cpassword = confirmPassword.text.toString().trim { it <= ' ' }

        if (name.isEmpty()) {
            username.setError("Can not be Empty")
            username.requestFocus()
            return
        }

        if (pass.isEmpty()) {
            password.error = "Password field cannot be empty"
            password.requestFocus()
            return
        }

        if (pass.length < 6) {
            password.error = "Password less than 6"
            password.requestFocus()
            return
        }
        if (cpassword != pass) {
            confirmPassword.error = "Password Do not Match"
            confirmPassword.requestFocus()
            return
        }
        if (name.isNotEmpty() && pass.isNotEmpty() && pass.length >= 6 && cpassword == pass){
            //move to login page
            Toast.makeText(this@Signup, "Registration Success", Toast.LENGTH_LONG).show()
            startActivity(
                Intent(this@Signup, Login::class.java).also {
                    it.putExtra("Username",name)
                    it.putExtra("Password",pass)
                    startActivity(it)
                    finish()
                }
            )
        }
    }
}