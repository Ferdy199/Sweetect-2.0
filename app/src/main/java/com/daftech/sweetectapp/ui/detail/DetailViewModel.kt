package com.daftech.sweetectapp.ui.detail

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.daftech.sweetectapp.core.data.DataHistory
import com.daftech.sweetectapp.core.data.FoodData
import com.daftech.sweetectapp.core.repository.FoodRepository

class DetailViewModel(private val foodRepository: FoodRepository) : ViewModel() {
    fun getFoods(): LiveData<List<FoodData>> = foodRepository.getAllFoods()
    fun getTensorFoods(context: Context, bitmap: Bitmap): FloatArray = foodRepository.getTensorFoods(context, bitmap)
    fun insertHistoryToFirebase(foodHistory: DataHistory, uid: String, context: Context){
        foodRepository.insertHistoryToFirebase(foodHistory, uid, context)
    }
}