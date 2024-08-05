package com.worksy.hr.art.models.claimRequestResponse
data class CreateFormClaimResponse(
    val formDescription: String,
    val formTitle: String,
    val items: List<Item>
)
{
    data class Item(
        val amount: String,
        val claimGroupId: String,
        val currency: String,
        val fileCodes: List<MutableList<String>>,
        val itemId: String,
        val rate: String,
        val reason: String,
        val remarks: String,
        val tax: String,
        val transactionDate: String
    )
}
