package com.daftech.sweetectapp.core.repository

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.daftech.sweetectapp.core.data.FoodData
import com.daftech.sweetectapp.core.source.FoodDataSource
import com.daftech.sweetectapp.core.source.LocalDataSource
import com.daftech.sweetectapp.ml.ModelRegulerizer
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class FoodRepository private constructor(private val localDataSource: LocalDataSource): FoodDataSource {

    companion object{
        @Volatile
        private var instance: FoodRepository? = null

        fun getInstance(localData: LocalDataSource): FoodRepository =
            instance ?: synchronized(this){
                instance ?: FoodRepository(localData).apply { instance = this }
            }
    }

    override fun getAllFoods(): LiveData<List<FoodData>> {
        val foodsResult = MutableLiveData<List<FoodData>>()
        localDataSource.getFoods(object : LocalDataSource.LoadFoodsCallback{
            override fun onAllFoodsReceived(foodResponses: List<FoodData>) {
                val foodList = ArrayList<FoodData>()
                for ( response in foodResponses ){
                    val food = FoodData(
                        response.calorie,
                        response.labels,
                        response.sugar
                    )
                    foodList.add(food)
                }
                foodsResult.postValue(foodList)
            }
        })
        return foodsResult
    }

    override fun getTensorFoods(context: Context, bitmap: Bitmap): FloatArray {
        //implement Tflite
        val model = ModelRegulerizer.newInstance(context)

        val imageProcessor = ImageProcessor.Builder().add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR)).build()

        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)
        tensorImage = imageProcessor.process(tensorImage)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        val byteBuffer = tensorImage.buffer
        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        model.close()

        return outputFeature0.floatArray
    }
}