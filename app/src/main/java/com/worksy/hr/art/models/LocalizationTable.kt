package com.worksy.hr.art.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.worksy.hr.art.Constants

@Entity(tableName = Constants.LOCALIZATION_TABLE)
data class LocalizationTable(
    @PrimaryKey()
    val screen: String,
    val json: String
)
