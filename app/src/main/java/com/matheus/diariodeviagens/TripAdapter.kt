package com.matheus.diariodeviagens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TripAdapter(
    private val trips: List<Trip>,
    private val onItemClick: (Trip) -> Unit
) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLocation: TextView = itemView.findViewById(R.id.tvTripLocation)
        val tvDescription: TextView = itemView.findViewById(R.id.tvTripDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trip, parent, false)
        return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = trips[position]
        holder.tvLocation.text = trip.location
        holder.tvDescription.text = trip.description

        holder.itemView.setOnClickListener {
            onItemClick(trip)
        }
    }

    override fun getItemCount(): Int = trips.size
}