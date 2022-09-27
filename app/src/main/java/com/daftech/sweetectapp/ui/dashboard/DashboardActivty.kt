package com.daftech.sweetectapp.ui.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daftech.sweetectapp.R
import com.daftech.sweetectapp.core.adapter.HistoryAdapter
import com.daftech.sweetectapp.core.utils.ViewModelFactory
import com.daftech.sweetectapp.databinding.ActivityDashboardBinding
import com.daftech.sweetectapp.ui.detail.DetailActivity
import com.daftech.sweetectapp.ui.signin.SignInActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.io.ByteArrayOutputStream

class DashboardActivty : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding : ActivityDashboardBinding
    private lateinit var bitmap : Bitmap
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var viewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        itemTouchHelper.attachToRecyclerView(binding.rvHistory)

        setSupportActionBar(binding.toolbar)

        firebaseAuth = FirebaseAuth.getInstance()

        historyAdapter = HistoryAdapter()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DashboardViewModel::class.java]

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)


        when(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            PackageManager.PERMISSION_GRANTED -> {

            }
            else -> {
                checkForPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, "write storage", 102)
            }
        }

        viewModel.getAllFirebaseHistory(firebaseAuth.currentUser!!.uid).observe(this){ history ->
            when{
                history != null -> {
                    dataNull(false)
                    historyAdapter.setListHistory(history)
                    Log.d("Isi Data list", history.orEmpty().isEmpty().toString())
                }
                else -> {
                    dataNull(true)
                    Log.d("Isi Data list", history.orEmpty().isEmpty().toString())
                }
            }
        }

        binding.apply {
            btnScan.setOnClickListener {
                when(ContextCompat.checkSelfPermission(this@DashboardActivty, Manifest.permission.CAMERA)){
                    PackageManager.PERMISSION_GRANTED -> {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(intent, 101)
                    }
                    else -> {
                        checkForPermissions(Manifest.permission.CAMERA, "camera", 101)
                    }
                }
            }

            btnGallery.setOnClickListener {
                when(ContextCompat.checkSelfPermission(this@DashboardActivty, Manifest.permission.READ_EXTERNAL_STORAGE)){
                    PackageManager.PERMISSION_GRANTED -> {
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "image/*"

                        startActivityForResult(intent, 100)
                    }
                    else -> {
                        checkForPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, "storage", 100)
                    }
                }
            }

            tvUsername.text = firebaseAuth.currentUser!!.email

        }

        with(binding.rvHistory){
            this.layoutManager = LinearLayoutManager(this@DashboardActivty)
            this.setHasFixedSize(true)
            this.adapter = historyAdapter
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK){
            val uri : Uri? = data?.data
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            val intent = Intent(this, DetailActivity::class.java)
            intent.data = uri
            startActivity(intent)
        }

        if (requestCode == 101 && resultCode == RESULT_OK){
            val bytes = ByteArrayOutputStream()
            bitmap = data?.getParcelableExtra("data")!!
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(this.contentResolver, bitmap, null, null)
            val imgPhoto = Uri.parse(path)
            val intent = Intent(this, DetailActivity::class.java)
            intent.data = imgPhoto
            startActivity(intent)
        }

    }

    private fun checkForPermissions(permission: String, name: String, requestCode: Int){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> {
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)
                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun innerCheck(name: String){
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "$name Permission Rejected", Toast.LENGTH_SHORT).show()
            }
        }
        when(requestCode){
            100 -> innerCheck("storage")
            101 -> innerCheck("camera")
            102 -> innerCheck("write storage")
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int){
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permission required $name")
            setTitle("Permission required")
            setPositiveButton("Ok"){ _, _ ->
                ActivityCompat.requestPermissions(this@DashboardActivty, arrayOf(permission), requestCode)
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

    private fun dataNull(state: Boolean){
        when(state){
            true -> {
                binding.lottieAnimationView.visibility = View.VISIBLE
                binding.tvNoData.visibility = View.VISIBLE
            }
            else -> {
                binding.lottieAnimationView.visibility = View.GONE
                binding.tvNoData.visibility = View.GONE
                binding.rvHistory.visibility = View.VISIBLE
            }
        }
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback(){
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
        ): Int {
            return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder,
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val swippedPosition = viewHolder.adapterPosition
            val historyEntity = historyAdapter.getSwippedData(swippedPosition)
            historyEntity?.let {
                viewModel.deleteFirebase(it.id!!, firebaseAuth.currentUser!!.uid, this@DashboardActivty)
            }
        }

    })

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        firebaseAuth = FirebaseAuth.getInstance()
        val title = getString(R.string.app_name)
        when(item.itemId){
            R.id.nav_logout -> {
                firebaseAuth.signOut()
                Intent(this@DashboardActivty, SignInActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        }
        supportActionBar?.title = title
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}