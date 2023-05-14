package com.belyakov.room

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import com.belyakov.room.model.accounts.AccountsRepository
import com.belyakov.room.model.accounts.room.RoomAccountsRepository
import com.belyakov.room.model.boxes.BoxesRepository
import com.belyakov.room.model.boxes.room.RoomBoxesRepository
import com.belyakov.room.model.room.AppDatabase
import com.belyakov.room.model.settings.AppSettings
import com.belyakov.room.model.settings.SharedPreferencesAppSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
object Repositories {

    private lateinit var applicationContext: Context

    private val database: AppDatabase by lazy<AppDatabase> {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
            .createFromAsset("initial_database.db")
            .build()
    }

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val appSettings: AppSettings by lazy {
        SharedPreferencesAppSettings(applicationContext)
    }

    // --- repositories

    val accountsRepository: AccountsRepository by lazy {
        RoomAccountsRepository(
            database.getAccountsDao(),
            appSettings,
            ioDispatcher
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