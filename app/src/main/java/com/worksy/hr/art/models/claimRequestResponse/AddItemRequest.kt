package com.worksy.hr.art.models.claimRequestResponse

data class AddItemRequest(
    val pendingItems: MutableList<PendingItem> = mutableListOf()
)
{
    data class PendingItem(
        val currency: String,
        var groupName: String,
        val items: MutableList<Item> = mutableListOf(),
        var total: String
    ){
        data class Item(
            val claimGroupId: String,
            var claimGroupName: String,
            val claimItemId: String,
            var claimItem: String,
            val fileUrls: MutableList<String>,
            val filePath: MutableList<String>,
            val fileSize: MutableList<String>,
            var reason: String,
            var remarks: String,
            var requestedAmount: String,
            val tagName: String,
            val tax: String,
            var transactionData: String,
            val paxStaff: String,
            val paxClientExisting: String,
            val paxClientPotential: String,
            val paxPrincipal: String
        )
    }

}


