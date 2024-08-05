package com.worksy.hr.art.models.claimApproval

data class ClaimPendingResponse(
    var count:String,var titleTxt:String,var rmValue:String,var isSelected: Boolean = false,val details: MutableList<ClaimPendingDetailResponse>
)