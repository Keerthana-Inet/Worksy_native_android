package com.worksy.hr.art.views.fragments.travelRequest

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentTravelRequestBottomSheetBinding
import com.worksy.hr.art.views.activity.Homeactivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TravelRequestBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentTravelRequestBottomSheetBinding?=null
    private val binding get() = _binding!!
    private lateinit var context: Context
    private var selectedTabPosition = 0
    var setUpView: List<View>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTravelRequestBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // unbind view onDestroyView
        (activity as? Homeactivity)?.setBottomNavigationVisibility(View.VISIBLE)
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context=this.requireContext()
        (activity as? Homeactivity)?.setBottomNavigationVisibility(View.GONE)
        val viewPager: ViewPager = binding.bottomViewPager
        val adapter = BottomSheetPagerAdapter(this.childFragmentManager)
        binding.bottomViewPager.adapter = adapter

        setUpView = listOf(
            binding.tabView1,
            binding.tabView2,
            binding.tabView3,
            binding.tabView4
        )
        viewPager.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            @SuppressLint("SuspiciousIndentation")
            override fun onPageSelected(position: Int) {
                selectedTabPosition = position
                  adapter.updateOnboardViewBackground(position, setUpView)
                if (position == 0) {
                    binding.nextBtn.visibility = View.VISIBLE
                    binding.linearLyt.visibility = View.GONE
                    binding.backImg.visibility = View.GONE
                } else if (position == 1) {
                    binding.nextBtn.visibility = View.GONE
                    binding.linearLyt.visibility = View.VISIBLE
                    binding.backImg.visibility = View.VISIBLE
                    binding.nxt2Btn.setText("Next")
                } else if (position == 2) {
                    binding.nextBtn.visibility = View.GONE
                    binding.linearLyt.visibility = View.VISIBLE
                    binding.backImg.visibility = View.VISIBLE
                    binding.nxt2Btn.setText("Next")
                } else if (position == 3) {
                    binding.nextBtn.visibility = View.GONE
                    binding.linearLyt.visibility = View.VISIBLE
                    binding.backImg.visibility = View.VISIBLE
                    binding.nxt2Btn.setText("Submit")
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                // No need to handle this for your case
            }
        })
        binding.backImg.setOnClickListener {
            handleTabSelection(selectedTabPosition - 1,binding)
        }
        binding.nextBtn.setOnClickListener {
            handleTabSelection(selectedTabPosition + 1,binding)
        }
        binding.nxt2Btn.setOnClickListener {
            handleTabSelection(selectedTabPosition + 1,binding)
        }
        binding.cancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun handleTabSelection(
        tabPosition: Int,
        binding: FragmentTravelRequestBottomSheetBinding
    ) {
        if (tabPosition < this.binding.bottomViewPager.adapter?.count ?: 0) {
            this.binding.bottomViewPager.setCurrentItem(tabPosition, true)
        }
    }

    inner class BottomSheetPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment {
            // Return the fragment for each page
            return when (position) {
                0 -> TravelRequestTab1()
                1 -> TravelRequestTab2()
                2 -> TravelRequestTab3()
                3 -> TravelRequestTab4()
                // Add more cases for other fragments
                else -> TravelRequestTab1()
            }
        }

        override fun getCount(): Int {
            // Return the number of pages
            return 4  // Adjust based on the number of fragments
        }
        fun updateOnboardViewBackground(position: Int, setUpViews: List<View>?) {
            setUpViews?.forEachIndexed { index, view ->
                if (index <= position) {
                    view.setBackgroundResource(R.drawable.blue_background_border)
                } else {
                    view.setBackgroundResource(R.drawable.rounded_bottom_corners)
                }
            }
        }
    }
}
