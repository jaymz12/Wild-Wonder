package com.example.wild_wonders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wild_wonders.Adapter.AddDetailsData
import com.example.wild_wonders.Adapter.MyAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class ListData : AppCompatActivity() {
    //private lateinit var recv: RecyclerView
    //private lateinit var userList:ArrayList<AddDetailsData>
    //private lateinit var userAdapter: Adapter

    private lateinit var recyclerView: RecyclerView
    //private lateinit var adapter: MyAdapter
    //private var dataList: List<AddDetailsData> = emptyList() // Initialize as an empty list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_data)

        //userList = ArrayList()
        //recv = findViewById(R.id.mRecycler)
        //userAdapter = Adapter(this, userList)
        //recv.layoutManager = LinearLayoutManager(this)
        //recv.adapter = userAdapter

        // Assuming you have a Dao to fetch the data
        //dataList = yourDao.getAllAddDetailsData()

        recyclerView = findViewById(R.id.mRecycler)

        val data = arrayListOf<AddDetailsData>()

        val name = intent.getStringExtra("Name")
        val location = intent.getStringExtra("Location")
        val species = intent.getStringExtra("Species")
        val description = intent.getStringExtra("Description")


        data.add(AddDetailsData(1, "Bird Name: $name", "Observation Location: $location",
            "Species: $species", "Description: $description"))

        val adapter = MyAdapter(this, data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        //dataList = ArrayList()
        //recyclerView = findViewById(R.id.mRecycler)
        //recyclerView.layoutManager = LinearLayoutManager(this)

        //adapter = MyAdapter(this, dataList)
        //recyclerView.adapter = adapter


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.ic_view
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.ic_home -> {
                    // Launch the MapsActivity
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.ic_view -> {
                    // Launch the DashboardActivity
                    val intent = Intent(this, ListData::class.java)
                    startActivity(intent)
                    true
                }
                R.id.ic_sighting -> {
                    // Launch the NotificationsActivity
                    val intent = Intent(this, AddObservation::class.java)
                    startActivity(intent)
                    true
                }
                R.id.ic_settings -> {
                    // Launch the NotificationsActivity
                    val intent = Intent(this, Settings::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

    }
}