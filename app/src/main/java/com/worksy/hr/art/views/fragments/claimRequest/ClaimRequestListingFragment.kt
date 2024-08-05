package com.worksy.hr.art.views.fragments.claimRequest

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentClaimRequestListingBinding
import com.worksy.hr.art.viewmodels.ClaimViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClaimRequestListingFragment : Fragment(R.layout.fragment_claim_request_listing) {

    private var _binding: FragmentClaimRequestListingBinding? = null
    private val binding get() = _binding!!
    private lateinit var context: Context
    private var isExpanded = false
    private var pendingCountValue: String = ""
    private var completedCountValue: String = ""
    private val viewModel: ClaimViewModel by activityViewModels()
    private val fromBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(requireActivity(), R.anim.from_bottom_fab)
    }
    private val topBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(requireActivity(), R.anim.from_top_fab)
    }
    private val rotateClockWiseFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(requireActivity(), R.anim.rotate_clock_wise)
    }
    private val rotateAntiClockWiseFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(requireActivity(), R.anim.rotate_anti_clockwise)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClaimRequestListingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context = this.requireContext()
      //  shrinkFab()

        val tabTitles = listOf("InProgress", "Completed")
        val fragments = listOf(
            InProgressClaimRequestFragment(),
            CompletedClaimRequestFragment(),
        )

        binding.customTabLayout.setupWithViewModel(viewModel, viewLifecycleOwner, requireActivity(), tabTitles, fragments)
        binding.actionbarLyt.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.fabPdf.setOnClickListener {
            findNavController().navigate(R.id.navi_createform_bottomsheet)
        }

        binding.fabPlus.setOnClickListener {
            findNavController().navigate(R.id.navi_createform_bottomsheet)
        }
    }
    private fun expandedFab() {
        binding.fabPlus.background = ContextCompat.getDrawable(context, R.drawable.circle_border)
        binding.fabPlus.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.fab_blue))
        binding.fabPlus.startAnimation(rotateClockWiseFabAnim)
        binding.fabPdf.startAnimation(fromBottomFabAnim)
        binding.fabCamera.startAnimation(fromBottomFabAnim)
        binding.fabDownload.startAnimation(fromBottomFabAnim)
        isExpanded = !isExpanded
    }

    private fun shrinkFab() {
        binding.fabPlus.startAnimation(rotateAntiClockWiseFabAnim)
        binding.fabPdf.startAnimation(topBottomFabAnim)
        binding.fabCamera.startAnimation(topBottomFabAnim)
        binding.fabDownload.startAnimation(topBottomFabAnim)
        isExpanded = !isExpanded
    }
}