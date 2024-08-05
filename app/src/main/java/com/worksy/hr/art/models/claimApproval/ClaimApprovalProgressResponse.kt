package com.worksy.hr.art.models.claimApproval

data class ClaimApprovalProgressResponse(
    val `data`: ClaimApprovalData,
    val error: Any,
    val message: String,
    val status: String
)
{
    data class ClaimApprovalData(
        val completedCount: String,
        val employees: List<ClaimEmployee>,
        val pendingCount: String
    )
    {
        data class ClaimEmployee(
            val currency: String,
            val forms: List<ClaimForm>,
            val name: String,
            val position: String,
            val total: String
        )
        {
            data class ClaimForm(
                val id:String,
                val code: String,
                val currency: String,
                val date: String,
                val description: String,
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

}
