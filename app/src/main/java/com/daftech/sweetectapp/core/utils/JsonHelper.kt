package com.daftech.sweetectapp.core.utils

import android.content.Context
import com.daftech.sweetectapp.core.data.FoodData
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class JsonHelper(private val context: Context) {
    private fun parsingFileToString(fileName: String): String?{
        return try {
            val `is` = context.assets.open(fileName)
            val buffer = ByteArray(`is`.available())
            `is`.read(buffer)
            `is`.close()
            String(buffer)
        }catch (ex: IOException){
            ex.printStackTrace()
            null
        }
    }

    fun loadFoods():List<FoodData>{
        val list = ArrayList<FoodData>()
        try {
            val responseObject = JSONObject(parsingFileToString("FoodResponse.json").toString())
            val listArray = responseObject.getJSONArray("foods")
            for (i in 0 until listArray.length()){
                val food =  listArray.getJSONObject(i)
                val labels = food.getString("labels")
                val calorie = food.getString("calorie")
                val sugar = food.getString("sugar")

                val foodResponse = FoodData(labels, calorie, sugar)
                list.add(foodResponse)
            }
        }catch (e: JSONException){
            e.printStackTrace()
        }
        return list
    }
}