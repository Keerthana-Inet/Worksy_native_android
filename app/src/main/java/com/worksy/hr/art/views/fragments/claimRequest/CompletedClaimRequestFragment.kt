package com.worksy.hr.art.views.fragments.claimRequest

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
import com.worksy.hr.art.databinding.FragmentCompletedClaimRequestBinding
import com.worksy.hr.art.models.claimRequestResponse.ClaimRequestListResponse
import com.worksy.hr.art.models.claimRequestResponse.InProgressClaimResponse
import com.worksy.hr.art.utils.SnackbarUtils
import com.worksy.hr.art.utils.UIResult
import com.worksy.hr.art.viewmodels.ClaimViewModel
import com.worksy.hr.art.views.activity.Homeactivity.Companion.hideLoading
import com.worksy.hr.art.views.activity.Homeactivity.Companion.showLoading
import com.worksy.hr.art.views.adapter.claimRequestListing.CompletedClaimRequestAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompletedClaimRequestFragment : Fragment(R.layout.fragment_completed_claim_request) {
    private lateinit var rvAdapter: CompletedClaimRequestAdapter
    private var progressList: MutableList<InProgressClaimResponse> = mutableListOf()
    private var _binding: FragmentCompletedClaimRequestBinding?=null
    private val binding get() = _binding!!
    private lateinit var context: Context
    private val completedViewModel: ClaimViewModel by activityViewModels()
    var claimRequest = listOf<ClaimRequestListResponse.ClaimRequestData.ClaimForm>()
    var totalClaimReq = listOf<ClaimRequestListResponse.ClaimRequestData.ClaimForm.ClaimItem>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCompletedClaimRequestBinding.inflate(inflater, container, false)
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
        setUpFragment()
    }
   private fun setUpFragment() {
        completedViewModel.claimCompleteRequest()
        completedViewModel.listCompleteClaimRequest.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is UIResult.Success -> {
                        hideLoading()
                        it.data?.let {data->
                            if(data.data.forms.isNotEmpty() && data.data.forms!=null) {
                                claimRequest = data.data.forms// Assign the list of ClaimRequestData
                                totalClaimReq = data.data.forms[0].items
                                rvAdapter = CompletedClaimRequestAdapter(context,
                                    claimRequest!!, onViewDetailClick = { position,code,title,desc,formId->
                                        val bundle = Bundle()
                                        bundle.putString("claimRequestCode", code)
                                        bundle.putString("codeTitle", title)
                                        bundle.putString("codeDesc", desc ?: "")
                                        bundle.putString("formId", formId)
                                        findNavController().navigate(R.id.navi_claim_request_detail,bundle)
                                    })
                                binding.completedRecyclerview.layoutManager =
                                    LinearLayoutManager(requireContext())
                                binding.completedRecyclerview.adapter = rvAdapter
                            }
                        }

                    }

                    is UIResult.Error -> {
                        hideLoading()
                        SnackbarUtils.showLongSnackbar(
                            requireView(),
                            "Something went wrong, Please try again later",
                        )
                    }

                    is UIResult.Loading -> {
                        showLoading()
                    }
                }
            }
        }
    }
}

