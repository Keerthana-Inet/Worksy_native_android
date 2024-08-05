package com.worksy.hr.art.models

import com.google.gson.annotations.SerializedName

data class NewpasswdResponse(
    val `data`: Data,
    val message: String,
    val status: Int
) {
    data class Data(
        val message: String,
        val otp: String = "",
        @SerializedName("access_token")
        val accessToken: String,
        @SerializedName("refresh_token")
        val refreshToken: String,
        @SerializedName("display_name")
        val displayName: String,
        @SerializedName("first_name")
        val firstName: String,
        @SerializedName("last_name")
        val lastName :String,
        val access: String,
        @SerializedName("full_name")
        val fullName:String,
        @SerializedName("unique_id")
        val uniqueId:String,
        @SerializedName("profile_pic")
        val profilePic :String

    )
}