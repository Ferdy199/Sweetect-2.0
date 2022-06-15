package com.daftech.sweetectapp.ui.signin

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.daftech.sweetectapp.core.data.signin.SignIn
import com.daftech.sweetectapp.core.repository.FoodRepository

class SignInViewModel(private val foodRepository: FoodRepository): ViewModel() {

    fun signInToFirebase(signIn: SignIn, context: Context): LiveData<SignIn>? {
        return foodRepository.signInToFirebase(signIn, context)
    }

}