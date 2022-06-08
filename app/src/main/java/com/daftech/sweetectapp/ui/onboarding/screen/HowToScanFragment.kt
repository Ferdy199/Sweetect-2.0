package com.daftech.sweetectapp.ui.onboarding.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.viewpager2.widget.ViewPager2
import com.daftech.sweetectapp.R
import com.daftech.sweetectapp.databinding.FragmentHowToScanBinding
import com.daftech.sweetectapp.databinding.FragmentMeaningBinding

class HowToScanFragment : Fragment() {

    private var _binding : FragmentHowToScanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHowToScanBinding.inflate(inflater, container, false)
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
            val viewPager = activity?.findViewById<ViewPager2>(R.id.iniViewPager)
            binding.next.setOnClickListener {
                viewPager?.currentItem = 2
            }
        }
    }

}