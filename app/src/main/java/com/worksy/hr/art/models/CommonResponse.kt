package com.worksy.hr.art.models

data class CommonResponse(
    val `data`: Any,
    val error: Any,
    val message: String,
    val status: String
)