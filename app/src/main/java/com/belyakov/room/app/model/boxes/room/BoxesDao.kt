package com.belyakov.room.app.model.boxes.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.belyakov.room.app.model.boxes.room.entities.AccountBoxSettingDbEntity
import com.belyakov.room.app.model.boxes.room.views.SettingsWithEntitiesTuples
import kotlinx.coroutines.flow.Flow

@Dao
interface BoxesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setActiveFlagForBox(account: AccountBoxSettingDbEntity)

    @Query("SELECT * FROM settings_view WHERE account_id = :accountId")
    fun getBoxesAndSettings(accountId: Long): Flow<List<SettingsWithEntitiesTuples>>
}
