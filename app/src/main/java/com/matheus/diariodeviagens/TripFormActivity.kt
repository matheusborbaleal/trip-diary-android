package com.matheus.diariodeviagens

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class TripFormActivity : AppCompatActivity() {

    private var tripId: String? = null
    private var selectedImageUri: Uri? = null
    private var existingImageUrl: String? = null
    private var currentLat: Double? = null
    private var currentLong: Double? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var storage: FirebaseStorage
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var ivTripPhoto: ImageView
    private lateinit var tvLocationCoords: TextView
    private lateinit var pbSaving: ProgressBar
    private lateinit var buttons: List<Button>

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            ivTripPhoto.setImageURI(it)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) fetchLocation()
    }

    private val requestNotificationLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) Toast.makeText(this, "Avisos desativados", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_form)

        storage = FirebaseStorage.getInstance()
        analytics = Firebase.analytics
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        ivTripPhoto = findViewById(R.id.ivTripPhoto)
        tvLocationCoords = findViewById(R.id.tvLocationCoords)
        pbSaving = findViewById(R.id.pbSaving)

        val btnSave = findViewById<Button>(R.id.btnSaveTrip)
        val btnSelect = findViewById<Button>(R.id.btnSelectPhoto)
        val btnLoc = findViewById<Button>(R.id.btnGetLocation)
        val btnDel = findViewById<Button>(R.id.btnDeleteTrip)

        buttons = listOf(btnSave, btnSelect, btnLoc, btnDel)

        if (intent.hasExtra("TRIP_ID")) {
            tripId = intent.getStringExtra("TRIP_ID")
            findViewById<EditText>(R.id.etTripLocation).setText(intent.getStringExtra("TRIP_LOCATION"))
            findViewById<EditText>(R.id.etTripDescription).setText(intent.getStringExtra("TRIP_DESCRIPTION"))
            existingImageUrl = intent.getStringExtra("TRIP_IMAGE_URL")
            if (!existingImageUrl.isNullOrEmpty()) Glide.with(this).load(existingImageUrl).into(ivTripPhoto)
            currentLat = intent.getDoubleExtra("TRIP_LAT", 0.0)
            currentLong = intent.getDoubleExtra("TRIP_LONG", 0.0)
            tvLocationCoords.text = "📍 $currentLat, $currentLong"
            btnSave.text = "Atualizar"
            btnDel.visibility = View.VISIBLE
        }

        checkNotificationPermission()

        btnSelect.setOnClickListener { pickImageLauncher.launch("image/*") }
        btnLoc.setOnClickListener { checkLocationPermission() }
        btnSave.setOnClickListener { saveTrip() }
        btnDel.setOnClickListener { deleteTrip() }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestNotificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fetchLocation()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun fetchLocation() {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    currentLat = it.latitude
                    currentLong = it.longitude
                    tvLocationCoords.text = "📍 $currentLat, $currentLong"
                }
            }
        } catch (e: SecurityException) { e.printStackTrace() }
    }

    private fun saveTrip() {
        val loc = findViewById<EditText>(R.id.etTripLocation).text.toString()
        val desc = findViewById<EditText>(R.id.etTripDescription).text.toString()

        if (loc.isEmpty() || desc.isEmpty()) return

        setLoading(true)

        if (selectedImageUri != null) {
            val ref = storage.reference.child("photos/${UUID.randomUUID()}.jpg")
            ref.putFile(selectedImageUri!!).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri -> finalizeSave(loc, desc, uri.toString()) }
            }
        } else {
            finalizeSave(loc, desc, existingImageUrl)
        }
    }

    private fun finalizeSave(loc: String, desc: String, url: String?) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val dbRef = FirebaseDatabase.getInstance().reference.child("users").child(user.uid).child("trips")
        val finalId = tripId ?: dbRef.push().key ?: UUID.randomUUID().toString()

        lifecycleScope.launch(Dispatchers.IO) {
            val trip = Trip(finalId, loc, desc, url, currentLat, currentLong)
            AppDatabase.getDatabase(this@TripFormActivity).tripDao().insert(trip)
            dbRef.child(finalId).setValue(trip)

            withContext(Dispatchers.Main) {
                showNotification(loc)
                finish()
            }
        }
    }

    private fun showNotification(name: String) {
        val channelId = "trip_channel"
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Trips", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Sucesso")
            .setContentText("Viagem para $name salva!")
            .setAutoCancel(true)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            manager.notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

    private fun deleteTrip() {
        val id = tripId ?: return
        val user = FirebaseAuth.getInstance().currentUser ?: return
        lifecycleScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(this@TripFormActivity).tripDao().delete(Trip(id = id))
            FirebaseDatabase.getInstance().reference.child("users").child(user.uid).child("trips").child(id).removeValue()
            withContext(Dispatchers.Main) { finish() }
        }
    }

    private fun setLoading(loading: Boolean) {
        pbSaving.visibility = if (loading) View.VISIBLE else View.GONE
        buttons.forEach { it.isEnabled = !loading }
    }
}