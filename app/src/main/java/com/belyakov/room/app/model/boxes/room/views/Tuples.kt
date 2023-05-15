package com.belyakov.room.app.model.boxes.room.views

import androidx.room.Embedded
import androidx.room.Relation
import com.belyakov.room.app.model.accounts.room.entities.AccountDbEntity
import com.belyakov.room.app.model.boxes.room.entities.BoxDbEntity

data class SettingsWithEntitiesTuples(
    @Embedded val settingsDbView: SettingsDbView,

    @Relation(
        parentColumn = "account_id",
        entityColumn = "id",
    )
    val accountDbEntity: AccountDbEntity,

    @Relation(
        parentColumn = "box_id",
        entityColumn = "id",
    )
    val boxDbEntity: BoxDbEntity
)