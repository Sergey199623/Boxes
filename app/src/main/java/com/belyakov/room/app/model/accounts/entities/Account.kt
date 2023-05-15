package com.belyakov.room.app.model.accounts.entities

/**
 * Information about the user.
 */
data class Account(
    val id: Long,
    val username: String,
    val email: String,
    val createdAt: Long = UNKNOWN_CREATED_AT
) {

    fun isAdmin() = id == ADMIN_ACCOUNT_ID

    companion object {
        const val UNKNOWN_CREATED_AT = 0L

        private const val ADMIN_ACCOUNT_ID = 1L
    }
}