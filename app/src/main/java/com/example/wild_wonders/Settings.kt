package com.example.wild_wonders

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.android.material.bottomnavigation.BottomNavigationView

class Settings : AppCompatActivity() {

    private lateinit var unitSystemRadioGroup: RadioGroup
    private lateinit var distanceEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        unitSystemRadioGroup = findViewById(R.id.unitSystemRadioGroup)
        distanceEditText = findViewById(R.id.distanceEditText)
        saveButton = findViewById(R.id.saveButton)
        sharedPreferences = getSharedPreferences("UserSettings", MODE_PRIVATE)

        loadUserSettings()

        saveButton.setOnClickListener {
            saveUserSettings()
            //finish() // Close the SettingsActivity
            startActivity(
                Intent(
                    this@Settings,
                    MapsActivity::class.java
                )
            )
        }


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.ic_settings
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
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadUserSettings() {
        val unitSystem = sharedPreferences.getString("unitSystem", "metric")
        val maxDistance = sharedPreferences.getFloat("maxDistance", 10.0f) // Default max distance

        if (unitSystem == "metric") {
            (findViewById<RadioButton>(R.id.metricRadioButton)).isChecked = true
        } else {
            (findViewById<RadioButton>(R.id.imperialRadioButton)).isChecked = true
        }

        distanceEditText.setText(maxDistance.toString())
    }

    private fun saveUserSettings() {
        val selectedUnitSystem = if (findViewById<RadioButton>(R.id.metricRadioButton).isChecked) {
            "metric"
        } else {
            "imperial"
        }

        val maxDistance = distanceEditText.text.toString().toFloat()

        val editor = sharedPreferences.edit()
        editor.putString("unitSystem", selectedUnitSystem)
        editor.putFloat("maxDistance", maxDistance)
        editor.apply()
    }
}