package com.daftech.sweetectapp.core.source

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.daftech.sweetectapp.core.data.DataHistory
import com.daftech.sweetectapp.core.data.FoodData
import com.daftech.sweetectapp.core.data.signin.SignIn
import com.daftech.sweetectapp.core.data.signup.SignUp

interface FoodDataSource {
    fun getAllFoods():LiveData<List<FoodData>>
    fun getTensorFoods(context: Context ,bitmap: Bitmap): FloatArray
    fun signInToFirebase(signIn: SignIn, context: Context): LiveData<SignIn>
    fun signUpToFirebase(signUp: SignUp, context: Context)
    fun insertHistoryToFirebase(foodHistory: DataHistory, uid: String, context: Context)
    fun getAllFirebaseHistory(uid: String): LiveData<ArrayList<DataHistory>>
    fun deleteFirebase(historyId: String, uid: String, context: Context)
}