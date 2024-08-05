package com.worksy.hr.art.views.adapter.claimRequestListing

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.worksy.hr.art.Constants
import com.worksy.hr.art.databinding.ClaimPendingRequestDetailitemBinding
import com.worksy.hr.art.models.claimRequestResponse.GetClaimFormResponse
import com.worksy.hr.art.viewmodels.ClaimViewModel
import com.worksy.hr.art.views.fragments.claimRequest.CustomBottomSheetDialog

class ClaimRequestDetailAdapter(
    val context: Context,
    private var claimPendingList: MutableList<GetClaimFormResponse>,
    private var type: String,
    private var clickPosition: Int,
    private var navController: NavController?=null,
    private var claimReqViewModel: ClaimViewModel?=null,
    private var viewLifecycleOwner: LifecycleOwner?=null,
    private val requireActivity : FragmentActivity?=null,
    private var onViewDetailClick:((position:Int, requestData: GetClaimFormResponse.ClaimFormData.ClaimForm.PendingItem)->Unit)?=null,
    private val checkboxClickListener: ((position: Int, groupName: String) -> Unit)? = null
) : RecyclerView.Adapter<ClaimRequestDetailAdapter.ViewHolder>() {
    var isSelectionMode = false
    private val selectedItems: MutableSet<Int> = HashSet()

    interface DetailItemSelectedListener {
        fun onDetailItemSelected(position: Int, selected: Boolean)
    }

    inner class ViewHolder(val binding: ClaimPendingRequestDetailitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(progressList: GetClaimFormResponse) {
            binding.claimProgressList = progressList
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                binding.checkbox.isChecked = selectedItems.contains(position)
                binding.checkbox.visibility = if (isSelectionMode) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ClaimPendingRequestDetailitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(claimPendingList[0])
        holder.binding.apply {
            header.setOnClickListener {
                if(type == "Pending"){
                    val pendingItem = claimPendingList[0].data.form.pendingItems[clickPosition]
                    toggleSelection(position, pendingItem.items[position].id,pendingItem.groupName)
                    Log.d("ClickedItemRequest", "ClickedItemRequest"+pendingItem.items[position])
                    claimReqViewModel!!.setClickedItemRequest(pendingItem.items[position])
                    claimReqViewModel!!.setGroupName(pendingItem.groupName)
                    var customBottomSheetDialog=CustomBottomSheetDialog(
                        context=context,
                        requestEditBtn = false,
                        requestEditFromDetail = true,
                        viewModel = claimReqViewModel!!,
                        progressData = null,
                        itemData = null,
                        viewLifecycleOwner = viewLifecycleOwner!!,
                        null,null,requireActivity!!
                    ).apply {

                    }
                    customBottomSheetDialog.show()
                }
               /* toggleSelectionMode()
                true*/
            }
            holder.binding.checkbox.setOnClickListener {
                if (position != RecyclerView.NO_POSITION) {
                    val pendingItem = claimPendingList[0].data.form.pendingItems[clickPosition]
                    toggleSelection(position, pendingItem.items[position].id,pendingItem.groupName)
                    Log.d("ClickedItemRequest", "ClickedItemRequest"+pendingItem.items[position])
                    claimReqViewModel?.setClickedItemRequest(pendingItem.items[position])
                    claimReqViewModel?.setGroupName(pendingItem.groupName)
                }
            }
            if(type == "Pending"){
                if(!claimPendingList[0].data.form.pendingItems.isNullOrEmpty()){
                    var claimDetailData = claimPendingList[0].data.form.pendingItems[clickPosition].items[position]
                    userName.text = claimDetailData.claimItem
                    rmValue.text = "MYR " + claimDetailData.requestedAmount
                    dateTxt.text = claimDetailData.transactionDate
                    totalClaim.text = claimDetailData.tagName

                    var remark = claimDetailData.remarks
                    if (!claimDetailData.paxStaff.isNullOrEmpty())
                        remark += "\nPax (Staff): " + claimDetailData.paxStaff

                    if (!claimDetailData.paxClientExisting.isNullOrEmpty())
                        remark += "\nPax (Client Existing): " + claimDetailData.paxClientExisting

                    if (!claimDetailData.paxClientPotential.isNullOrEmpty())
                        remark += "\nPax (Client Potential): " + claimDetailData.paxClientPotential

                    if (!claimDetailData.paxPrincipal.isNullOrEmpty())
                        remark += "\nPax (Principal): " + claimDetailData.paxPrincipal

                    if (!claimDetailData.approval.isNullOrEmpty()) {
                        var levelCount = 1
                        for (level in claimDetailData.approval) {
                            remark += "\nLevel " + levelCount.toString() + " " + level.employees + " " + level.status
                            levelCount++
                        }
                    }

                    desc.text = remark

                    try {
                        Glide.with(context).load(Constants.IMG_PLACEHOLDER).into(image1)
                    } catch (e: Exception) {
                        Log.d("ImgException", "ImgException: " + e.message)
                    }
                    val rvAdapter = ClaimImageAdapter(context, claimPendingList)
                    imageRecyclerview.layoutManager = LinearLayoutManager(context)
                    imageRecyclerview.adapter = rvAdapter
                }
                }
            else if(type == "Approve"){
                 if(!claimPendingList[0].data.form.approvedItems.isNullOrEmpty()){
                    var claimDetailData = claimPendingList[0].data.form.approvedItems[clickPosition].items[position]
                    userName.text = claimDetailData.claimItem
                    rmValue.text = "MYR " + claimDetailData.requestedAmount
                    dateTxt.text = claimDetailData.transactionDate
                    totalClaim.text = claimDetailData.tagName

                    var remark = claimDetailData.remarks
                    if (!claimDetailData.paxStaff.isNullOrEmpty())
                        remark += "\nPax (Staff): " + claimDetailData.paxStaff

                    if (!claimDetailData.paxClientExisting.isNullOrEmpty())
                        remark += "\nPax (Client Existing): " + claimDetailData.paxClientExisting

                    if (!claimDetailData.paxClientPotential.isNullOrEmpty())
                        remark += "\nPax (Client Potential): " + claimDetailData.paxClientPotential

                    if (!claimDetailData.paxPrincipal.isNullOrEmpty())
                        remark += "\nPax (Principal): " + claimDetailData.paxPrincipal

                    if (!claimDetailData.approval.isNullOrEmpty()) {
                        var levelCount = 1
                        for (level in claimDetailData.approval) {
                            remark += "\nLevel " + levelCount.toString() + " " + level.employees + " " + level.status
                            levelCount++
                        }
                    }

                    desc.text = remark

                    try {
                        Glide.with(context).load(Constants.IMG_PLACEHOLDER).into(image1)
                    } catch (e: Exception) {
                        Log.d("ImgException", "ImgException: " + e.message)
                    }
                     val rvAdapter = ClaimImageAdapter(context, claimPendingList)
                    imageRecyclerview.layoutManager = LinearLayoutManager(context)
                    imageRecyclerview.adapter = rvAdapter
                }
            }
            else if(type == "Reject"){
                if(!claimPendingList[0].data.form.rejectedItems.isNullOrEmpty()){
                    var claimDetailData = claimPendingList[0].data.form.rejectedItems[clickPosition].items[position]
                    userName.text = claimDetailData.claimItem
                    rmValue.text = "MYR " + claimDetailData.requestedAmount
                    dateTxt.text = claimDetailData.transactionDate
                    totalClaim.text = claimDetailData.tagName

                    var remark = claimDetailData.remarks
                    if (!claimDetailData.paxStaff.isNullOrEmpty())
                        remark += "\nPax (Staff): " + claimDetailData.paxStaff

                    if (!claimDetailData.paxClientExisting.isNullOrEmpty())
                        remark += "\nPax (Client Existing): " + claimDetailData.paxClientExisting

                    if (!claimDetailData.paxClientPotential.isNullOrEmpty())
                        remark += "\nPax (Client Potential): " + claimDetailData.paxClientPotential

                    if (!claimDetailData.paxPrincipal.isNullOrEmpty())
                        remark += "\nPax (Principal): " + claimDetailData.paxPrincipal

                    if (!claimDetailData.approval.isNullOrEmpty()) {
                        var levelCount = 1
                        for (level in claimDetailData.approval) {
                            remark += "\nLevel " + levelCount.toString() + " " + level.employees + " " + level.status
                            levelCount++
                        }
                    }

                    desc.text = remark

                    try {
                        Glide.with(context).load(Constants.IMG_PLACEHOLDER).into(image1)
                    } catch (e: Exception) {
                        Log.d("ImgException", "ImgException: " + e.message)
                    }
                    val rvAdapter = ClaimImageAdapter(context, claimPendingList)
                    imageRecyclerview.layoutManager = LinearLayoutManager(context)
                    imageRecyclerview.adapter = rvAdapter
                }
            }
            }

    }
    private fun toggleSelection(position: Int, itemId: String, groupName: String) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(position)
        } else {
            selectedItems.clear()
            selectedItems.add(position)
        }
        notifyDataSetChanged()
    }

    fun setSelectedItems(isSelected: Boolean, selectedId: Boolean) {
        if (isSelected) {
            for (i in claimPendingList.indices) {
                selectedItems.add(i)
            }
        } else {
            selectedItems.clear()
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (type == "Pending") {
            claimPendingList[0].data.form.pendingItems[clickPosition].items.size
        } else if (type == "Approve") {
            claimPendingList[0].data.form.approvedItems[clickPosition].items.size
        } else {
            claimPendingList[0].data.form.rejectedItems[clickPosition].items.size
        }
    }
}
