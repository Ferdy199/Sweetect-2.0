package com.daftech.sweetectapp.core.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.daftech.sweetectapp.core.di.Injection
import com.daftech.sweetectapp.core.repository.FoodRepository
import com.daftech.sweetectapp.ui.dashboard.DashboardViewModel
import com.daftech.sweetectapp.ui.detail.DetailViewModel
import com.daftech.sweetectapp.ui.signin.SignInViewModel
import com.daftech.sweetectapp.ui.signup.SignUpViewModel

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
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                SignInViewModel(mFoodRepository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(mFoodRepository) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(mFoodRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}