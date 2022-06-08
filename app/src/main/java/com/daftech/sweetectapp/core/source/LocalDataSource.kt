package com.daftech.sweetectapp.core.source

import android.os.Handler
import android.os.Looper
import com.daftech.sweetectapp.core.data.FoodData
import com.daftech.sweetectapp.core.utils.JsonHelper

class LocalDataSource private constructor(private val jsonHelper: JsonHelper){

    private val handler = Handler(Looper.getMainLooper())

    companion object{

        private const val SERVICE_LATENCY_IN_MILLS: Long = 1000

        @Volatile
        private var instance: LocalDataSource? = null

        fun getInstance(helper: JsonHelper): LocalDataSource =
            instance ?: synchronized(this){
                instance ?: LocalDataSource(helper).apply {
                    instance = this
                }
            }
    }

    fun getFoods(callback: LoadFoodsCallback){
        handler.postDelayed({callback.onAllFoodsReceived(jsonHelper.loadFoods())}, SERVICE_LATENCY_IN_MILLS)
    }

    interface LoadFoodsCallback{
        fun onAllFoodsReceived(foodResponses: List<FoodData>)
    }
}