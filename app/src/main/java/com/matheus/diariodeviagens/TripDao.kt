package com.matheus.diariodeviagens

import androidx.room.*

@Dao
interface TripDao {
    @Query("SELECT * FROM trips")
    fun getAllTrips(): List<Trip>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trip: Trip)

    @Update
    fun update(trip: Trip)

    @Delete
    fun delete(trip: Trip)

    @Query("DELETE FROM trips")
    fun deleteAllTrips()
}