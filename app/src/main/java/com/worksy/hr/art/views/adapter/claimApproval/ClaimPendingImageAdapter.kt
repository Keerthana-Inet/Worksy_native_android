package com.worksy.hr.art.views.adapter.claimApproval

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.ApprovalMultipleImageItemBinding
import com.worksy.hr.art.models.claimApproval.ClaimImageResponse

class ClaimPendingImageAdapter(
    val context: Context,
    private val approverFlowItems: List<ClaimImageResponse>
) : RecyclerView.Adapter<ClaimPendingImageAdapter.ViewHolder>() {
    var isSelectionMode = false
    inner class ViewHolder(val binding: ApprovalMultipleImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ClaimImageResponse) {
            binding.approverImagesContainer.removeAllViews()

            val imageSize = 100
            val overlapMargin = -30
            val strokeColor = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.green_dark)
            )
            val groupLayout = LinearLayout(context)
            groupLayout.orientation = LinearLayout.HORIZONTAL

            for ((index, groupImageUrl) in item.approverImages.withIndex()) {
                val frameLayout = LinearLayout(context)
                frameLayout.orientation = LinearLayout.VERTICAL
                val layoutParams = LinearLayout.LayoutParams(
                    imageSize,
                    imageSize
                )
                if (index > 0) {
                    layoutParams.setMargins(overlapMargin, 0, 0, 0)
                } else {
                    layoutParams.setMargins(0, 0, 0, 0)
                }
                frameLayout.layoutParams = layoutParams

                // Create ShapeableImageView and add to FrameLayout
                val groupImageView = ShapeableImageView(context)
                groupImageView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                groupImageView.scaleType = ImageView.ScaleType.CENTER_CROP

                val shapeAppearanceModel = groupImageView.shapeAppearanceModel.toBuilder()
                    .setAllCornerSizes(50f) // For rounded corners
                    .build()
                groupImageView.shapeAppearanceModel = shapeAppearanceModel

                // Load image using Glide
                Glide.with(context)
                    .load(groupImageUrl)
                    .into(groupImageView)

                // Add ShapeableImageView to LinearLayout
                frameLayout.addView(groupImageView)

                // Add green stroke border
                val strokeWidth = 2 // Border width in pixels
                groupImageView.strokeWidth = strokeWidth.toFloat()
                groupImageView.strokeColor = ColorStateList.valueOf(
                    ContextCompat.getColor(context, item.approveColor[index])
                )
                groupImageView.setPadding(strokeWidth, strokeWidth, strokeWidth, strokeWidth)

                // Add LinearLayout to groupLayout
                groupLayout.addView(frameLayout)
            }

            // Add groupLayout to the container
            binding.approverImagesContainer.addView(groupLayout)

            // Add arrows and individual images
            for ((index, individualImageUrl) in item.approverImages.withIndex()) {

                val arrowImageView = ImageView(context)
                arrowImageView.setImageResource(R.drawable.right_arrow_icon)
                arrowImageView.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER_VERTICAL
                    leftMargin = 5
                    rightMargin = 5
                }
                arrowImageView.setPadding(15, 0, 35, 0)
                binding.approverImagesContainer.addView(arrowImageView)


                val frameLayout = LinearLayout(context)
                frameLayout.orientation = LinearLayout.VERTICAL
                val layoutParams = LinearLayout.LayoutParams(
                    imageSize,
                    imageSize
                )
                layoutParams.setMargins(overlapMargin, 0, 0, 0)
                frameLayout.layoutParams = layoutParams

                // Create ShapeableImageView and add to FrameLayout
                val individualImageView = ShapeableImageView(context)
                individualImageView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                individualImageView.scaleType = ImageView.ScaleType.CENTER_CROP

                val individualShapeAppearanceModel = individualImageView.shapeAppearanceModel.toBuilder()
                    .setAllCornerSizes(50f) // For rounded corners
                    .build()
                individualImageView.shapeAppearanceModel = individualShapeAppearanceModel

                // Load image using Glide
                Glide.with(context)
                    .load(individualImageUrl)
                    .into(individualImageView)

                // Add ShapeableImageView to LinearLayout
                frameLayout.addView(individualImageView)

                // Add green stroke border

                individualImageView.strokeWidth = 2F
                individualImageView.strokeColor = ColorStateList.valueOf(
                    ContextCompat.getColor(context, item.approveColor[index])
                )
                individualImageView.setPadding(
                    2,
                    2,
                    2,
                    2
                )
                // Add LinearLayout to container
                binding.approverImagesContainer.addView(frameLayout)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ApprovalMultipleImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(approverFlowItems[position])
    }

    override fun getItemCount(): Int = approverFlowItems.size
}
