package com.daftech.sweetectapp.ui.detail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.daftech.sweetectapp.core.utils.ViewModelFactory
import com.daftech.sweetectapp.databinding.ActivityDetailBinding
import com.daftech.sweetectapp.ui.base.BaseActivity
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        //viewModel
        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]


        //get uri image bitmap
        val imageUri: Uri? = intent.data
        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)

        //load image
        loadImage(imageUri)

        //implement to get tensorImage
        val max = getMax(viewModel.getTensorFoods(this, bitmap))

        binding.btnPredicted.setOnClickListener {
            binding.progbar.visibility = View.VISIBLE

            viewModel.getFoods().observe(this) { foods ->
                val food = foods[max]

                binding.apply {
                    //label food
                    foodName.text = "Your Food Is ${food.labels}"
                    foodDesc.text = "your sugar and calories take from ${food.labels} (as per 100gr food / portion)"

                    //calorie chart
                    calorieChart.visibility = View.VISIBLE
                    progbar.visibility = View.GONE
                    calorieChart(food.calorie)

                    btnPredicted.visibility = View.GONE
                    btnSelectTake.visibility = View.VISIBLE


                    //sugar chart
                    sugarChart.visibility = View.VISIBLE
                    progbar.visibility = View.GONE
                    sugarChart(food.sugar)
                }
            }
        }

        binding.btnSelectTake.setOnClickListener {
            val intent = Intent(this, BaseActivity::class.java)
            startActivity(intent)
        }
    }

    //load image function
    private fun loadImage(image : Uri?){
        Log.d("photo", image.toString())
        if (image != null){
            binding.foodPhoto.setImageURI(image)
        }else{
            binding.foodName.text = "Tidak Ada Foto"
        }
    }

    //get value of data
    private fun getMax(arr:FloatArray) : Int{
        var ind = 0
        var min = 0.0f

        for (i in 0..100){
            if (arr[i] > min){
                ind = i
                min = arr[i]
            }
        }
        return ind
    }

    //chart function
    private fun calorieChart(data: String){
        val visitors: ArrayList<PieEntry> = ArrayList()

        visitors.add(PieEntry(data.toFloat(), ""))
        val pieDataSet = PieDataSet(visitors, "Calories / 100g")
        pieDataSet.color = Color.rgb(157,190,185)
        pieDataSet.valueTextSize = 12f

        val pieData = PieData(pieDataSet)

        binding.apply {
            calorieChart.data = pieData
            calorieChart.description.isEnabled = false
            calorieChart.centerText = "Calories"
            calorieChart.animate()
        }
    }

    private fun sugarChart(resultSugar: String) {
        val visitors: ArrayList<PieEntry> = ArrayList()

        if (resultSugar.toFloat() > 0.0){
            visitors.add(PieEntry(resultSugar.toFloat(), ""))

            val pieDataSet = PieDataSet(visitors, "Sugar / 100g")
            pieDataSet.color = Color.rgb(255,192,203)
            pieDataSet.valueTextColor = Color.BLACK
            pieDataSet.valueTextSize = 12f

            val pieData = PieData(pieDataSet)

            binding.apply {
                sugarChart.data = pieData
                sugarChart.description.isEnabled = false
                sugarChart.centerText = "Sugar"
                sugarChart.animate()
            }
        }else{
            visitors.add(PieEntry(resultSugar.toFloat(), ""))

            val pieDataSet = PieDataSet(visitors, "Sugar / 100g")
            pieDataSet.color = Color.rgb(255,192,203)
            pieDataSet.valueTextColor = Color.BLACK
            pieDataSet.valueTextSize = 12f

            val pieData = PieData(pieDataSet)

            binding.apply {
                sugarChart.data = pieData
                sugarChart.description.isEnabled = false
                sugarChart.centerText = "No Sugar"
                sugarChart.animate()
            }
        }
    }
}
