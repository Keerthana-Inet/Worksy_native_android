package com.worksy.hr.art.models.login


data class LoginResponse(
    val `data`: Data,
    val error: List<ErrorData>?= emptyList(),
    val message: String,
    val status: String
)
{
    data class Data(
        val accessToken: String,
        val expiresAt: ExpiresAt,
        val tokenType: String,
        val user: User
    )

    data class ErrorData(
        var code:String,
        var message: String,
        var key:String
    )
    data class ExpiresAt(
        val d: Int,
        val days: Boolean,
        val f: Int,
        val from_string: Boolean,
        val h: Int,
        val i: Int,
        val invert: Int,
        val m: Int,
        val s: Int,
        val y: Int
    )

    data class User(
        val companyCode: String,
        val email: String,
        val id: Int,
        val name: String,
        val phoneNumber: String,
        val username: String
    )
}
