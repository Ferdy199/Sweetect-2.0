package com.daftech.sweetectapp.ui.onboarding

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.daftech.sweetectapp.R

class SplashFragment : Fragment() {

    private lateinit var handler: Handler
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment

        handler = Handler()
        handler.postDelayed({
            if (!setOnBoardingFinish()){
                findNavController().navigate(R.id.action_splashFragment2_to_signInActivity)
                Log.d("Isi On Boarding", setOnBoardingFinish().toString())
            }else{
                findNavController().navigate(R.id.action_splashFragment2_to_viewPagerFragment)
                Log.d("Isi On Boarding", setOnBoardingFinish().toString())
            }
        }, 3000)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun setOnBoardingFinish() : Boolean{
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

}