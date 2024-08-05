package com.worksy.hr.art.views.fragments.splash

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentSplashGetReadyBinding
import com.worksy.hr.art.views.fragments.onboard.OnBoardFragment1
import com.worksy.hr.art.views.fragments.onboard.OnBoardFragment2
import com.worksy.hr.art.views.fragments.onboard.OnBoardFragment3
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashGetReadyFragment : Fragment(R.layout.fragment_splash_get_ready) {

    private var _binding: FragmentSplashGetReadyBinding? = null
    private val binding get() = _binding!!
    private lateinit var context: Context
    private var selectedTabPosition = 0
     var onboardViews: List<View>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashGetReadyBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context = requireContext()
        onboardViews = listOf(
            binding.onboard1,
            binding.onboard2,
            binding.onboard3
        )

        val adapter = OnboardingPagerAdapter(childFragmentManager)
        binding.viewPager.adapter = adapter

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // No need to handle this for your case
            }

            override fun onPageSelected(position: Int) {
                selectedTabPosition = position
                adapter.updateOnboardViewBackground(position,onboardViews)
                if(position==0){
                    binding.nextTxt.visibility=View.VISIBLE
                    binding.previousTxt.visibility=View.GONE
                    binding.skipTxt.visibility=View.VISIBLE
                    binding.btnContinue.visibility=View.GONE
                }
                else if(position==1){
                    binding.previousTxt.visibility=View.VISIBLE
                    binding.nextTxt.visibility=View.VISIBLE
                    binding.skipTxt.visibility=View.VISIBLE
                    binding.btnContinue.visibility=View.GONE
                }else{
                    binding.previousTxt.visibility=View.GONE
                    binding.nextTxt.visibility=View.GONE
                    binding.skipTxt.visibility=View.GONE
                    binding.btnContinue.visibility=View.VISIBLE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                // No need to handle this for your case
            }
        })

        binding.skipTxt.setOnClickListener {
            findNavController().navigate(R.id.navi_get_started)
        }

        // Set continue button click listener
        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.navi_get_started)
        }
        binding.nextTxt.setOnClickListener {
            handleTabSelection(selectedTabPosition + 1)
        }
        binding.previousTxt.setOnClickListener {
            handleTabSelection(selectedTabPosition - 1)
        }
    }

    private fun handleTabSelection(tabPosition: Int) {
        if (tabPosition < binding.viewPager.adapter?.count ?: 0) {
            binding.viewPager.setCurrentItem(tabPosition, true)
        }
    }

    class OnboardingPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> OnBoardFragment1()
                1 -> OnBoardFragment2()
                2 -> OnBoardFragment3()
                else -> OnBoardFragment1()
            }
        }

        override fun getCount(): Int {
            return 3 // Number of fragments
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return "" // No titles for tabs
        }
        fun updateOnboardViewBackground(position: Int, onboardViews: List<View>?) {
            onboardViews?.forEachIndexed { index, view ->
                if (index <= position) {
                    view.setBackgroundColor(view.resources.getColor(R.color.thin_gray))
                } else {
                    view.setBackgroundColor(view.resources.getColor(R.color.onboard_color))
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
