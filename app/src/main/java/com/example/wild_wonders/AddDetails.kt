package com.example.wild_wonders

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import coil.load
import com.example.wild_wonders.Adapter.Adapter
import com.example.wild_wonders.Adapter.AddDetailsData
import com.example.wild_wonders.databinding.ActivityAddDetailsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class AddDetails : AppCompatActivity() {

    lateinit var binding: ActivityAddDetailsBinding
    val CAMERA_REQUEST_CODE = 1
    private lateinit var saveBtn: Button

    private lateinit var name: EditText
    private lateinit var location: EditText
    private lateinit var species: EditText
    private lateinit var description: EditText

    private lateinit var userList:ArrayList<AddDetailsData>
    private lateinit var userAdapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        saveBtn = findViewById(R.id.save_btn)

        name = findViewById(R.id.name)
        location = findViewById(R.id.location)
        species = findViewById(R.id.species)
        description = findViewById(R.id.descrip)

        userList = ArrayList()
        userAdapter = Adapter(this, userList)


        val imageView = findViewById<ImageView>(R.id.camera)
        val imageViewBack = findViewById<ImageView>(R.id.back)

        imageView.setOnClickListener {
            cameraPermission()
        }

        imageViewBack.setOnClickListener {
            startActivity(
                Intent(
                    this@AddDetails,
                    AddObservation::class.java
                )
            )
        }

        saveBtn.setOnClickListener {
            /*val nam = name.getText().toString()
            val loc = location.getText().toString()
            val spec = species.getText().toString()
            val desc = description.getText().toString()
            val data = AddDetailsData(1,nam, loc, spec, desc)*/

            /*userList.add(AddDetailsData("Name: $nam","Observation Location : $loc","Species : $spec",
                "Description : $desc")
            )
            userAdapter.notifyDataSetChanged()*/
            BirdDetails()
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
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


    private fun BirdDetails() {

        Log.d("AddDetails", "BirdDetails method called")

        val birdname = name.text.toString().trim { it <= ' ' }
        val loc = location.text.toString().trim { it <= ' ' }
        val spec = species.text.toString().trim { it <= ' ' }
        val desc = description.text.toString().trim { it <= ' ' }

        if (birdname.isEmpty()) {
            name.setError("Can not be Empty")
            name.requestFocus()
            return
        }

        if (loc.isEmpty()) {
            location.error = "Location field cannot be empty"
            location.requestFocus()
            return
        }

        if (spec.isEmpty()) {
            species.error = "Species field cannot be empty"
            species.requestFocus()
            return
        }

        if (desc.isEmpty()) {
            description.error = "Description field cannot be empty"
            description.requestFocus()
            return
        }

        if (birdname.isNotEmpty() && loc.isNotEmpty() && spec.isNotEmpty() && desc.isNotEmpty()){
            Toast.makeText(this@AddDetails, "Saved Successfully", Toast.LENGTH_LONG).show()
            startActivity(
                Intent(this@AddDetails, ListData::class.java).also {
                    it.putExtra("Name",birdname)
                    it.putExtra("Location",loc)
                    it.putExtra("Species",spec)
                    it.putExtra("Description",desc)
                    startActivity(it)
                    finish()
                }
            )
            //startActivity(Intent(this@AddDetails, ListData::class.java))
        }
    }

    private fun cameraPermission(){
        Dexter.withContext(this)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA).withListener(

                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {

                            if (report.areAllPermissionsGranted()) {
                                camera()
                            }
                        }
                    }
                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?) {
                        showRotationalDialogForPermission()
                    }
                }
            ).onSameThread().check()
    }

    private fun camera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {

                CAMERA_REQUEST_CODE -> {

                    val bitmap = data?.extras?.get("data") as Bitmap

                    binding.camera.load(bitmap)
                }
            }
        }
    }

    private fun showRotationalDialogForPermission() {
        AlertDialog.Builder(this)
            .setMessage("It looks like you have turned off permissions"
                    + "required for this feature. It can be enable under App settings!!!")

            .setPositiveButton("Go TO SETTINGS") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}