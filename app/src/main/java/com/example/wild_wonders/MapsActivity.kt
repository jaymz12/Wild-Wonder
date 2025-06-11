package com.example.wild_wonders


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.directions.route.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import java.util.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    RoutingListener {

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var start: LatLng? = null
    var end: LatLng? = null

    private val previousPolylines: MutableList<Polyline> = mutableListOf()
    private var currentDestinationMarker: Marker? = null

    companion object {
        private const val Location_Request_Code = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map)
                as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigation.selectedItemId = R.id.ic_home
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.ic_home -> {
                    // Launch the MapsActivity
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.ic_view -> {
                    // Launch the ListDataActivity
                    val intent = Intent(this, ListData::class.java)
                    startActivity(intent)
                    true
                }
                R.id.ic_sighting -> {
                    // Launch the AddObservationActivity
                    val intent = Intent(this, AddObservation::class.java)
                    startActivity(intent)
                    true
                }
                R.id.ic_settings -> {
                    // Launch the SettingsActivity
                    val intent = Intent(this, Settings::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        val locationList = loadJsonDataFromAsset(requireContext(), "eBird.json")
        for (location in locationList) {
            val marker = MarkerOptions()
                .position(LatLng(location.lat!!, location.lng!!))
                .title(location.comName)
            val addedMarker = googleMap.addMarker(marker)

            addedMarker!!.tag = location
        }

        /* mMap.setOnMapClickListener { latLng ->
            end = latLng

            // Call the Findroutes() method to start the route search.
            Findroutes(start, end)
        }*/

        googleMap.setOnMarkerClickListener(this)

        setUpMap()
    }

    private fun requireContext(): Context {
        return this
    }

    fun loadJsonDataFromAsset(context: Context, fileName: String): List<EBirdModel> {
        val locationList = mutableListOf<EBirdModel>()
        val jsonString = context.assets.open(fileName).bufferedReader().use {
            it.readText()
        }
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val name = jsonObject.getString("comName")
            val speciesCode = jsonObject.getString("speciesCode")
            val sciName = jsonObject.getString("sciName")
            val locId = jsonObject.getString("locId")
            val locName = jsonObject.getString("locName")
            val latitude = jsonObject.getDouble("lat")
            val longitude = jsonObject.getDouble("lng")
            val obsDt = jsonObject.getString("obsDt")
            val howMany = jsonObject.optInt("howMany", 6)
            val obsValid = jsonObject.getBoolean("obsValid")
            val obsReviewed = jsonObject.getBoolean("obsReviewed")
            val locationPrivate = jsonObject.getBoolean("locationPrivate")
            val subId = jsonObject.getString("subId")
            locationList.add(
                EBirdModel(
                    speciesCode,
                    name,
                    sciName,
                    locId,
                    locName,
                    obsDt,
                    howMany,
                    latitude,
                    longitude,
                    obsValid,
                    obsReviewed,
                    locationPrivate,
                    subId
                )
            )
        }

        return locationList
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ), Location_Request_Code
            )
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLocation = LatLng(location.latitude, location.longitude)
                placeMarkerHolder(currentLocation)

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12f))

                mMap.setOnMapClickListener { latLng ->
                    end = latLng
                    //mMap.clear()
                    start = LatLng(
                        location.getLatitude(),
                        location.getLongitude()
                    )
                    //start route finding
                    Findroutes(start, end)
                }
            }
        }
    }

    fun Findroutes(Start: LatLng?, End: LatLng?) {

        // Clear the map before starting a new route search.
        //mMap.clear()

        if (Start == null || End == null) {
            Toast.makeText(this@MapsActivity, "Unable to get location", Toast.LENGTH_LONG).show()
        } else {
            val routing = Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(Start, End)
                .key("AIzaSyApJejjoJ2yq1uuFPeFgTMgPNjzdV14dEY") //also define your api key here.
                .build()
            routing.execute()

            // Create Location objects for start and end points
            val startLocation = Location("start").apply {
                latitude = Start.latitude
                longitude = Start.longitude
            }

            val endLocation = Location("end").apply {
                latitude = End.latitude
                longitude = End.longitude
            }

            // Calculate the distance
            val distanceInMiles = startLocation.distanceTo(endLocation)
            val calcFunc = distanceInMiles/1000;
            val distanceInKm = calcFunc * 1.609344

            // Display the distance
            // Add this code in your `onCreate` method after `distanceTextView.text = "Distance: ${String.format("%.2f", distanceInKm)} km"`

            val toggleButton = findViewById<ToggleButton>(R.id.toggle_button)
            // Set the text to "Distance"
            //toggleButton.text = "Distance"

            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                val distanceTextView = findViewById<TextView>(R.id.distance_text_view)

                if (isChecked) {
                    // Display in miles
                    distanceTextView.text = "Distance: ${String.format("%.2f", calcFunc)} miles"
                } else {
                    // Display in kilometers
                    distanceTextView.text = "Distance: ${String.format("%.2f", distanceInKm)} km"
                }
            }

        }

    }


    private fun placeMarkerHolder(currentLocation: LatLng) {
        val markerOptions = MarkerOptions().position(currentLocation)
        markerOptions.title("My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        mMap.addMarker(markerOptions)

    }

    //When a user clicks on a hotspot it will give routes to that hotspot
    override fun onMarkerClick(marker: Marker): Boolean {
        // Retrieve the tag to get information about the marker
        val locationData = marker.tag as? EBirdModel
        if (locationData != null) {
            // Get the destination LatLng from the clicked marker
            val destinationLatLng = marker.position

            // Clear the previous destination marker and associated directions
            if (currentDestinationMarker != null) {
                currentDestinationMarker?.remove()
                clearPreviousPolylines()
            }

            // Set the current destination marker
            currentDestinationMarker = marker


            // Start route finding from your current location to the clicked marker's position
            Findroutes(LatLng(lastLocation.latitude, lastLocation.longitude), destinationLatLng)

            return true
        }

        return false

    }

    private fun clearPreviousPolylines() {
        for (polyline in previousPolylines) {
            polyline.remove()
        }
        previousPolylines.clear()
    }

    override fun onRoutingFailure(p0: RouteException?) {
        val parentLayout: View = findViewById(android.R.id.content)
        val snackbar = Snackbar.make(parentLayout, p0.toString(), Snackbar.LENGTH_LONG)
        snackbar.show()
        Findroutes(start, end)
    }

    override fun onRoutingStart() {
        Toast.makeText(this@MapsActivity, "Finding Route...", Toast.LENGTH_LONG).show()
    }

    override fun onRoutingSuccess(routes: ArrayList<Route>?, shortestRouteIndex: Int) {
        // Clear previous polylines without removing markers
        clearPreviousPolylines()

        var center: LatLng? = null
        if (start != null) {
            center = start
        }

        var zoom: Float? = null
        zoom = 16f

        // Clear the map before adding the new routes.
        //mMap.clear()

        // Add the new routes to the map, but only if the routes variable is not null.
        if (routes != null) {
            for (route in routes) {
                val polylineOptions = PolylineOptions()
                polylineOptions.color(ContextCompat.getColor(this, R.color.colorPrimary))
                polylineOptions.width(7f)
                polylineOptions.addAll(route.points)

                val polyline = mMap.addPolyline(polylineOptions)

                previousPolylines.add(polyline)

                // Animate the camera to the center of the route, but only if the center variable is not null.
                if (center != null && zoom != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, zoom))
                }
            }
        }

        // Add a marker to the destination.
    }

    override fun onRoutingCancelled() {
        Findroutes(start, end);
    }

    private fun <E> List<E>.clear() {
        TODO("Not yet implemented")
    }


}