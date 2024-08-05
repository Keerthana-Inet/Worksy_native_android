package com.worksy.hr.art.models.claimApproval

data class ClaimPendingDetailResponse (var titleTxt:String,var rmValue: String,var date:String,var name:String,var image:Int,var desc:String,var isSelected: Boolean = false)