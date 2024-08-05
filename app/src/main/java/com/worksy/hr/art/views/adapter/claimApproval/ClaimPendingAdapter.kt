package com.worksy.hr.art.views.adapter.claimApproval

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.ClaimapprovalPendingItemBinding
import com.worksy.hr.art.models.claimApproval.ClaimApprovalDetailResponse
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import com.worksy.hr.art.viewmodels.ClaimViewModel

class ClaimPendingAdapter(
    val context: Context,
    private val appPrefManager: SharedPrefManager,
    private var progressListData: MutableList<ClaimApprovalDetailResponse>,
    private val viewmodel : ClaimViewModel,
    private val type:String,
    private var onViewDetailClick: (position: Int) -> Unit,
    private val requestStoragePermission: (() -> Unit)? = null
) : RecyclerView.Adapter<ClaimPendingAdapter.ViewHolder>() {
    var selectionModeListener: SelectionModeListener? = null
    var isSelectionMode = false
        private val selectedItems: MutableSet<Int> = HashSet()


        interface SelectionModeListener {
        fun onSelectionModeChanged(isSelectionMode: Boolean)
        fun onSelectionCount(selectedCount: Int, selectedID:String)
    }

    inner class ViewHolder(val binding: ClaimapprovalPendingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var isLytRecyclerVisible: Boolean = false

        init {


            binding.rmValueTxt.setOnClickListener {
                toggleRecyclerVisibility()
            }
        }





        private fun toggleRecyclerVisibility() {
            isLytRecyclerVisible = !isLytRecyclerVisible
            binding.lytRecycler.visibility = if (isLytRecyclerVisible) View.VISIBLE else View.GONE
            binding.bottomView.visibility = if (isLytRecyclerVisible) View.VISIBLE else View.GONE
            val drawable = if (isLytRecyclerVisible) {
                ContextCompat.getDrawable(context, R.drawable.up_arrow)
            } else {
                ContextCompat.getDrawable(context, R.drawable.ic_down_arrow)?.apply {
                    setTint(ContextCompat.getColor(context, R.color.text_gray))
                }
            }
            binding.rmValueTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                drawable,
                null
            )
        }

        fun bind(progressList: ClaimApprovalDetailResponse) {
            binding.claimProgressList = progressList
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val pendingItem = progressList.data.form.pendingItems[position]
                binding.checkbox.isChecked =
                    selectedItems.contains(position) || (viewmodel.selectedIds.value?.contains(
                        pendingItem.items[0].id
                    ) == true)
                binding.checkbox.visibility = if (isSelectionMode) View.VISIBLE else View.GONE

//            // Set up child adapter
//            val rvAdapter = ClaimPendingDetailListAdapter(context, progressListData, position)
//            rvAdapter.isSelectionMode = isSelectionMode
//            rvAdapter.setSelectedItems(selectedItems.contains(position), viewmodel.selectedIds.value?.contains(progressList.data.form.id) == true)
//            binding.recyclerview.layoutManager = LinearLayoutManager(context)
//            binding.recyclerview.adapter = rvAdapter

                // Set up child adapter
                val rvAdapter = ClaimPendingDetailListAdapter(
                    context,
                    progressListData,
                    null,
                    type,
                    position,
                    onViewDetailClick,
                    requestStoragePermission
                )
                rvAdapter.isSelectionMode = isSelectionMode
                rvAdapter.setSelectedItems(
                    selectedItems.contains(position),
                    viewmodel.selectedIds.value?.contains(pendingItem.items[0].id) == true
                )
                binding.recyclerview.layoutManager = LinearLayoutManager(context)
                binding.recyclerview.adapter = rvAdapter

                binding.groupName.text = pendingItem.groupName
                binding.countTxt.text = pendingItem.itemCount
                binding.rmValueTxt.text = "${pendingItem.currency} ${pendingItem.total}"
                binding.lytRecycler.visibility =
                    if (isLytRecyclerVisible) View.VISIBLE else View.GONE
                binding.bottomView.visibility =
                    if (isLytRecyclerVisible) View.VISIBLE else View.GONE
            }
        }
    }
    private fun toggleSelection(
        position: Int,
        itemId: String,
        binding: ClaimapprovalPendingItemBinding
    ) {
        viewmodel.toggleSelection(itemId)
        Log.d("SelectedIds", "SelectedIds: ${viewmodel.selectedIds.value}")
        if (selectedItems.contains(position)) {
            selectedItems.remove(position)
        } else {
            selectedItems.add(position)
        }
        notifyItemChanged(position)
        selectionModeListener?.onSelectionCount(
            selectedItems.size,
            viewmodel.selectedIds.value.toString()
        )
        notifyChildAdapterOfSelection(
            binding,
            position,
            selectedItems.contains(position),
            viewmodel.selectedIds.value?.contains(itemId) == true
        )
    }
    fun selectAll() {
        selectedItems.clear()
        val selectedIds = mutableSetOf<String>()

        for (pendingItem in progressListData[0].data.form.pendingItems) {
            for (item in pendingItem.items) {
                selectedIds.add(item.id)
            }
        }

        selectedItems.addAll(progressListData[0].data.form.pendingItems.indices)
        notifyDataSetChanged()
        viewmodel.selectedIds.value = selectedIds
        selectionModeListener?.onSelectionCount(selectedItems.size, viewmodel.selectedIds.value.toString())
    }
    private fun notifyChildAdapterOfSelection(
        binding: ClaimapprovalPendingItemBinding,
        position: Int,
        isSelected: Boolean,
        selectedId: Boolean
    ) {
        val rvAdapter = binding.recyclerview.adapter as? ClaimPendingDetailListAdapter
        rvAdapter?.setSelectedItems(isSelected, selectedId)
    }

    fun deselectAll() {
        selectedItems.clear()
        viewmodel.clearSelections()
        notifyDataSetChanged()
        selectionModeListener?.onSelectionCount(selectedItems.size,viewmodel.selectedIds.value.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ClaimapprovalPendingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(progressListData[0])
        holder.binding.apply {
            holder.binding.checkbox.setOnClickListener {
                if (position != RecyclerView.NO_POSITION) {
                    val pendingItem = progressListData[0].data.form.pendingItems[position]
                    if (pendingItem.items.isNotEmpty()) {
                        val itemId = pendingItem.items[0].id
                        toggleSelection(position, itemId,holder.binding)
                    }
                }
            }
            holder.binding.header.setOnLongClickListener {
                toggleSelectionMode()
                true
            }
            if (progressListData[0].data.form.pendingItems.isNotEmpty()) {
               var progressData=progressListData[0].data.form.pendingItems[position]
                val getForm = progressListData[0].data.form.pendingItems[position]
                appPrefManager.setPendingTotalAmount(progressData.total)
                appPrefManager.setPendingId(progressData.items[0].id)
                groupName.text = getForm.groupName
                countTxt.text = getForm.itemCount
                rmValueTxt.text = getForm.currency + " " + getForm.total

                if(type.equals("Pending")) {
                    if (progressListData[0].data.form.pendingItems[position].items.isNotEmpty()) {
                       detailAdapter(position,recyclerview,"Pending")
                    }
                }
            }
        }
    }

    private fun detailAdapter(position: Int, recyclerview: RecyclerView,type:String) {
        val rvAdapter = ClaimPendingDetailListAdapter(
            context,
            progressListData,
            null,
            type,
            position,
            onViewDetailClick,
            requestStoragePermission
        )
        rvAdapter.isSelectionMode = isSelectionMode
        recyclerview.layoutManager = LinearLayoutManager(context)
        recyclerview.adapter = rvAdapter
    }

    private fun toggleSelectionMode() {
        isSelectionMode = !isSelectionMode
        selectionModeListener?.onSelectionModeChanged(isSelectionMode)
        if (!isSelectionMode) deselectAll()
        notifyDataSetChanged()
        selectionModeListener?.onSelectionCount(selectedItems.size,viewmodel.selectedIds.value.toString())
    }


    override fun getItemCount(): Int = progressListData[0].data.form.pendingItems.size
}
