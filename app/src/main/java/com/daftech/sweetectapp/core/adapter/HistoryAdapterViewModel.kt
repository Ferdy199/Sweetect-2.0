package com.daftech.sweetectapp.core.adapter

import android.content.Context
import androidx.lifecycle.ViewModel
import com.daftech.sweetectapp.core.repository.FoodRepository

class HistoryAdapterViewModel(private val foodRepository: FoodRepository): ViewModel() {

    fun deleteFirebase(history: String, uid: String, context: Context){
        foodRepository.deleteFirebase(history, uid, context)
    }

}