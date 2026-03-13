package com.matheus.diariodeviagens

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TripDao {
    @Insert
    fun insert(trip: Trip)

    @Update
    fun update(trip: Trip)

    @Delete
    fun delete(trip: Trip)

    @Query("SELECT * FROM trips")
    fun getAllTrips(): List<Trip>

    @Query("DELETE FROM trips")
    fun deleteAllTrips()
}