package com.matheus.diariodeviagens

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class TripFormActivity : AppCompatActivity() {

    private var tripId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_form)

        val editLocation = findViewById<EditText>(R.id.etTripLocation)
        val editDescription = findViewById<EditText>(R.id.etTripDescription)
        val btnSave = findViewById<Button>(R.id.btnSaveTrip)
        val btnDelete = findViewById<Button>(R.id.btnDeleteTrip)

        if (intent.hasExtra("TRIP_ID")) {
            tripId = intent.getIntExtra("TRIP_ID", -1)
            editLocation.setText(intent.getStringExtra("TRIP_LOCATION"))
            editDescription.setText(intent.getStringExtra("TRIP_DESCRIPTION"))
            btnSave.text = "Atualizar Viagem" // <--- Retornado para PT-BR
            btnDelete.visibility = View.VISIBLE
        }

        btnSave.setOnClickListener {
            val location = editLocation.text.toString()
            val description = editDescription.text.toString()

            if (location.isNotEmpty() && description.isNotEmpty()) {
                val dao = AppDatabase.getDatabase(this).tripDao()

                if (tripId == -1) {
                    val newTrip = Trip(location = location, description = description)
                    dao.insert(newTrip)
                    Toast.makeText(this, "Viagem salva!", Toast.LENGTH_SHORT).show()
                } else {
                    val updatedTrip = Trip(id = tripId, location = location, description = description)
                    dao.update(updatedTrip)
                    Toast.makeText(this, "Viagem atualizada!", Toast.LENGTH_SHORT).show()
                }

                syncWithFirebase()
                finish()
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            val tripToDelete = Trip(
                id = tripId,
                location = editLocation.text.toString(),
                description = editDescription.text.toString()
            )

            val dao = AppDatabase.getDatabase(this).tripDao()
            dao.delete(tripToDelete)

            Toast.makeText(this, "Viagem excluída!", Toast.LENGTH_SHORT).show()

            syncWithFirebase()
            finish()
        }
    }

    private fun syncWithFirebase() {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val databaseRef = FirebaseDatabase.getInstance().reference
            val dao = AppDatabase.getDatabase(this).tripDao()
            val allTrips = dao.getAllTrips()

            databaseRef.child("users").child(currentUser.uid).child("trips")
                .setValue(allTrips)
        }
    }
}