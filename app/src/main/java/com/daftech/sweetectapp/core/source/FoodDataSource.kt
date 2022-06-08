package com.daftech.sweetectapp.core.source

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.daftech.sweetectapp.core.data.FoodData

interface FoodDataSource {
    fun getAllFoods():LiveData<List<FoodData>>
    fun getTensorFoods(context: Context ,bitmap: Bitmap): FloatArray
}