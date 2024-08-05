package com.worksy.hr.art.views.adapter.claimRequestListing

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.ClaimRequestPendingItemBinding
import com.worksy.hr.art.models.claimRequestResponse.GetClaimFormResponse
import com.worksy.hr.art.viewmodels.ClaimViewModel

class ClaimRequestPendingAdapter(
    val context: Context,
    private var progressList: MutableList<GetClaimFormResponse>,
    private val type: String,
    private val navController: NavController,
    private val claimReqViewModel: ClaimViewModel?=null,
    private val viewLifecycleOwner: LifecycleOwner,
    private val requireActivity : FragmentActivity?=null,
    private var onViewDetailClick: ((position: Int) -> Unit)?=null
) : RecyclerView.Adapter<ClaimRequestPendingAdapter.ViewHolder>(),ClaimRequestDetailAdapter.DetailItemSelectedListener {
    var selectionModeListener: SelectionModeListener? = null
    var isSelectionMode = false
    private val selectedItems: MutableSet<Int> = HashSet()

    interface SelectionModeListener {
        fun onSelectionCount(selectedCount: Int, selectedID: String)
    }

    inner class ViewHolder(val binding: ClaimRequestPendingItemBinding) :
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
            binding.rmValueTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
        }

        fun bind(progressListData: GetClaimFormResponse) {
            binding.claimProgressList = progressListData
            val position = adapterPosition
            binding.lytRecycler.visibility = if (isLytRecyclerVisible) View.VISIBLE else View.GONE
            binding.bottomView.visibility = if (isLytRecyclerVisible) View.VISIBLE else View.GONE

            // Set up child adapter
            val rvAdapter = ClaimRequestDetailAdapter(context, progressList,type,position,navController,claimReqViewModel,viewLifecycleOwner,requireActivity)
            rvAdapter.isSelectionMode = isSelectionMode
          /*  rvAdapter.setSelectedItems(
                selectedItems.contains(position),
                viewmodel.requestselectedIds.value?.contains(progressListData.data.form.id) == true
            )*/
            binding.recyclerview.layoutManager = LinearLayoutManager(context)
            binding.recyclerview.adapter = rvAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ClaimRequestPendingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(progressList[0])
        holder.binding.apply {
            if (progressList[0].data.form.pendingItems.isNotEmpty()) {
                val getForm = progressList[0].data.form.pendingItems[position]
                totalTxt.text = getForm.groupName
                countTxt.text = getForm.itemCount
                rmValueTxt.text = getForm.currency + " " + getForm.total

                val rvAdapter = ClaimRequestDetailAdapter(
                    context, progressList, type,position,navController,claimReqViewModel,viewLifecycleOwner,requireActivity,
                    onViewDetailClick = { position, requestData ->
                        Log.d("AdapterRequestData", "AdapterRequestData: "+requestData)
                    },
                ){childPosition,groupName-> }
                rvAdapter.isSelectionMode = isSelectionMode
                recyclerview.layoutManager = LinearLayoutManager(context)
                recyclerview.adapter = rvAdapter

            }
        }
    }
    override fun getItemCount(): Int = progressList[0].data.form.pendingItems.size
    override fun onDetailItemSelected(position: Int, selected: Boolean) {
        notifyItemChanged(position)
    }
}
