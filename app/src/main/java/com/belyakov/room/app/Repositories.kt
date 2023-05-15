package com.belyakov.room.app

import android.content.Context
import androidx.room.Room
import com.belyakov.room.app.model.MIGRATION_2_3
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import com.belyakov.room.app.model.accounts.AccountsRepository
import com.belyakov.room.app.model.accounts.room.RoomAccountsRepository
import com.belyakov.room.app.model.boxes.BoxesRepository
import com.belyakov.room.app.model.boxes.room.RoomBoxesRepository
import com.belyakov.room.app.model.room.AppDatabase
import com.belyakov.room.app.model.settings.AppSettings
import com.belyakov.room.app.model.settings.SharedPreferencesAppSettings
import com.belyakov.room.app.utils.security.DefaultSecurityUtilsImpl
import com.belyakov.room.app.utils.security.SecurityUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
object Repositories {

    private lateinit var applicationContext: Context

    private val database: AppDatabase by lazy<AppDatabase> {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
            .addMigrations(MIGRATION_2_3)
            .createFromAsset("initial_database.db")
            .build()
    }

    val securityUtils: SecurityUtils by lazy { DefaultSecurityUtilsImpl() }

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val appSettings: AppSettings by lazy {
        SharedPreferencesAppSettings(applicationContext)
    }

    // --- repositories

    val accountsRepository: AccountsRepository by lazy {
        RoomAccountsRepository(
            database.getAccountsDao(),
            appSettings,
            ioDispatcher,
            securityUtils
        )
    }

    val boxesRepository: BoxesRepository by lazy {
        RoomBoxesRepository(
            accountsRepository,
            database.getBoxesDao(),
            ioDispatcher
        )
    }

    /**
     * Call this method in all application components that may be created at app startup/restoring
     * (e.g. in onCreate of activities and services)
     */
    fun init(context: Context) {
        applicationContext = context
    }
}