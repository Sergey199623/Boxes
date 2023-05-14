package com.belyakov.room.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.belyakov.room.model.accounts.room.AccountsDao
import com.belyakov.room.model.accounts.room.entities.AccountDbEntity
import com.belyakov.room.model.boxes.room.BoxesDao
import com.belyakov.room.model.boxes.room.entities.AccountBoxSettingDbEntity
import com.belyakov.room.model.boxes.room.entities.BoxDbEntity

@Database(
    entities = [AccountDbEntity::class, BoxDbEntity::class, AccountBoxSettingDbEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAccountsDao() : AccountsDao

    abstract fun getBoxesDao() : BoxesDao

}