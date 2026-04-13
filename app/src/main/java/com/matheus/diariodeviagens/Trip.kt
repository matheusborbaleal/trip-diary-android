package com.matheus.diariodeviagens

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey
    var id: String = "",
    val location: String = "",
    val description: String = "",
    val imageUrl: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)