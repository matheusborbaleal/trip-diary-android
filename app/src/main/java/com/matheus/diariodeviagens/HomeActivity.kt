package com.matheus.diariodeviagens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TripAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val fabAddTrip = findViewById<View>(R.id.fabAddTrip)
        recyclerView = findViewById(R.id.rvTrips)

        recyclerView.layoutManager = LinearLayoutManager(this)

        listenToCloudSync()

        btnLogout.setOnClickListener {
            val dao = AppDatabase.getDatabase(this).tripDao()
            dao.deleteAllTrips()

            auth.signOut()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        fabAddTrip.setOnClickListener {
            val intent = Intent(this, TripFormActivity::class.java)
            startActivity(intent)
        }
    }

    private fun listenToCloudSync() {
        val currentUser = auth.currentUser
        val emptyState = findViewById<View>(R.id.llEmptyState)
        val loading = findViewById<ProgressBar>(R.id.pbLoading)

        if (currentUser != null) {
            loading.visibility = View.VISIBLE

            val databaseRef = FirebaseDatabase.getInstance().reference
                .child("users").child(currentUser.uid).child("trips")

            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    loading.visibility = View.GONE

                    val cloudTrips = mutableListOf<Trip>()

                    for (item in snapshot.children) {
                        val id = item.child("id").getValue(Long::class.java)?.toInt() ?: 0
                        val location = item.child("location").getValue(String::class.java) ?: ""
                        val description = item.child("description").getValue(String::class.java) ?: ""
                        cloudTrips.add(Trip(id, location, description))
                    }

                    if (cloudTrips.isEmpty()) {
                        emptyState.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        emptyState.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE

                        adapter = TripAdapter(cloudTrips) { clickedTrip ->
                            val intent = Intent(this@HomeActivity, TripFormActivity::class.java)
                            intent.putExtra("TRIP_ID", clickedTrip.id)
                            intent.putExtra("TRIP_LOCATION", clickedTrip.location)
                            intent.putExtra("TRIP_DESCRIPTION", clickedTrip.description)
                            startActivity(intent)
                        }
                        recyclerView.adapter = adapter
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    loading.visibility = View.GONE
                    println("Firebase Error: ${error.message}")
                }
            })
        }
    }
}