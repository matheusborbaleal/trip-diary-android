package com.matheus.diariodeviagens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TripAdapter(
    private val trips: List<Trip>,
    private val onItemClick: (Trip) -> Unit
) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLocation: TextView = itemView.findViewById(R.id.tvTripLocation)
        val tvDescription: TextView = itemView.findViewById(R.id.tvTripDescription)
        val ivPhoto: ImageView = itemView.findViewById(R.id.ivTripCardPhoto)
        val tvCoords: TextView = itemView.findViewById(R.id.tvTripCardCoords)
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

        if (!trip.imageUrl.isNullOrEmpty()) {
            holder.ivPhoto.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(trip.imageUrl)
                .into(holder.ivPhoto)
        } else {
            holder.ivPhoto.visibility = View.GONE
        }

        if (trip.latitude != null && trip.longitude != null) {
            holder.tvCoords.visibility = View.VISIBLE
            holder.tvCoords.text = "📍 GPS: ${trip.latitude}, ${trip.longitude}"
        } else {
            holder.tvCoords.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onItemClick(trip)
        }
    }

    override fun getItemCount(): Int = trips.size
}