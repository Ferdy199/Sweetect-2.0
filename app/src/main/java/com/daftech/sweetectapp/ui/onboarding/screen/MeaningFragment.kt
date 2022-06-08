package com.daftech.sweetectapp.ui.onboarding.screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.daftech.sweetectapp.R
import com.daftech.sweetectapp.databinding.FragmentMeaningBinding


class MeaningFragment : Fragment() {

    private var _binding : FragmentMeaningBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMeaningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null){
            val viewPager = activity?.findViewById<ViewPager2>(R.id.iniViewPager)
            val animSide = AnimationUtils.loadAnimation(activity, R.anim.side_anim)
            val animBottom = AnimationUtils.loadAnimation(activity, R.anim.bottom_anim)
            binding.apply {
                textView.startAnimation(animSide)
                textView2.startAnimation(animBottom)
            }

           binding.next.setOnClickListener {
                viewPager?.currentItem = 1
           }
        }
    }
}