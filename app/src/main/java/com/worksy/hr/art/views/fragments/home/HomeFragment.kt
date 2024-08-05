package com.worksy.hr.art.views.fragments.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentHomeBinding
import com.worksy.hr.art.utils.NetworkReceiver
import com.worksy.hr.art.utils.NetworkUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding?=null
    private val binding get() = _binding!!
    private lateinit var context: Context
    private var isExpanded=false
    private val fromBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(requireActivity(),R.anim.from_bottom_fab)
    }
    private val topBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(requireActivity(),R.anim.from_top_fab)
    }
    private val rotateClockWiseFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(requireActivity(),R.anim.rotate_clock_wise)
    }
    private val rotateAntiClockWiseFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(requireActivity(),R.anim.rotate_anti_clockwise)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // unbind view onDestroyView
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context=this.requireContext()
        shrinkFab()
        setupTabLayout()
        setUpViewPager()
        binding.fabPlus.setOnClickListener {
            if(isExpanded){
                shrinkFab()
            }else{
                expandedFab()
            }
        }
        binding.fabPdf.setOnClickListener {
            findNavController().navigate(R.id.navi_travel_request_bottomsheet)
        }
    }

    private fun setUpViewPager() {
        binding.viewPager.apply {
            adapter = ViewPagerAdapter(childFragmentManager, binding.tabLayout.tabCount)
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
        }
    }
    inner class ViewPagerAdapter(
        fm: FragmentManager,
        var totalTabs: Int,
    ) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    InProgressFragment()
                }
                1 -> {
                    CompletedFragment()
                }
                else -> InProgressFragment()
            }
        }

        override fun getCount(): Int {
            return totalTabs
        }
    }
    private fun setupTabLayout() {
        binding.tabLayout.apply {
            addTab(this.newTab().setText("In Progress"))
            addTab(this.newTab().setText("Completed"))
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let {
                        binding.viewPager.currentItem = it
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    //Nothing to do
                }
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    //Nothing to do
                }
            })
        }

    }
    private fun expandedFab() {
        binding.fabPlus.startAnimation(rotateClockWiseFabAnim)
        binding.fabPdf.startAnimation(fromBottomFabAnim)
        binding.fabCamera.startAnimation(fromBottomFabAnim)
        binding.fabDownload.startAnimation(fromBottomFabAnim)
        isExpanded=!isExpanded
    }

    private fun shrinkFab() {
        binding.fabPlus.startAnimation(rotateAntiClockWiseFabAnim)
        binding.fabPdf.startAnimation(topBottomFabAnim)
        binding.fabCamera.startAnimation(topBottomFabAnim)
        binding.fabDownload.startAnimation(topBottomFabAnim)
        isExpanded=!isExpanded
    }
}