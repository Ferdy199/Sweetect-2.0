package com.daftech.sweetectapp.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.daftech.sweetectapp.R
import com.daftech.sweetectapp.core.data.signup.SignUp
import com.daftech.sweetectapp.core.utils.ViewModelFactory
import com.daftech.sweetectapp.databinding.ActivitySignUpBinding
import com.daftech.sweetectapp.ui.signin.SignInActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private var signUp: SignUp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        signUp = SignUp()

        val factory = ViewModelFactory.getInstance(application)
        val viewModel = ViewModelProvider(this, factory)[SignUpViewModel::class.java]

        binding.tvToSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignUp.setOnClickListener {

            val email = binding.emailEt.text.toString().trim()
            val pass = binding.passET.text.toString().trim()
            val confirmPass = binding.confirmPassEt.text.toString().trim()

            when{
                email.isEmpty() -> binding.emailEt.error = getString(R.string.empty)
                pass.isEmpty() -> binding.passET.error = getString(R.string.empty)
                pass != confirmPass -> {
                    Toast.makeText(this, "Password Not Matching", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    signUp.let {
                        it?.email = email
                        it?.pass = pass
                    }
                    if (signUp != null){
                        viewModel.signUpToFirebase(signUp as SignUp, this)
                        val intent = Intent(applicationContext, SignInActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "Sign Up Kosong", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

    }
}