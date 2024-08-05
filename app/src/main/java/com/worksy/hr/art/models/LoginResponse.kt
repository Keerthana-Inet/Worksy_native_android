package com.worksy.hr.art.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val `data`: Data,
    val response: String,
    val status: Boolean
) {


    data class Data(
        val message: String,
        val otp: String,
        val access: String,
        val token: String,
        @SerializedName("refresh_token")
        val refreshToken: String,
        @SerializedName("display_name")
        val displayName: String,
        @SerializedName("f_name")
        val fName: String,
        @SerializedName("image_link")
        val imageLink:String
    )
}