package com.worksy.hr.art.models.companies

data class ListCompaniesResponse(
    val `data`: List<CompanyData>,
    val error: Any,
    val message: String,
    val status: String
)
{
    data class CompanyData(
        val account_id: Int,
        val name:String="",
        val account_user_id: Int,
        val user_id: Int
    )
}
