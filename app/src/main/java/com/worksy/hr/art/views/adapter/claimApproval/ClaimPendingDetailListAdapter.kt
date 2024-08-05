package com.worksy.hr.art.views.adapter.claimApproval

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.worksy.hr.art.Constants
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.ClaimPendingDetailistItemBinding
import com.worksy.hr.art.models.claimApproval.ApproverRemarkListResponse
import com.worksy.hr.art.models.claimApproval.ClaimApprovalDetailResponse
import com.worksy.hr.art.models.claimApproval.ClaimImageResponse
import com.worksy.hr.art.utils.CommonUtils
import java.io.File

class ClaimPendingDetailListAdapter(
    val context: Context,
    private var claimPendingList: MutableList<ClaimApprovalDetailResponse>,
    private var claimApprovlList: ClaimApprovalDetailResponse.DetailData.DetailForm.ApprovedItem ?=null,
    private val type:String,
    private val clickPosition:Int,
    private var onViewDetailClick: (position: Int) -> Unit,
    private val requestStoragePermission: (() -> Unit)? = null
) : RecyclerView.Adapter<ClaimPendingDetailListAdapter.ViewHolder>() {

    var isSelectionMode = false
    private val selectedItems: MutableSet<Int> = HashSet()


    inner class ViewHolder(val binding: ClaimPendingDetailistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.checkbox.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val pendingItem = claimPendingList[0].data.form.pendingItems[position]
                    if (pendingItem.items.isNotEmpty()) {
                        val itemId = pendingItem.items[0].id
                        toggleSelection(position, itemId)
                    }
                }
            }
        }

        private fun toggleSelection(position: Int, itemId: String) {
            if (selectedItems.contains(position)) {
                selectedItems.remove(position)
            } else {
                selectedItems.add(position)
            }
            notifyItemChanged(position)
        }

        fun bind(progressList: ClaimApprovalDetailResponse) {
            binding.claimProgressList = progressList
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                binding.checkbox.isChecked = selectedItems.contains(position)
                binding.checkbox.visibility = if (isSelectionMode) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ClaimPendingDetailistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(claimPendingList[0])
        holder.binding.apply {
            lytRecycler.setOnLongClickListener {
                toggleSelectionMode()
                true
            }

            if(type == "Pending") {
                if (claimPendingList[0].data.form.pendingItems.isNotEmpty()) {
                    val claimDetailData =
                        claimPendingList[0].data.form.pendingItems[clickPosition].items[position]
                    val groupData = claimPendingList[0].data.form.pendingItems[clickPosition]
                    userName.text = claimDetailData.claimItem
                    rmValue.text = groupData.currency + " " + claimDetailData.requestedAmount
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
                        var levelCount = 1;
                        for (level in claimDetailData.approval) {
                            remark += "\nApprover " + levelCount.toString() + " " + level.employees + " " + level.status
                            levelCount++
                        }
                    }

                    desc.text = remark

                    // Check if the image file exists locally
                    val fileName = "downloaded_image_${position}.jpg"
                    val filePath =
                        context.getExternalFilesDir(null)?.absolutePath + File.separator + fileName
                    val imageFile = File(filePath)
                    if (imageFile.exists()) {
                        // Image file exists locally, load it

                        Glide.with(context)
                            .load(imageFile)
                            .into(image1)
                    } else {
                        // Image file does not exist, load placeholder and set click listener to download
                        Glide.with(context)
                            .load(Constants.IMG_PLACEHOLDER)
                            .into(image1)

                        image1.setOnClickListener {
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                val imageUrl = Constants.IMG_PLACEHOLDER
                                CommonUtils.downloadImage(context, imageUrl, fileName)
                            } else {
                                requestStoragePermission?.invoke()
                            }
                        }
                    }
                    multipleImgType.visibility=View.VISIBLE
                    val approverFlowItems = listOf(
                        ClaimImageResponse(
                            approverImages = listOf(
                                "https://imgur.com/JRSDvNN.png",
                                "https://imgur.com/jBYiaXK.png",
                                "https://via.placeholder.com/150",
                                "https://imgur.com/jBYiaXK.png"
                            ),
                            approveColor = listOf(R.color.green_dark,R.color.yellow,R.color.red,R.color.light_yellow)
                        )
                    )

                    val rvAdapter = ClaimPendingImageAdapter(context, approverFlowItems)
                    imageRecyclerview.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    imageRecyclerview.adapter = rvAdapter
                }
            }
            else if(type == "Approve"){
                if (claimPendingList[0].data.form.approvedItems.isNotEmpty()) {
                    val claimDetailData =
                        claimPendingList[0].data.form.approvedItems[clickPosition].items[position]
                    val groupData = claimPendingList[0].data.form.approvedItems[clickPosition]
                    userName.text = claimDetailData.claimItem
                    rmValue.text = groupData.currency + " " + claimDetailData.requestedAmount
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
                            remark += "\nApprover " + levelCount.toString() + " " + level.employees + " " + level.status
                            levelCount++
                        }
                    }

                    desc.text = remark

                    // Check if the image file exists locally
                    val fileName = "downloaded_image_${position}.jpg"
                    val filePath = context.getExternalFilesDir(null)?.absolutePath + File.separator + fileName
                    val imageFile = File(filePath)
                    if (imageFile.exists()) {
                        // Image file exists locally, load it
                        Glide.with(context)
                            .load(imageFile)
                            .into(image1)
                    } else {
                        // Image file does not exist, load placeholder and set click listener to download
                        Glide.with(context)
                            .load(Constants.IMG_PLACEHOLDER)
                            .into(image1)

                        image1.setOnClickListener {
                            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                val imageUrl = Constants.IMG_PLACEHOLDER
                                CommonUtils.downloadImage(context, imageUrl, fileName)
                            } else {
                                requestStoragePermission?.invoke()
                            }
                        }
                    }

                    multipleImgType.visibility = View.GONE
                    val approverFlowItems = listOf(
                        ClaimImageResponse(
                            approverImages = listOf(
                                "https://imgur.com/JRSDvNN.png",
                                "https://imgur.com/jBYiaXK.png",
                                "https://via.placeholder.com/150",
                                "https://imgur.com/jBYiaXK.png"
                            ),
                            approveColor = listOf(R.color.green_dark, R.color.yellow, R.color.red, R.color.light_yellow)
                        )
                    )
                    val rvAdapter = ClaimPendingImageAdapter(context, approverFlowItems)
                    imageRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    imageRecyclerview.adapter = rvAdapter

                    val approverRemark = listOf(
                        ApproverRemarkListResponse(
                            "https://imgur.com/JRSDvNN.png",
                            R.color.green_dark, "No entitlement"
                        ),
                        ApproverRemarkListResponse(
                            "https://imgur.com/jBYiaXK.png",
                            R.color.yellow, "No entitlement"
                        ),
                    )
                    val approveAdapter = ApproverRemarkListAdapter(context, approverRemark)
                    approverRecyclerview.layoutManager = LinearLayoutManager(context)
                    approverRecyclerview.adapter = approveAdapter
                }
            }
            else if(type == "Reject"){
                if (claimPendingList[0].data.form.rejectedItems.isNotEmpty()) {
                    val claimDetailData =
                        claimPendingList[0].data.form.rejectedItems[clickPosition].items[position]
                    val groupData = claimPendingList[0].data.form.rejectedItems[clickPosition]
                    userName.text = claimDetailData.claimItem
                    rmValue.text = groupData.currency + " " + claimDetailData.requestedAmount
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
                        var levelCount = 1;
                        for (level in claimDetailData.approval) {
                            remark += "\nApprover " + levelCount.toString() + " " + level.employees + " " + level.status
                            levelCount++
                        }
                    }

                    desc.text = remark

                    // Check if the image file exists locally
                    val fileName = "downloaded_image_${position}.jpg"
                    val filePath =
                        context.getExternalFilesDir(null)?.absolutePath + File.separator + fileName
                    val imageFile = File(filePath)
                    if (imageFile.exists()) {
                        // Image file exists locally, load it
                        Glide.with(context)
                            .load(imageFile)
                            .into(image1)
                    } else {
                        // Image file does not exist, load placeholder and set click listener to download
                        Glide.with(context)
                            .load(Constants.IMG_PLACEHOLDER)
                            .into(image1)

                        image1.setOnClickListener {
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                val imageUrl = Constants.IMG_PLACEHOLDER
                                CommonUtils.downloadImage(context, imageUrl, fileName)
                            } else {
                                requestStoragePermission?.invoke()
                            }
                        }
                    }
                    multipleImgType.visibility=View.GONE
                    val approverFlowItems = listOf(
                        ClaimImageResponse(
                            approverImages = listOf(
                                "https://imgur.com/JRSDvNN.png",
                                "https://imgur.com/jBYiaXK.png",
                                "https://via.placeholder.com/150",
                                "https://imgur.com/jBYiaXK.png"
                            ),
                            approveColor = listOf(R.color.green_dark,R.color.yellow,R.color.red,R.color.light_yellow)
                        )
                    )
                    val rvAdapter = ClaimPendingImageAdapter(context, approverFlowItems)
                    imageRecyclerview.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    imageRecyclerview.adapter = rvAdapter


                    val approverRemark = listOf(
                        ApproverRemarkListResponse(
                            "https://imgur.com/JRSDvNN.png",
                            R.color.green_dark,"No entitlement"
                        ),
                        ApproverRemarkListResponse(
                            "https://imgur.com/jBYiaXK.png",
                            R.color.yellow,"No entitlement"
                        ),
                    )
                    val approveAdapter = ApproverRemarkListAdapter(context, approverRemark)
                    approverRecyclerview.layoutManager = LinearLayoutManager(context)
                    approverRecyclerview.adapter = approveAdapter
                }
            }
        }
    }

    private fun toggleSelectionMode() {
        isSelectionMode = !isSelectionMode
        if (!isSelectionMode) deselectAll()
        notifyDataSetChanged()
    }




    override fun getItemCount(): Int {
        return if (type == "Pending") {
            claimPendingList[0].data.form.pendingItems[clickPosition].items.size
        } else if(type == "Approve"){
            claimPendingList[0].data.form.approvedItems[clickPosition].items.size
        }else {
            claimPendingList[0].data.form.rejectedItems[clickPosition].items.size
        }
    }

    fun deselectAll() {
        selectedItems.clear()
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

}
