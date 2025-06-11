package com.example.wild_wonders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView

class AddObservation : AppCompatActivity() {
    private lateinit var addDetailsBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_observation)

        addDetailsBtn = findViewById(R.id.addBtn)

        addDetailsBtn.setOnClickListener {
            startActivity(
                Intent(
                    this@AddObservation,
                    AddDetails::class.java
                )
            )
        }


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.ic_sighting
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.ic_home -> {
                    // Launch the MapsActivity
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.ic_view -> {
                    // Launch the DashboardActivity
                    val intent = Intent(this, ListData::class.java)
                    startActivity(intent)
                    true
                }
                R.id.ic_sighting -> {
                    // Launch the NotificationsActivity
                    val intent = Intent(this, AddObservation::class.java)
                    startActivity(intent)
                    true
                }
                R.id.ic_settings -> {
                    // Launch the NotificationsActivity
                    val intent = Intent(this, Settings::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

    }
}