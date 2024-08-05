package com.worksy.hr.art.models

import com.google.gson.JsonObject

data class LocalizationResponse(
    val `data`: JsonObject,
    val message: String,
    val status: Int
)

