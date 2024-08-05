package com.worksy.hr.art.views.fragments.claimRequest

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentInProgressClaimRequestBinding
import com.worksy.hr.art.models.claimRequestResponse.ClaimRequestListResponse
import com.worksy.hr.art.models.claimRequestResponse.InProgressClaimResponse
import com.worksy.hr.art.utils.SnackbarUtils
import com.worksy.hr.art.utils.UIResult
import com.worksy.hr.art.viewmodels.ClaimViewModel
import com.worksy.hr.art.views.activity.Homeactivity
import com.worksy.hr.art.views.adapter.claimRequestListing.InProgressClaimRequestAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InProgressClaimRequestFragment : Fragment(R.layout.fragment_in_progress_claim_request) {
    private lateinit var rvAdapter: InProgressClaimRequestAdapter
    private val progressList = mutableListOf<InProgressClaimResponse>()
    private var _binding: FragmentInProgressClaimRequestBinding? = null
    private val viewModel: ClaimViewModel by activityViewModels()
    private val binding get() = _binding!!
    private lateinit var context: Context
    var claimRequest = listOf<ClaimRequestListResponse.ClaimRequestData.ClaimForm>()
    var totalClaimRequest = listOf<ClaimRequestListResponse.ClaimRequestData.ClaimForm.ClaimItem>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInProgressClaimRequestBinding.inflate(inflater, container, false)
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
        viewModel.claimRequest("pending")
        viewModel.listClaimRequest.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is UIResult.Success -> {
                        Homeactivity.hideLoading()
                        it.data?.let { data->
                            if(data.data.forms.isNotEmpty() && data.data.forms!=null) {
                                claimRequest = data.data.forms// Assign the list of ClaimRequestData
                                totalClaimRequest = data.data.forms[0].items
                                viewModel.updatePendingCount(data.data.pendingCount)
                                viewModel.updateCompletedCount(data.data.completedCount)
                                Log.d("setUpFragment", "setUpFragment: " + claimRequest)
                                rvAdapter = InProgressClaimRequestAdapter(context,
                                    claimRequest!!, onViewDetailClick = { position,code,title,desc,formId->
                                        val bundle = Bundle()
                                        bundle.putString("claimRequestCode", code)
                                        bundle.putString("codeTitle", title)
                                        bundle.putString("codeDesc", desc ?: "")
                                        bundle.putString("formId", formId)
                                        findNavController().navigate(R.id.navi_claim_request_detail,bundle)
                                    })
                                binding.inProgressRecyclerView.layoutManager =
                                    LinearLayoutManager(requireContext())
                                binding.inProgressRecyclerView.adapter = rvAdapter
                            }
                        }

                    }

                    is UIResult.Error -> {
                        Homeactivity.hideLoading()
                        SnackbarUtils.showLongSnackbar(
                            requireView(),
                            it.message.toString(),
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