package com.daftech.sweetectapp.ui.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import com.daftech.sweetectapp.core.data.signup.SignUp
import com.daftech.sweetectapp.core.repository.FoodRepository

class SignUpViewModel(private val foodRepository: FoodRepository): ViewModel() {

    fun signUpToFirebase(signUp: SignUp, context: Context){
        foodRepository.signUpToFirebase(signUp, context)
    }
}
