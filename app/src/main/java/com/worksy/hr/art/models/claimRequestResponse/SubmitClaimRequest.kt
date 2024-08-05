package com.worksy.hr.art.models.claimRequestResponse

data class SubmitClaimRequest(
    val formDescription: String,
    val formTitle: String,
    val items: MutableList<SubmitItem> = mutableListOf(),
)
{
    data class SubmitItem(
        val amount: String,
        val claimGroupId: String,
        val claimGroupName: String,
        val currency: String,
        val fileCodes: MutableList<String>,
        val itemId: String,
        val claimItemName:String,
        val rate: String,
        val reason: String,
        val remarks: String,
        val tax: String,
        val transactionDate: String,
        val paxStaff: String,
        val paxClientExisting: String,
        val paxClientPotential: String,
        val paxPrincipal: String
    )
}


