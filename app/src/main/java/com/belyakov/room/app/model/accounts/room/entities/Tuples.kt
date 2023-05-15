package com.belyakov.room.app.model.accounts.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.belyakov.room.app.model.boxes.room.entities.AccountBoxSettingDbEntity
import com.belyakov.room.app.model.boxes.room.entities.BoxDbEntity
import com.belyakov.room.app.model.boxes.room.views.SettingsDbView

data class AccountSignInTuple(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "hash") val hash: String,
    @ColumnInfo(name = "salt") val salt: String,
)

data class AccountUpdateUsernameTuple(
    @ColumnInfo(name = "id") @PrimaryKey val id: Long,
    @ColumnInfo(name = "username") val username: String
)

data class AccountAndEditedBoxesTuple(
    @Embedded val accountDbEntity: AccountDbEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = AccountBoxSettingDbEntity::class,
            parentColumn = "account_id",
            entityColumn = "box_id"
        )
    )
    val boxes: List<BoxDbEntity>
)

data class AccountAndAllSettingsTuple(
    @Embedded val accountDbEntity: AccountDbEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "account_id",
        entity = SettingsDbView::class
    )
    val settings: List<SettingAndBoxTuple>
)

data class SettingAndBoxTuple(
    // Настройки для ящиков
    @Embedded val accountBoxSettingsDbView: SettingsDbView,
    @Relation(
        parentColumn = "box_id",
        entityColumn = "id"
    )
    // Сами ящики, для которых предназначены настройки
    val boxDbEntity: BoxDbEntity
)