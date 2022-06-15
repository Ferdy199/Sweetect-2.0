package com.daftech.sweetectapp.core.data.signup

data class SignUp(
    var email: String? = null,
    var pass : String? = null,
    val confirmPass: String? = null
)