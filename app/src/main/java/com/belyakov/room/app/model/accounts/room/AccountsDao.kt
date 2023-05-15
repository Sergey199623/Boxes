package com.belyakov.room.app.model.accounts.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.belyakov.room.app.model.accounts.room.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountsDao {

    @Query("SELECT id, hash, salt FROM accounts WHERE email = :email")
    suspend fun findByEmail(email: String): AccountSignInTuple?

    @Update(entity = AccountDbEntity::class)
    suspend fun updateUsername(updateUsernameTuple: AccountUpdateUsernameTuple)

    @Insert
    suspend fun createNewAccount(account: AccountDbEntity)

    @Query("SELECT * FROM accounts WHERE id = :accountId")
    fun getById(accountId: Long): Flow<AccountDbEntity?>

    @Transaction
    @Query("SELECT * FROM accounts WHERE id = :accountId")
    fun getAccountAndEditedBoxes(accountId: Long): List<AccountAndEditedBoxesTuple>

    @Transaction
    @Query("SELECT * FROM accounts")
    fun getAllData() : Flow<List<AccountAndAllSettingsTuple>>
}