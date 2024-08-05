package com.worksy.hr.art.models.claimRequestResponse

data class ClaimRequestListResponse(
    val `data`: ClaimRequestData,
    val error: Any,
    val message: String,
    val status: String
)
{
    data class ClaimRequestData(
        val completedCount: String,
        val forms: List<ClaimForm>,
        val pendingCount: String
    )
    {
        data class ClaimForm(
            val id:String,
            val code: String,
            val currency: String,
            val date: String,
            val description: String?,
            val items: List<ClaimItem>,
            val status: String,
            val title: String,
            val total: String
        )
        {
            data class ClaimItem(
                val count: String,
                val currency: String,
                val name: String,
                val subtotal: String
            )
        }
    }
}
