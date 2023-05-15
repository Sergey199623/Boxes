package com.belyakov.room.app.model.accounts.entities

import com.belyakov.room.app.model.boxes.entities.BoxAndSettings

data class AccountFullData(
    val account: Account,
    val boxesAndSettings: List<BoxAndSettings>
)