package com.worksy.hr.art.views.fragments.claimApproval

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.ClaimRejectBottomsheetBinding
import com.worksy.hr.art.databinding.FragmentClaimApprovalDetailBinding
import com.worksy.hr.art.models.claimApproval.ActionRequest
import com.worksy.hr.art.models.claimRequestResponse.InProgressClaimResponse
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import com.worksy.hr.art.utils.SnackbarUtils
import com.worksy.hr.art.utils.UIResult
import com.worksy.hr.art.viewmodels.ClaimViewModel
import com.worksy.hr.art.views.activity.Homeactivity
import com.worksy.hr.art.views.fragments.claimRequest.CompletedClaimRequestFragment
import com.worksy.hr.art.views.fragments.claimRequest.InProgressClaimRequestFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClaimApprovalDetailFragment : Fragment(R.layout.fragment_claim_approval_detail){
    private val viewmodel: ClaimViewModel by activityViewModels()
    private var _binding: FragmentClaimApprovalDetailBinding?=null
    var code: String? = ""
    var claimTitle: String? = ""
    var desc: String? = ""
    var formId: String? = ""
    var itemListId : Set<String> = emptySet()
    private val binding get() = _binding!!
    private lateinit var context: Context
    private var bottomSheetBinding: ClaimRejectBottomsheetBinding? = null

    @Inject
    lateinit var appPref: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentClaimApprovalDetailBinding.inflate(inflater, container, false)
        (activity as? Homeactivity)?.setBottomNavigationVisibility(View.GONE)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? Homeactivity)?.setBottomNavigationVisibility(View.VISIBLE)
        // unbind view onDestroyView
        viewmodel.clearSelections()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context=this.requireContext()
        (activity as? Homeactivity)?.setBottomNavigationVisibility(View.GONE)
        if (arguments != null) {
            code=requireArguments().getString("Code")
            claimTitle=requireArguments().getString("DetailPageTitle")
            desc=requireArguments().getString("DetailPageDesc")
            formId=requireArguments().getString("formId")
        }
        val tabTitles = listOf("Pending", "Approve","Reject")
        val fragments = listOf(
            ClaimPending(),
            ClaimApprove(),
            ClaimReject()
        )
        binding.customTabLayout.setupWithViewModel(viewmodel, viewLifecycleOwner, requireActivity(), tabTitles, fragments,binding)
        setUpFragment(binding,formId!!)
        binding.apply {
            title.text=code
            titleTxt.text=claimTitle
            subtitleTxt.text=desc
            actionbarLyt.setOnClickListener {
                findNavController().popBackStack()
            }
            binding.rejectBtn.setOnClickListener {
                showBottomSheetDialog("Reject")
            }
            binding.approveBtn.setOnClickListener {
                showBottomSheetDialog("Approve")
            }
        }
    }
    private fun showBottomSheetDialog(type: String) {
        var bottomSheetDialog: BottomSheetDialog? = null
        var bottomSheet: View? = null
        bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialog)
        bottomSheetBinding = ClaimRejectBottomsheetBinding.inflate(layoutInflater)
        bottomSheetDialog!!.setContentView(bottomSheetBinding!!.root)
        if(type.equals("Reject")){
            bottomSheetBinding!!.button.text="Reject"
            bottomSheetBinding!!.title.text="Reject Remark"
        }else if(type.equals("Approve")){
            bottomSheetBinding!!.button.text="Approve"
            bottomSheetBinding!!.title.text="Approve Remark"
        }
        bottomSheetBinding!!.apply {
            backImg.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            cancelBtn.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            button.setOnClickListener {
                if (type.equals("Reject")) {
                    if (editTxt.text?.length!! >= 4) {
                        rejectRequestAction(editTxt.text.toString())
                        bottomSheetDialog.dismiss()
                    } else {
                        Toast.makeText(requireActivity(),"Enter Your Remark",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (editTxt.text?.length!! >= 4) {
                        approveRequestAction(editTxt.text.toString())
                        bottomSheetDialog.dismiss()
                    } else {
                        Toast.makeText(requireActivity(),"Enter Your Remark",Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

        bottomSheetDialog!!.setOnShowListener { _ ->
            bottomSheet = bottomSheetDialog?.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet,
            )
            if (bottomSheet != null) {
                val behavior = BottomSheetBehavior.from(bottomSheet!!)

                val screenHeight = Resources.getSystem().displayMetrics.heightPixels
                val customPeekHeight = (screenHeight * 0.99).toInt()

                behavior.peekHeight = customPeekHeight
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                Log.d("BottomSheet", "Calculated Peek Height: $customPeekHeight")
                Log.d("BottomSheet", "Actual Height: ${bottomSheet?.height}")
            } else {
                // Handle the case where the bottomSheet is null (optional)
            }
        }
        bottomSheetDialog!!.show()
    }


    private fun approveRequestAction(editText: String) {
        val request = ActionRequest(
            action = "approve",
            remarks = editText,
            formId = formId.toString(),
            items = listOf(ActionRequest.ListItem(appPref.getPendingTotalAmount().toString(), itemListId))
        )

        val gson = Gson()
        val json = gson.toJson(request)
        println(json)
        viewmodel.actionApproveApi(request)
        ObserverApi()
    }
    private fun rejectRequestAction(editText: String) {
        val request = ActionRequest(
            action = "reject",
            remarks = editText,
            formId = formId.toString(),
            items = listOf(ActionRequest.ListItem(appPref.getPendingTotalAmount().toString(), itemListId))
        )

        val gson = Gson()
        val json = gson.toJson(request)
        println(json)
        viewmodel.actionRejectApi(request)
        ObserverApi()
    }

    private fun ObserverApi() {
        viewmodel.actionApprove.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is UIResult.Success -> {
                        Homeactivity.hideLoading()

                        if(it.data?.status.equals("success")){
                            it.data?.let {
                                SnackbarUtils.showLongSnackbar(requireView(),it.message)
                                setUpFragment(binding,formId!!)
                            }
                        } else if(it.data?.status.equals("error")){
                            SnackbarUtils.showLongSnackbar(requireView(),it.message!!)
                        }


                    }

                    is UIResult.Error -> {
                        Homeactivity.hideLoading()
                        setUpFragment(binding,formId!!)
                       /* setupTabLayout("","","")
                        setUpViewPager()*/
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
        viewmodel.actionReject.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is UIResult.Success -> {
                        Homeactivity.hideLoading()
                        if(it.data?.status.equals("success")){
                            it.data?.let {
                                SnackbarUtils.showLongSnackbar(requireView(),it.message)
                                setUpFragment(binding,formId!!)
                            }
                        } else if(it.data?.status.equals("error")){
                            SnackbarUtils.showLongSnackbar(requireView(),it.message!!)
                        }

                    }

                    is UIResult.Error -> {
                        Homeactivity.hideLoading()
                        setUpFragment(binding,formId!!)
                      /*  setupTabLayout("","","")
                        setUpViewPager()*/
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
    private fun setUpFragment(binding: FragmentClaimApprovalDetailBinding,formId:String) {
       binding.customTabLayout.claimDetailApi(viewmodel,viewLifecycleOwner,requireActivity(),formId!!,appPref,binding)
        viewmodel.selectedIds.observe(requireActivity(), Observer {
                selectedId->
            itemListId=selectedId
            Log.d("SELECTEDID", "SELECTEDID: "+selectedId)
        })
    }
    override fun onResume() {
        super.onResume()
        setUpFragment(binding,formId!!)
    }
}

