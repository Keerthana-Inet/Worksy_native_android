package com.worksy.hr.art.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.worksy.hr.art.Constants.USER_TABLE

@Entity(tableName = USER_TABLE)
data class UserTable(
    @ColumnInfo(name = "fullName")
    val name: String,
    val email: String,
    val password: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var id: Int? = null
}
