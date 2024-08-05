package com.worksy.hr.art.models.claimRequestResponse

data class UploadClaimFileResponse(
    val `data`: UploadFileData,
    val error: Any,
    val message: String,
    val status: String
)
{
    data class UploadFileData(
        val key: String
    )
}
