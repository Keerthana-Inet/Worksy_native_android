package com.worksy.hr.art.models.claimRequestResponse

data class Level (
    val employees: String,
    val status: String
)
data class GetClaimFormResponse(
    val `data`: ClaimFormData,
    val error: Any,
    val message: String,
    val status: String
)
{
    data class ClaimFormData(
        val form: ClaimForm
    )
    {
        data class ClaimForm(
            val approvedItems: List<ApprovedItem>,
            val currency: String,
            val id: String,
            val pendingItems: List<PendingItem>,
            val rejectedItems: List<RejectedItem>,
            val requesterName: String,
            val requesterPosition: String,
            val status: String,
            val total: String,
            val totalApproved: String,
            val totalItems: String,
            val totalPending: String,
            val totalRejected: String,
            val approval: List<Level>
        )

        {
            data class ApprovedItem(
                val currency: String,
                val groupName: String,
                val itemCount: String,
                val items: List<Item>,
                val total: String
            )

            data class PendingItem(
                val currency: String,
                val groupName: String,
                val itemCount: String,
                val items: List<Item>,
                val total: String
            )

            data class RejectedItem(
                val currency: String,
                val groupName: String,
                val itemCount: String,
                val items: List<Item>,
                val total: String
            )
            data class Item(
                val claimGroup: String,
                val claimItem: String,
                val fileUrls: String,
                val id: String,
                val reason: String,
                val remarks: String,
                val requestedAmount: String,
                val tagName: String,
                val tax: String,
                val transactionDate: String,
                val pax: String,
                val paxStaff: String,
                val paxClientExisting: String,
                val paxClientPotential: String,
                val paxPrincipal: String,
                val approval: List<Level>
            )

        }

    }

}
