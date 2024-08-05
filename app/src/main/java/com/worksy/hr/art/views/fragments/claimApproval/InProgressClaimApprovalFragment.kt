package com.worksy.hr.art.views.fragments.claimApproval

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentInProgressClaimApprovalBinding
import com.worksy.hr.art.models.claimApproval.ClaimApprovalProgressResponse
import com.worksy.hr.art.utils.SnackbarUtils
import com.worksy.hr.art.utils.UIResult
import com.worksy.hr.art.viewmodels.ClaimViewModel
import com.worksy.hr.art.views.activity.Homeactivity
import com.worksy.hr.art.views.adapter.claimApproval.InProgressClaimApprovalAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InProgressClaimApprovalFragment : Fragment(R.layout.fragment_in_progress_claim_approval) {
    private lateinit var rvAdapter: InProgressClaimApprovalAdapter
    private var progressList = listOf<ClaimApprovalProgressResponse.ClaimApprovalData.ClaimEmployee>()
    private var _binding: FragmentInProgressClaimApprovalBinding? = null
    private val binding get() = _binding!!
    private var pendingCount:String=""
    private var completedCount:String=""
    private lateinit var context: Context
    private val viewModel:ClaimViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInProgressClaimApprovalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // unbind view onDestroyView
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context = this.requireContext()
        setUpFragment()
    }
    private fun setUpFragment() {
        viewModel.getClaimApprovalProgress("pending")
        viewModel.getClaimApprovalProgress.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is UIResult.Success -> {
                        Homeactivity.hideLoading()
                        it.data?.let {data->
                             viewModel.updatePendingApprovalCount(data.data.pendingCount)
                             viewModel.updateCompletedApprovalCount(data.data.completedCount)
                            progressList=data.data.employees
                            rvAdapter = InProgressClaimApprovalAdapter(requireContext(), progressList, viewModel,findNavController(),onViewDetailClick = {
                            })
                            binding.inProgressRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                            binding.inProgressRecyclerView.adapter = rvAdapter
                        }

                    }

                    is UIResult.Error -> {
                        Homeactivity.hideLoading()
                        SnackbarUtils.showLongSnackbar(
                            requireView(),
                            it.message.toString()
                        )
                    }

                    is UIResult.Loading -> {
                        Homeactivity.showLoading()
                    }
                }
            }
        }

    }
}