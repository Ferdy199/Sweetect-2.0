package com.daftech.sweetectapp.core.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.daftech.sweetectapp.core.di.Injection
import com.daftech.sweetectapp.core.repository.FoodRepository
import com.daftech.sweetectapp.ui.detail.DetailViewModel

class ViewModelFactory private constructor(private val mFoodRepository: FoodRepository): ViewModelProvider.NewInstanceFactory(){
    companion object{
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory{
            return instance ?: synchronized(this){
                instance ?: ViewModelFactory(Injection.provideRepository(context)).apply {
                    instance = this
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(mFoodRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}