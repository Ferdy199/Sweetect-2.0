package com.daftech.sweetectapp.ui.dashboard

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.daftech.sweetectapp.core.data.DataHistory
import com.daftech.sweetectapp.core.repository.FoodRepository

class DashboardViewModel(private val foodRepository: FoodRepository): ViewModel() {
    fun getAllFirebaseHistory(uid: String): LiveData<ArrayList<DataHistory>> = foodRepository.getAllFirebaseHistory(uid)
    fun deleteFirebase(history: String, uid: String, context: Context){
        foodRepository.deleteFirebase(history, uid, context)
    }
}