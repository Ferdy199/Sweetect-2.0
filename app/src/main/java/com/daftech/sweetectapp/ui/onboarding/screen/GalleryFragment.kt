package com.daftech.sweetectapp.ui.onboarding.screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.daftech.sweetectapp.R
import com.daftech.sweetectapp.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding : FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null){
            val animSide = AnimationUtils.loadAnimation(activity, R.anim.side_anim)
            val animBottom = AnimationUtils.loadAnimation(activity, R.anim.bottom_anim)
            binding.apply {
                textView.startAnimation(animSide)
                textView2.startAnimation(animBottom)
            }
            binding.finish.setOnClickListener {
                findNavController().navigate(R.id.action_viewPagerFragment_to_signInActivity)
                setOnBoardingFinish()
            }
        }
    }

    private fun setOnBoardingFinish(){
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", false)
        editor.apply()
    }


}