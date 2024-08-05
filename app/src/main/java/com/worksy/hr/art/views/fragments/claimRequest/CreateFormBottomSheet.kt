package com.worksy.hr.art.views.fragments.claimRequest

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentCreateFormBottomSheetBinding
import com.worksy.hr.art.models.claimRequestResponse.AddItemRequest
import com.worksy.hr.art.models.claimRequestResponse.SubmitClaimRequest
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import com.worksy.hr.art.utils.SnackbarUtils
import com.worksy.hr.art.utils.UIResult
import com.worksy.hr.art.viewmodels.ClaimViewModel
import com.worksy.hr.art.views.activity.Homeactivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateFormBottomSheet : BottomSheetDialogFragment() {
    private lateinit var gson: Gson
    private var _binding: FragmentCreateFormBottomSheetBinding?=null
    private val binding get() = _binding!!
    private lateinit var context: Context
    private var selectedTabPosition = 0
    var setUpView: List<View>? = null
    private val viewmodel : ClaimViewModel by activityViewModels()
    @Inject
    lateinit var appPref : SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateFormBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // unbind view onDestroyView
        (activity as? Homeactivity)?.setBottomNavigationVisibility(View.VISIBLE)
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        gson = Gson()
        super.onViewCreated(view, savedInstanceState)
        context=this.requireContext()
        (activity as? Homeactivity)?.setBottomNavigationVisibility(View.GONE)
        val viewPager: ViewPager = binding.bottomViewPager
        val adapter = BottomSheetPagerAdapter(this.childFragmentManager)
        binding.bottomViewPager.adapter = adapter

        setUpView = listOf(
            binding.tabView1,
            binding.tabView2
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
                    binding.nextBtn.background = ContextCompat.getDrawable(context, R.drawable.blue_background_border)
                    binding.nextBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.claim_background))
                    binding.nextBtn.text=requireActivity().resources.getString(R.string.next)
                    binding.nextBtn.setTextColor(ContextCompat.getColor(context, R.color.button_txt))
                    binding.linearLyt.visibility = View.GONE
                    binding.backImg.visibility = View.GONE
                } else if (position == 1) {
                    binding.nextBtn.visibility = View.VISIBLE
                    binding.nextBtn.background = ContextCompat.getDrawable(context, R.drawable.blue_background_border)
                    binding.nextBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.button_txt))
                    binding.nextBtn.text=requireActivity().resources.getString(R.string.create_form_txt)
                    binding.nextBtn.setTextColor(ContextCompat.getColor(context, R.color.white))
                    binding.backImg.visibility = View.VISIBLE

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
            if(binding.nextBtn.text.equals(requireActivity().resources.getString(R.string.create_form_txt))){
                submitClaimRequestApi();
            }
            else
                handleTabSelection(selectedTabPosition + 1,binding)
        }
        binding.nxt2Btn.setOnClickListener {
            handleTabSelection(selectedTabPosition + 1,binding)
        }
        binding.cancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

//    private fun addItemToClaimRequest() {
//        val newItemsList = mutableListOf<SubmitClaimRequest.SubmitItem>()
//        // Create a new item with the data from ViewModel or input fields
//        val newItem = SubmitClaimRequest.SubmitItem(
//            amount = viewmodel.claimAmount.toString(),
//            claimGroupId = viewmodel.claimGroupId.toString(),
//            currency = viewmodel.claimCurrency.toString(),
//            fileCodes = viewmodel.fileCodes,
//            itemId = viewmodel.claimItemId.toString(),
//            rate = "",
//            reason = viewmodel.claimReasons.toString(),
//            remarks = viewmodel.claimRemarks.toString(),
//            tax = viewmodel.claimTax.toString(),
//            transactionDate = viewmodel.transactionDate.toString()
//        )
//
//        newItemsList.add(newItem)
//
//        val addRequest = SubmitClaimRequest(
//            formDescription = appPref.getFormDesc().toString(),
//            formTitle = appPref.getFormTitle().toString(),
//            items = newItemsList.toMutableList()
//        )
//        Log.d("submitClaimRequestApi", "submitClaimRequestApi: "+addRequest)
//        if(appPref.getFormTitle()!!.isNotEmpty() && appPref.getFormDesc()!!.isNotEmpty()) {
//            viewmodel.submitClaim(addRequest)
//            ApiObeserver()
//        }else{
//            SnackbarUtils.showLongSnackbar(requireView(),"Enter Form Title and Description")
//        }
//    }

    private fun handleTabSelection(
        tabPosition: Int,
        binding: FragmentCreateFormBottomSheetBinding
    ) {
        if (tabPosition < this.binding.bottomViewPager.adapter?.count ?: 0) {
            this.binding.bottomViewPager.setCurrentItem(tabPosition, true)
        }
    }
    private fun submitClaimRequestApi() {
        var existingDataJson = appPref.getClaimRequest()

        if (existingDataJson.isNullOrEmpty()) {
            SnackbarUtils.showLongSnackbar(requireView(),"You need to have at least 1 claim Item")
            return
        }

        val existingData: AddItemRequest = gson.fromJson(existingDataJson, object : TypeToken<AddItemRequest>() {}.type)

        val submitItems = mutableListOf<SubmitClaimRequest.SubmitItem>()

        for(pendingItem in existingData.pendingItems) {
            for(item in pendingItem.items) {
                submitItems.add(
                    SubmitClaimRequest.SubmitItem(
                        amount = item.requestedAmount,
                        claimGroupId = item.claimGroupId,
                        claimGroupName = item.claimGroupName,
                        currency = "MYR",
                        fileCodes = item.fileUrls,
                        itemId = item.claimItemId,
                        claimItemName = item.claimItem,
                        rate = "",
                        reason = item.reason,
                        remarks = item.remarks,
                        tax = item.tax,
                        transactionDate = item.transactionData,
                        paxStaff = item.paxStaff,
                        paxClientExisting = item.paxClientExisting,
                        paxClientPotential = item.paxClientPotential,
                        paxPrincipal = item.paxPrincipal
                    )
                )
            }
        }

        val claimRequest = SubmitClaimRequest(
            formDescription = appPref.getFormDesc().toString(),
            formTitle = appPref.getFormTitle().toString(),
            items =  submitItems,
        )
        Log.d("submitClaimRequestApi", "submitClaimRequestApi: " + claimRequest)
        if(appPref.getFormTitle()!!.isNotEmpty()) {
            viewmodel.submitClaim(claimRequest)
            ApiObeserver()
        }else{
            SnackbarUtils.showLongSnackbar(requireView(),"Enter Form Title")
        }
    }

    private fun ApiObeserver() {
        viewmodel.submitClaim.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is UIResult.Success -> {
                        Homeactivity.hideLoading()
                        appPref.setClaimRequest("")
                        appPref.setFormTitle("")
                        appPref.setFormDesc("")
                        SnackbarUtils.showLongSnackbar(
                            requireView(),
                            it.data?.message.toString(),

                        )
                        findNavController().popBackStack()
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

                    else -> {}
                }
            }
        }
    }

    inner class BottomSheetPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment {
            // Return the fragment for each page
            return when (position) {
                0 -> ClaimRequestTab1()
                1 -> ClaimRequestTabLayout2()
                // Add more cases for other fragments
                else -> ClaimRequestTab1()
            }
        }

        override fun getCount(): Int {
            // Return the number of pages
            return 2  // Adjust based on the number of fragments
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
