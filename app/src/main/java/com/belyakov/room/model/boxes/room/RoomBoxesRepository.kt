package com.belyakov.room.model.boxes.room

import com.belyakov.room.model.AuthException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import com.belyakov.room.model.accounts.AccountsRepository
import com.belyakov.room.model.boxes.BoxesRepository
import com.belyakov.room.model.boxes.entities.Box
import com.belyakov.room.model.boxes.entities.BoxAndSettings
import com.belyakov.room.model.boxes.room.entities.AccountBoxSettingDbEntity
import com.belyakov.room.model.boxes.room.entities.SettingsTuples
import com.belyakov.room.model.room.wrapSQLiteException
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class RoomBoxesRepository(
    private val accountsRepository: AccountsRepository,
    private val boxesDao: BoxesDao,
    private val ioDispatcher: CoroutineDispatcher
) : BoxesRepository {

    override suspend fun getBoxesAndSettings(onlyActive: Boolean): Flow<List<BoxAndSettings>> {
        return accountsRepository.getAccount()
            .flatMapLatest { account ->
                if (account == null) return@flatMapLatest flowOf(emptyList())
                queryBoxesAndSettings(account.id)
            }
            .mapLatest { boxAndSettings ->
                if (onlyActive) {
                    boxAndSettings.filter { it.isActive }
                } else {
                    boxAndSettings
                }
            }
    }

    override suspend fun activateBox(box: Box) = wrapSQLiteException(ioDispatcher) {
        setActiveFlagForBox(box, true)
    }

    override suspend fun deactivateBox(box: Box) = wrapSQLiteException(ioDispatcher) {
        setActiveFlagForBox(box, false)
    }

    private fun queryBoxesAndSettings(accountId: Long): Flow<List<BoxAndSettings>> {
        return boxesDao.getBoxesAndSettings(accountId).map { entities ->
            entities.map {
                val boxEntity = it.boxDbEntity
                val settingsEntity = it.settingsDbEntity
                BoxAndSettings(
                    box = boxEntity.toBox(),
                    isActive = settingsEntity == null || settingsEntity.settings.isActive
                )
            }
        }
    }

    private suspend fun setActiveFlagForBox(box: Box, isActive: Boolean) {
        if (!accountsRepository.isSignedIn()) throw AuthException()
        val account = accountsRepository.getAccount().first() ?: throw AuthException()
        boxesDao.setActiveFlagForBox(
            AccountBoxSettingDbEntity(
                accountId = account.id,
                boxId = box.id,
                settings = SettingsTuples(isActive)
            )
        )
    }
}