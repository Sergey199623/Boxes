package com.belyakov.room.model.accounts.room.entities

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.NOCASE
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.belyakov.room.model.accounts.entities.Account
import com.belyakov.room.model.accounts.entities.SignUpData

@Entity(tableName = "accounts", indices = [Index(value = ["email"], unique = true)])
class AccountDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "email", collate = NOCASE) val email: String,
    val username: String,
    val password: String,
    @ColumnInfo(name = "created_at") val createdAt: Long
) {

    fun toAccount(): Account = Account(
        id = id,
        email = email,
        username = username,
        createdAt = createdAt
    )

    companion object {
        fun fromSignUpData(signUpData: SignUpData): AccountDbEntity = AccountDbEntity(
            id = 0,
            email = signUpData.email,
            password = signUpData.password,
            username = signUpData.username,
            createdAt = System.currentTimeMillis()
        )
    }
}
