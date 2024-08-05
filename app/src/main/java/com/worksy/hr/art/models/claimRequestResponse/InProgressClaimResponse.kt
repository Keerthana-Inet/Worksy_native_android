package com.worksy.hr.art.models.claimRequestResponse

data class InProgressClaimResponse (
    var claimType:String,var claimStatus:String,var claimTitle:String, var date:String, var claimSubTitle:String,var totalCount:String,var totalRMValue:String
)