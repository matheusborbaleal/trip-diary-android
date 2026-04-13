package com.matheus.diariodeviagens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TripListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TripAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trip_list, container, false)
        auth = FirebaseAuth.getInstance()
        recyclerView = view.findViewById(R.id.rvTrips)
        recyclerView.layoutManager = LinearLayoutManager(context)
        listenToCloudSync(view)
        return view
    }

    private fun listenToCloudSync(view: View) {
        val currentUser = auth.currentUser ?: return
        val loading = view.findViewById<ProgressBar>(R.id.pbLoading)
        val emptyState = view.findViewById<View>(R.id.llEmptyState)

        loading.visibility = View.VISIBLE
        val databaseRef = FirebaseDatabase.getInstance().reference
            .child("users").child(currentUser.uid).child("trips")

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isAdded) return

                loading.visibility = View.GONE
                val cloudTrips = mutableListOf<Trip>()

                for (item in snapshot.children) {
                    val tripIdFromFirebase = item.child("id").value?.toString() ?: ""

                    val trip = Trip(
                        id = tripIdFromFirebase,
                        location = item.child("location").getValue(String::class.java) ?: "",
                        description = item.child("description").getValue(String::class.java) ?: "",
                        imageUrl = item.child("imageUrl").getValue(String::class.java),
                        latitude = item.child("latitude").getValue(Double::class.java),
                        longitude = item.child("longitude").getValue(Double::class.java)
                    )
                    cloudTrips.add(trip)
                }

                if (cloudTrips.isEmpty()) {
                    emptyState.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    emptyState.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    adapter = TripAdapter(cloudTrips) { clickedTrip ->
                        val intent = Intent(context, TripFormActivity::class.java)
                        intent.putExtra("TRIP_ID", clickedTrip.id)
                        intent.putExtra("TRIP_LOCATION", clickedTrip.location)
                        intent.putExtra("TRIP_DESCRIPTION", clickedTrip.description)
                        intent.putExtra("TRIP_IMAGE_URL", clickedTrip.imageUrl)
                        clickedTrip.latitude?.let { intent.putExtra("TRIP_LAT", it) }
                        clickedTrip.longitude?.let { intent.putExtra("TRIP_LONG", it) }
                        startActivity(intent)
                    }
                    recyclerView.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                if (isAdded) loading.visibility = View.GONE
            }
        })
    }
}