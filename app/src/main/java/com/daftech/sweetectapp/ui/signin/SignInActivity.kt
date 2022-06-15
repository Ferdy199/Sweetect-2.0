package com.daftech.sweetectapp.ui.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import com.daftech.sweetectapp.R
import com.daftech.sweetectapp.core.data.signin.SignIn
import com.daftech.sweetectapp.core.utils.ViewModelFactory
import com.daftech.sweetectapp.databinding.ActivitySignInBinding
import com.daftech.sweetectapp.ui.dashboard.DashboardActivty
import com.daftech.sweetectapp.ui.signup.SignUpActivity
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private var signIn: SignIn? = null
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        signIn = SignIn()
        firebaseAuth = FirebaseAuth.getInstance()

        val factory = ViewModelFactory.getInstance(application)
        val viewModel = ViewModelProvider(this, factory)[SignInViewModel::class.java]

        binding.tvToSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignIn.setOnClickListener {

            binding.progbar.visibility = View.VISIBLE
            binding.btnSignIn.visibility = View.GONE

            val email = binding.emailEt.text.toString().trim()
            val pass = binding.passET.text.toString().trim()

            when{
                email.isEmpty() -> binding.emailEt.error = getString(R.string.empty)
                pass.isEmpty() -> binding.passET.error = getString(R.string.empty)
                else -> {
                    signIn.let {
                        it?.email = email
                        it?.pass = pass
                    }
                }
            }

            viewModel.signInToFirebase(signIn as SignIn, this)?.observe(this){ signIn ->
                if (signIn.state == true){
                    val intent = Intent(this, DashboardActivty::class.java)
                    binding.progbar.visibility = View.GONE
                    binding.btnSignIn.visibility = View.VISIBLE
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                }
                Log.d("Isi Data Login", signIn!!.email.toString())
                Log.d("Isi Data bisa", signIn.state.toString())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null){
            val intent = Intent(this, DashboardActivty::class.java)
            startActivity(intent)
        }
    }
}