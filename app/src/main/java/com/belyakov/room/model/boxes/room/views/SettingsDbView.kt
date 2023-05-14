package com.belyakov.room.model.boxes.room.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import com.belyakov.room.model.boxes.room.entities.SettingsTuples

@DatabaseView(
    viewName = "settings_view",
    value = "SELECT \n" +
            " accounts.id as account_id, \n" +
            " boxes.id as box_id, \n" +
            " boxes.color_name, \n" +
            " boxes.color_value, \n" +
            " ifnull(accounts_boxes_settings.is_active, 1) as is_active \n" +
            "FROM accounts \n" +
            "JOIN boxes \n" +
            "LEFT JOIN accounts_boxes_settings \n" +
            " ON accounts_boxes_settings.account_id = account_id \n" +
            " AND accounts_boxes_settings.box_id = boxes.id"
)
data class SettingsDbView(
    @ColumnInfo(name = "account_id") val accountId: Long,
    @ColumnInfo(name = "box_id") val boxId: Long,
    @ColumnInfo(name = "color_name") val colorName: String,
    @ColumnInfo(name= "color_value") val colorValue: String,
    @Embedded val settings: SettingsTuples,
)