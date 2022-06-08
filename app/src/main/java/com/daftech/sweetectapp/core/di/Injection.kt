package com.daftech.sweetectapp.core.di

import android.content.Context
import com.daftech.sweetectapp.core.repository.FoodRepository
import com.daftech.sweetectapp.core.source.LocalDataSource
import com.daftech.sweetectapp.core.utils.JsonHelper

object Injection {
    fun provideRepository(context: Context): FoodRepository{
        val localDataSource = LocalDataSource.getInstance(JsonHelper(context))
        return FoodRepository.getInstance(localDataSource)
    }
}