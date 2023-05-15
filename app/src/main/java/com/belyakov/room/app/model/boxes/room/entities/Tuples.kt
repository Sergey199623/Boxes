package com.belyakov.room.app.model.boxes.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class SettingsTuples(
    @ColumnInfo(name = "is_active") val isActive: Boolean
)

data class BoxAndSettingsTuples(
    @Embedded val boxDbEntity: BoxDbEntity,
    @Embedded val settingsDbEntity: AccountBoxSettingDbEntity?
)