package com.worksy.hr.art.models.claimApproval

data class ActionRequest(
    val action: String,
    val items: List<ListItem>,
    val remarks: String,
    val formId:String
)
{
    data class ListItem(
        val amount: String,
        val id: Set<String>
    )
}
