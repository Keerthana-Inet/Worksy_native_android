    package com.worksy.hr.art.views.fragments.claimRequest

    import android.content.Context
    import android.content.res.ColorStateList
    import android.util.AttributeSet
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.widget.LinearLayout
    import android.widget.Toast
    import androidx.core.content.ContextCompat
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.FragmentActivity
    import androidx.lifecycle.LifecycleOwner
    import androidx.lifecycle.Observer
    import androidx.viewpager2.adapter.FragmentStateAdapter
    import com.google.android.material.tabs.TabLayout
    import com.google.android.material.tabs.TabLayoutMediator
    import com.google.gson.Gson
    import com.worksy.hr.art.R
    import com.worksy.hr.art.databinding.FragmentClaimApprovalDetailBinding
    import com.worksy.hr.art.databinding.FragmentClaimRequestTabDetailBinding
    import com.worksy.hr.art.databinding.ViewCustomTabLayoutBinding
    import com.worksy.hr.art.models.claimRequestResponse.GetClaimFormResponse
    import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
    import com.worksy.hr.art.utils.SnackbarUtils
    import com.worksy.hr.art.utils.UIResult
    import com.worksy.hr.art.viewmodels.ClaimViewModel
    import com.worksy.hr.art.views.activity.Homeactivity

    class CustomTabLayoutView @JvmOverloads constructor(
        context: Context,attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : LinearLayout(context, attrs, defStyleAttr) {

        private var binding: ViewCustomTabLayoutBinding =
            ViewCustomTabLayoutBinding.inflate(LayoutInflater.from(context), this)
        private var tabTitles: List<String> = emptyList()
        private var fragments: List<Fragment> = emptyList()
        private var pendingCountValue: String = ""
        private var completedCountValue: String = ""
        private var approvalDetailPending: String = ""
        private var approvalDetailApprove: String = ""
        private var approvalDetailReject: String = ""
        private var approvalDetail:Boolean=false
        var claimDataList: List<GetClaimFormResponse.ClaimFormData.ClaimForm.PendingItem> = mutableListOf()
        init {
            orientation = VERTICAL
        }
        fun setupWithViewModel(viewModel: ClaimViewModel, lifecycleOwner: LifecycleOwner,fragmentActivity: FragmentActivity,tabTitles: List<String>, fragments: List<Fragment>,binding: FragmentClaimApprovalDetailBinding?=null,
        requestDetailBinding:FragmentClaimRequestTabDetailBinding?=null) {
            this.tabTitles = tabTitles
            this.fragments = fragments
            viewModel.pendingCount.observe(lifecycleOwner, Observer { pendingCount ->
                pendingCountValue = pendingCount.toString()
                Log.d("PendingCountValue", "PendingCountValue: $pendingCount")
                updateTabTitles()
            })

            viewModel.completedCount.observe(lifecycleOwner, Observer { completedCount ->
                completedCountValue = completedCount.toString()
                Log.d("completedCountValue", "completedCountValue: $completedCount")
                updateTabTitles()
            })

            viewModel.pendingApprovalCount.observe(lifecycleOwner, Observer { pendingCount ->
                pendingCountValue = pendingCount.toString()
                Log.d("PendingApprovalCountValue", "PendingApprovalCountValue: $pendingCountValue")
                updateTabTitles()
            })

            viewModel.completedApprovalCount.observe(lifecycleOwner, Observer { completedCount ->
                completedCountValue = completedCount.toString()
                Log.d("completedApprovalCountValue", "completedApprovalCountValue: $completedCountValue")
                updateTabTitles()
            })
            if (binding != null) {
                setupTabLayout(binding)
            }
            if (requestDetailBinding != null) {
                setupRequestTabLayout(requestDetailBinding)
            }
            setUpViewPager(fragmentActivity)
        }
        fun claimRequestDetailApi(
            viewmodel: ClaimViewModel,
            viewLifecycleOwner: LifecycleOwner,
            requireActivity: FragmentActivity,
            formId: String,
            appPref: SharedPrefManager,
            binding: FragmentClaimRequestTabDetailBinding
        ){
            viewmodel.getClaimFormDetailList(formId!!)
            viewmodel.getClaimForm.observe(viewLifecycleOwner) {
                if (it != null) {
                    when (it) {
                        is UIResult.Success -> {
                            Homeactivity.hideLoading()
                            it.data?.let { claimData->
                                Log.d("JSONDAta", "JSONDAta: "+claimData)
                                approvalDetailPending=claimData.data.form.totalPending
                                approvalDetailReject=claimData.data.form.totalRejected
                                approvalDetailApprove=claimData.data.form.totalApproved
                                binding.apply {
                                   binding.commonProfile.userName.text=claimData.data.form.requesterName
                                   binding.commonProfile.userPosition.text=claimData.data.form.requesterPosition
                                   binding.commonProfile.status.text=claimData.data.form.status
                                    itemCountTxt.text=claimData.data.form.totalItems
                                    rmValueTxt.text=claimData.data.form.currency+" "+claimData.data.form.total
                                    claimDataList=claimData.data.form.pendingItems
                                    if(!claimData.data.form.approval.isNullOrEmpty()) {
                                        var approvalTxt = "DAG Exceed Limit Approval"
                                        var levelCount = 1;
                                        for(level in claimData.data.form.approval) {
                                            approvalTxt += "\nApprover " + levelCount.toString() + " - " + level.employees + " (" + level.status + ")"
                                            levelCount++
                                        }
                                        formApprovalTxt.text = approvalTxt
                                    }
                                    else {
                                        if(binding.commonProfile.status.text === "pending")
                                            formApprovalTxt.text = "Pending Claim Approval"
                                        else
                                            formApprovalTxt.text = "Form within Limit"
                                    }
                                }
                                approvalDetail=true
                                val gson = Gson()
                                val jsonString = gson.toJson(claimData)
                                viewmodel.jsonString=jsonString
                                setupRequestTabLayout(binding)
                                setUpViewPager(requireActivity)
                            }

                        }

                        is UIResult.Error -> {
                            Homeactivity.hideLoading()
                            setupRequestTabLayout(binding)
                            setUpViewPager(requireActivity)
                           Toast.makeText(requireActivity,it.message.toString(),Toast.LENGTH_SHORT).show()
                        }

                        is UIResult.Loading -> {
                            Homeactivity.showLoading()
                        }
                    }
                }
            }
        }

         fun claimDetailApi(
             viewmodel: ClaimViewModel,
             viewLifecycleOwner: LifecycleOwner,
             requireActivity: FragmentActivity,
             formId: String,
             appPref: SharedPrefManager,
             binding: FragmentClaimApprovalDetailBinding
         ) {
             appPref.setFormId(formId)
             viewmodel.getClaimApprovalDetailForm(formId)
             viewmodel.getClaimApprovalDetailForm.observe(viewLifecycleOwner) {
                 if (it != null) {
                     when (it) {
                         is UIResult.Success -> {
                             Homeactivity.hideLoading()
                             //when we click back the list item show opened
                             viewmodel.isListView=true
                             it.data?.let { claimData->
                                 Log.d("JSONDAta", "JSONDAta: "+claimData)
                                 approvalDetailPending=claimData.data.form.totalPending
                                 approvalDetailReject=claimData.data.form.totalRejected
                                 approvalDetailApprove=claimData.data.form.totalApproved


                                 binding.rejectBtn.text="Reject"
                                 binding.approveBtn.text="Approve"
                                 binding.apply {
                                     binding.commonProfile.userName.text=claimData.data.form.requesterName
                                     binding.commonProfile.userPosition.text=claimData.data.form.requesterPosition
                                     binding.commonProfile.status.text=claimData.data.form.status
                                     itemCountTxt.text=claimData.data.form.totalItems
                                     rmValueTxt.text=claimData.data.form.currency+" "+claimData.data.form.total
                                     if(claimData.data.form.pendingItems.isEmpty()){
                                         rejectBtn.visibility=View.GONE
                                         approveBtn.visibility=View.GONE
                                     }else{
                                         rejectBtn.visibility=View.VISIBLE
                                         approveBtn.visibility=View.VISIBLE
                                     }
                                     if(!claimData.data.form.approval.isNullOrEmpty()) {
                                         var approvalTxt = "Exceed Limit Approval"
                                         var levelCount = 1;
                                         for(level in claimData.data.form.approval) {
                                             approvalTxt += "\nApprover " + levelCount.toString() + " - " + level.employees + " (" + level.status + ")"
                                             levelCount++
                                         }
                                         formApprovalTxt.text = approvalTxt
                                     }
                                     else {
                                         if(binding.commonProfile.status.text === "pending")
                                             formApprovalTxt.text = "Pending Claim Approval"
                                         else
                                             formApprovalTxt.text = "Form within Limit"
                                     }
                                 }
                                 val gson = Gson()
                                 val ApprovalJsonString = gson.toJson(claimData)
                                 viewmodel.ApprovalJsonString=ApprovalJsonString
                                 approvalDetail=true
                                 setupTabLayout(binding)
                                 setUpViewPager(requireActivity)
                             }

                         }

                         is UIResult.Error -> {
                             Homeactivity.hideLoading()
                             setupTabLayout(binding)
                             setUpViewPager(requireActivity)
                            Toast.makeText(requireActivity,it.message.toString(),Toast.LENGTH_SHORT).show()
                         }

                         is UIResult.Loading -> {
                             Homeactivity.showLoading()
                         }
                     }
                 }
             }
        }

        private fun setUpViewPager(fragmentActivity: FragmentActivity) {
            binding.viewPager.apply {
                adapter = ViewPagerAdapter(fragmentActivity,fragments)
            }

            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = getFormattedTitle(tabTitles[position], position)
                if(approvalDetail){
                    tab.text = getApprovalDetailText(tabTitles[position], position)
                }
            }.attach()
        }

        private fun getApprovalDetailText(title: String, position: Int): String {
            return when (position) {
                0 -> if (approvalDetailPending.isEmpty()) title else "$title $approvalDetailPending"
                1 -> if (approvalDetailApprove.isEmpty()) title else "$title $approvalDetailApprove"
                2 -> if (approvalDetailReject.isEmpty()) title else "$title $approvalDetailReject"
                else -> title
            }
        }

        private fun getFormattedTitle(title: String, position: Int): String {
            return when (position) {
                0 -> if (pendingCountValue.isEmpty()) title else "$title $pendingCountValue"
                1 -> if (completedCountValue.isEmpty()) title else "$title $completedCountValue"
                else -> title
            }
        }
        private fun updateTabTitles() {
            tabTitles.forEachIndexed { index, title ->
                binding.tabLayout.getTabAt(index)?.text = getFormattedTitle(title, index)
            }
        }
        fun setupTabLayout(binding: FragmentClaimApprovalDetailBinding) {
            this.binding.tabLayout.apply {
                removeAllTabs()
                tabTitles.forEachIndexed { index, title ->
                    addTab(this.newTab().setText(getFormattedTitle(title, index)))
                }

                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        tab?.position?.let {
                            this@CustomTabLayoutView.binding.viewPager.currentItem = it
                        }
                        if(approvalDetail){
                            tab?.position?.let {
                                this@CustomTabLayoutView.binding.viewPager.currentItem = it
                                when (it) {
                                    0 -> {
                                        this@CustomTabLayoutView.binding.handTxt.visibility=View.GONE
                                        binding.claimLyt.visibility=View.VISIBLE
                                        binding.commonProfile.status.background = ContextCompat.getDrawable(context, R.drawable.blue_background_border)
                                        binding.commonProfile.status.backgroundTintList = ColorStateList.valueOf(
                                            ContextCompat.getColor(context, R.color.thin_blue))
                                        val drawable = ContextCompat.getDrawable(context, R.drawable.getting_ready_icon)
                                        drawable?.setTint(ContextCompat.getColor(context, R.color.button_txt)) // Change tint color here
                                       binding.commonProfile.status.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                            drawable, null, null, null
                                        )
                                        binding.commonProfile.status.setTextColor(ContextCompat.getColor(context, R.color.button_txt))
                                        binding.claimImg.setImageResource(R.drawable.donation_hand)
                                        binding.totalClaimTxt.text="Total Claim"
                                        binding.linearLyt.visibility=View.VISIBLE
                                    }
                                    1 -> {
                                        this@CustomTabLayoutView.binding.handTxt.visibility=View.GONE
                                        binding.claimLyt.visibility=View.VISIBLE
                                        binding.commonProfile.status.background = ContextCompat.getDrawable(context, R.drawable.blue_background_border)
                                        binding.commonProfile.status.backgroundTintList = ColorStateList.valueOf(
                                            ContextCompat.getColor(context, R.color.thin_blue))
                                        val drawable = ContextCompat.getDrawable(context, R.drawable.getting_ready_icon)
                                        drawable?.setTint(ContextCompat.getColor(context, R.color.button_txt)) // Change tint color here
                                        binding.commonProfile.status.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                            drawable, null, null, null
                                        )
                                        binding.commonProfile.status.setTextColor(ContextCompat.getColor(context, R.color.button_txt))
                                        binding.claimImg.setImageResource(R.drawable.donation_hand)
                                        binding.totalClaimTxt.text="Total Claim"
                                        binding.linearLyt.visibility=View.GONE
                                    }
                                    2 -> {
                                        binding.claimLyt.visibility=View.VISIBLE
                                        this@CustomTabLayoutView.binding.handTxt.visibility=View.GONE
                                        binding.commonProfile.status.background = ContextCompat.getDrawable(context, R.drawable.blue_background_border)
                                        binding.commonProfile.status.backgroundTintList = ColorStateList.valueOf(
                                            ContextCompat.getColor(context, R.color.thin_blue))
                                        val drawable = ContextCompat.getDrawable(context, R.drawable.getting_ready_icon)
                                        drawable?.setTint(ContextCompat.getColor(context, R.color.button_txt)) // Change tint color here
                                        binding.commonProfile.status.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                            drawable, null, null, null
                                        )
                                        binding.commonProfile.status.setTextColor(ContextCompat.getColor(context, R.color.button_txt))
                                        binding.claimImg.setImageResource(R.drawable.rejected_img)
                                        binding.totalClaimTxt.text="Rejected Claim"
                                        binding.linearLyt.visibility=View.GONE
                                    }

                                }
                            }
                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        //Nothing to do
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                        //Nothing to do
                    }
                })
            }
        }
        fun setupRequestTabLayout(binding: FragmentClaimRequestTabDetailBinding) {
            this.binding.tabLayout.apply {
                removeAllTabs()
                tabTitles.forEachIndexed { index, title ->
                    addTab(this.newTab().setText(getFormattedTitle(title, index)))
                }

                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        tab?.position?.let {
                            this@CustomTabLayoutView.binding.viewPager.currentItem = it
                        }
                        if(approvalDetail){
                            tab?.position?.let {
                                this@CustomTabLayoutView.binding.viewPager.currentItem = it
                                when (it) {
                                    0 -> {
                                        this@CustomTabLayoutView.binding.handTxt.visibility=View.GONE
                                        binding.claimLyt.visibility=View.VISIBLE
                                        binding.commonProfile.status.background = ContextCompat.getDrawable(context, R.drawable.blue_background_border)
                                        binding.commonProfile.status.backgroundTintList = ColorStateList.valueOf(
                                            ContextCompat.getColor(context, R.color.thin_blue))
                                        val drawable = ContextCompat.getDrawable(context, R.drawable.getting_ready_icon)
                                        drawable?.setTint(ContextCompat.getColor(context, R.color.button_txt)) // Change tint color here
                                        binding.commonProfile.status.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                            drawable, null, null, null
                                        )
                                        binding.commonProfile.status.setTextColor(ContextCompat.getColor(context, R.color.button_txt))
                                        binding.claimImg.setImageResource(R.drawable.donation_hand)
                                        binding.totalClaimTxt.text="Total Claim"
                                        binding.linearLyt.visibility=View.VISIBLE
                                    }
                                    1 -> {
                                        this@CustomTabLayoutView.binding.handTxt.visibility=View.GONE
                                        binding.claimLyt.visibility=View.VISIBLE
                                        binding.commonProfile.status.background = ContextCompat.getDrawable(context, R.drawable.blue_background_border)
                                        binding.commonProfile.status.backgroundTintList = ColorStateList.valueOf(
                                            ContextCompat.getColor(context, R.color.thin_blue))
                                        val drawable = ContextCompat.getDrawable(context, R.drawable.getting_ready_icon)
                                        drawable?.setTint(ContextCompat.getColor(context, R.color.button_txt)) // Change tint color here
                                        binding.commonProfile.status.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                            drawable, null, null, null
                                        )
                                        binding.commonProfile.status.setTextColor(ContextCompat.getColor(context, R.color.button_txt))
                                        binding.claimImg.setImageResource(R.drawable.donation_hand)
                                        binding.totalClaimTxt.text="Total Claim"
                                        binding.linearLyt.visibility=View.GONE
                                    }
                                    2 -> {
                                        binding.claimLyt.visibility=View.VISIBLE
                                        this@CustomTabLayoutView.binding.handTxt.visibility=View.GONE
                                        binding.commonProfile.status.background = ContextCompat.getDrawable(context, R.drawable.blue_background_border)
                                        binding.commonProfile.status.backgroundTintList = ColorStateList.valueOf(
                                            ContextCompat.getColor(context, R.color.thin_blue))
                                        val drawable = ContextCompat.getDrawable(context, R.drawable.getting_ready_icon)
                                        drawable?.setTint(ContextCompat.getColor(context, R.color.button_txt)) // Change tint color here
                                        binding.commonProfile.status.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                            drawable, null, null, null
                                        )
                                        binding.commonProfile.status.setTextColor(ContextCompat.getColor(context, R.color.button_txt))
                                        binding.claimImg.setImageResource(R.drawable.rejected_img)
                                        binding.totalClaimTxt.text="Rejected Claim"
                                        binding.linearLyt.visibility=View.GONE
                                    }

                                }
                            }
                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        //Nothing to do
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                        //Nothing to do
                    }
                })
            }
        }
        inner class ViewPagerAdapter(
            fragmentActivity: FragmentActivity,
            private val fragments: List<Fragment>
        ) : FragmentStateAdapter(fragmentActivity) {
            override fun getItemCount(): Int = fragments.size

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }
    }