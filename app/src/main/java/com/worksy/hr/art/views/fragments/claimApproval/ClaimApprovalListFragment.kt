package com.worksy.hr.art.views.fragments.claimApproval
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentClaimApprovalListBinding
import com.worksy.hr.art.viewmodels.ClaimViewModel
import com.worksy.hr.art.views.fragments.claimRequest.CompletedClaimRequestFragment
import com.worksy.hr.art.views.fragments.claimRequest.InProgressClaimRequestFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClaimApprovalListFragment : Fragment(R.layout.fragment_claim_approval_list){

    private var _binding: FragmentClaimApprovalListBinding?=null
    private val viewModel : ClaimViewModel by activityViewModels()
    private val binding get() = _binding!!
    private lateinit var context: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentClaimApprovalListBinding.inflate(inflater, container, false)
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
        val tabTitles = listOf("InProgress", "Completed")
        val fragments = listOf(
            InProgressClaimApprovalFragment(),
            CompletedClaimApprovalFragment(),
        )
        binding.customTabLayout.setupWithViewModel(viewModel, viewLifecycleOwner, requireActivity(), tabTitles, fragments,null,null)
        binding.actionbarLyt.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
