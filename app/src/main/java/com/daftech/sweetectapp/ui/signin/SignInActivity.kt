package com.daftech.sweetectapp.ui.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daftech.sweetectapp.R
import com.daftech.sweetectapp.databinding.ActivitySignInBinding
import com.daftech.sweetectapp.ui.dashboard.DashboardActivty
import com.daftech.sweetectapp.ui.signup.SignUpActivity

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.tvToSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignIn.setOnClickListener {
            val intent = Intent(this, DashboardActivty::class.java)
            startActivity(intent)
        }
    }
}