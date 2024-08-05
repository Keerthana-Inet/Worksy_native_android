package com.worksy.hr.art.views.fragments.claimRequest

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.ClaimRejectBottomsheetBinding
import com.worksy.hr.art.databinding.FragmentClaimRequestTabDetailBinding
import com.worksy.hr.art.models.claimRequestResponse.GetClaimFormResponse
import com.worksy.hr.art.models.claimRequestResponse.InProgressClaimResponse
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import com.worksy.hr.art.utils.SnackbarUtils
import com.worksy.hr.art.utils.UIResult
import com.worksy.hr.art.viewmodels.ClaimViewModel
import com.worksy.hr.art.views.activity.Homeactivity
import com.worksy.hr.art.views.fragments.claimApproval.ClaimApprove
import com.worksy.hr.art.views.fragments.claimApproval.ClaimPending
import com.worksy.hr.art.views.fragments.claimApproval.ClaimReject
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClaimRequestTabDetailFragment : Fragment(R.layout.fragment_claim_request_tab_detail) {
    var code: String? = ""
    var codeTitle: String? = ""
    var codeDesc: String? = ""
    var formId: String? = ""
    private var _binding: FragmentClaimRequestTabDetailBinding? = null
    private val viewmodel: ClaimViewModel by activityViewModels()
    private val binding get() = _binding!!
    private lateinit var context: Context
    @Inject
    lateinit var appPref: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentClaimRequestTabDetailBinding.inflate(inflater, container, false)
        (activity as? Homeactivity)?.setBottomNavigationVisibility(View.GONE)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? Homeactivity)?.setBottomNavigationVisibility(View.VISIBLE)
        // unbind view onDestroyView
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context = this.requireContext()
        (activity as? Homeactivity)?.setBottomNavigationVisibility(View.GONE)
        if (arguments != null) {
            code = requireArguments().getString("claimRequestCode")
            codeTitle = requireArguments().getString("codeTitle")
            codeDesc = requireArguments().getString("codeDesc")
            formId = requireArguments().getString("formId")
            Log.d("claimRequestCode", "claimRequestCode: " + code)
        }
        val tabTitles = listOf("Pending", "Approve","Reject")
        val fragments = listOf(
            ClaimRequestPending(),
            ClaimRequestApproveTab(),
            ClaimRequestRejectTab()
        )
        binding.customTabLayout.setupWithViewModel(viewmodel, viewLifecycleOwner, requireActivity(), tabTitles, fragments,null,binding)
        setUpFragment(binding,formId)
        binding.apply {
            actionbarLyt.setOnClickListener {
                findNavController().popBackStack()
            }
            title.text = code
            titleTxt.text = codeTitle
            subtitleTxt.text = codeDesc
            binding.cancelRequestBtn.setOnClickListener {

            }
            binding.editBtn.setOnClickListener {
                binding.editBtn.text="Done Edit"
                if(binding.editBtn.text.equals("Done Edit")){
                    binding.editBtn.setOnClickListener {
                        SnackbarUtils.showLongSnackbar(requireView(),"Tap to edit the Pending item")
                    }

                }
            }


        }
    }
    private fun setUpFragment(binding: FragmentClaimRequestTabDetailBinding, formId: String?) {
        binding.customTabLayout.claimRequestDetailApi(viewmodel,viewLifecycleOwner,requireActivity(),formId!!,appPref,binding)
    }
    override fun onResume() {
        super.onResume()
        setUpFragment(binding,formId)
    }
}