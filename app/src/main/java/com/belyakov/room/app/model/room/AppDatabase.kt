package com.belyakov.room.app.model.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.belyakov.room.app.model.AutoMigrationsSpec1To2
import com.belyakov.room.app.model.accounts.room.AccountsDao
import com.belyakov.room.app.model.accounts.room.entities.AccountDbEntity
import com.belyakov.room.app.model.boxes.room.BoxesDao
import com.belyakov.room.app.model.boxes.room.entities.AccountBoxSettingDbEntity
import com.belyakov.room.app.model.boxes.room.entities.BoxDbEntity
import com.belyakov.room.app.model.boxes.room.views.SettingsDbView

@Database(
    entities = [AccountDbEntity::class, BoxDbEntity::class, AccountBoxSettingDbEntity::class],
    version = 2,
    views = [
        SettingsDbView::class
    ],
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2,
            spec = AutoMigrationsSpec1To2::class
        )
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAccountsDao(): AccountsDao

    abstract fun getBoxesDao(): BoxesDao

}