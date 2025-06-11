package com.example.wild_wonders.Adapter

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AddDetailsData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val observationLocation: String,
    val species: String,
    val description: String
)

