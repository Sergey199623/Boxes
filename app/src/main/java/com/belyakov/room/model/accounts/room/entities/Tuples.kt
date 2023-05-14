package com.belyakov.room.model.accounts.room.entities

data class AccountSignInTuple(
    val id: Long,
    val password: String
)

data class AccountUpdateUsernameTuple(
    val id: Long,
    val username: String
)