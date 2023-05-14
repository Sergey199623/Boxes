package com.belyakov.room.model.boxes.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.belyakov.room.model.boxes.room.entities.AccountBoxSettingDbEntity
import com.belyakov.room.model.boxes.room.entities.BoxAndSettingsTuples
import com.belyakov.room.model.boxes.room.entities.BoxDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BoxesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setActiveFlagForBox(account: AccountBoxSettingDbEntity)

    @Query(
        "SELECT * " +
                "FROM boxes " +
                "LEFT JOIN accounts_boxes_settings " +
                "ON boxes.id = accounts_boxes_settings.box_id" +
                " AND accounts_boxes_settings.account_id = :accountId"
    )
    fun getBoxesAndSettings(accountId: Long): Flow<List<BoxAndSettingsTuples>>
}
