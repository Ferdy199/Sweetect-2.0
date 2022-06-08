package com.daftech.sweetectapp.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daftech.sweetectapp.R
import com.daftech.sweetectapp.databinding.FragmentViewPagerBinding
import com.daftech.sweetectapp.ui.onboarding.screen.GalleryFragment
import com.daftech.sweetectapp.ui.onboarding.screen.HowToScanFragment
import com.daftech.sweetectapp.ui.onboarding.screen.MeaningFragment

class ViewPagerFragment : Fragment() {

    private var _binding : FragmentViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null){

            val fragmentList = arrayListOf<Fragment>(
                MeaningFragment(),
                HowToScanFragment(),
                GalleryFragment()
            )

            val adapter = ViewPagerAdapter(
                fragmentList,
                requireActivity().supportFragmentManager,
                lifecycle
            )
            binding.iniViewPager.adapter = adapter
        }
    }

}