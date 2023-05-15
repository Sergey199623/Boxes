package com.belyakov.room.app.model.accounts.room

import android.database.sqlite.SQLiteConstraintException
import com.belyakov.room.app.model.AccountAlreadyExistsException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import com.belyakov.room.app.model.AuthException
import com.belyakov.room.app.model.EmptyFieldException
import com.belyakov.room.app.model.Field
import com.belyakov.room.app.model.accounts.AccountsRepository
import com.belyakov.room.app.model.accounts.entities.Account
import com.belyakov.room.app.model.accounts.entities.AccountFullData
import com.belyakov.room.app.model.accounts.entities.SignUpData
import com.belyakov.room.app.model.accounts.room.entities.AccountDbEntity
import com.belyakov.room.app.model.accounts.room.entities.AccountUpdateUsernameTuple
import com.belyakov.room.app.model.boxes.entities.BoxAndSettings
import com.belyakov.room.app.model.room.wrapSQLiteException
import com.belyakov.room.app.model.settings.AppSettings
import com.belyakov.room.app.utils.AsyncLoader
import com.belyakov.room.app.utils.security.SecurityUtils

class RoomAccountsRepository(
    private val accountsDao: AccountsDao,
    private val appSettings: AppSettings,
    private val ioDispatcher: CoroutineDispatcher,
    private val securityUtils: SecurityUtils,
) : AccountsRepository {

    private val currentAccountIdFlow = AsyncLoader {
        MutableStateFlow(AccountId(appSettings.getCurrentAccountId()))
    }

    override suspend fun isSignedIn(): Boolean {
        delay(2000)
        return appSettings.getCurrentAccountId() != AppSettings.NO_ACCOUNT_ID
    }

    override suspend fun signIn(email: String, password: CharArray) =
        wrapSQLiteException(ioDispatcher) {
            if (email.isBlank()) throw EmptyFieldException(Field.Email)
            if (password.isEmpty()) throw EmptyFieldException(Field.Password)

            delay(1000)

            val accountId = findAccountIdByEmailAndPassword(email, password)
            appSettings.setCurrentAccountId(accountId)
            currentAccountIdFlow.get().value = AccountId(accountId)

            return@wrapSQLiteException
        }

    override suspend fun signUp(signUpData: SignUpData) = wrapSQLiteException(ioDispatcher) {
        signUpData.validate()
        delay(1000)
        createAccount(signUpData)
    }

    override suspend fun logout() {
        appSettings.setCurrentAccountId(AppSettings.NO_ACCOUNT_ID)
        currentAccountIdFlow.get().value = AccountId(AppSettings.NO_ACCOUNT_ID)
    }

    override suspend fun getAccount(): Flow<Account?> {
        return currentAccountIdFlow.get()
            .flatMapLatest { accountId ->
                if (accountId.value == AppSettings.NO_ACCOUNT_ID) {
                    flowOf(null)
                } else {
                    getAccountById(accountId.value)
                }
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun updateAccountUsername(newUsername: String) =
        wrapSQLiteException(ioDispatcher) {
            if (newUsername.isBlank()) throw EmptyFieldException(Field.Username)
            delay(1000)
            val accountId = appSettings.getCurrentAccountId()
            if (accountId == AppSettings.NO_ACCOUNT_ID) throw AuthException()

            updateUsernameForAccountId(accountId, newUsername)

            currentAccountIdFlow.get().value = AccountId(accountId)
            return@wrapSQLiteException
        }

    override suspend fun getAllData(): Flow<List<AccountFullData>> {
        val account = getAccount().first()
        if (account == null || !account.isAdmin()) throw AuthException()
        return accountsDao.getAllData()
            .map { accountsAndSettings ->
                accountsAndSettings.map { accountAndAllSettingsTuple ->
                    AccountFullData(
                        account = accountAndAllSettingsTuple.accountDbEntity.toAccount(),
                        boxesAndSettings = accountAndAllSettingsTuple.settings.map { settingAndBoxTuple ->
                            BoxAndSettings(
                                box = settingAndBoxTuple.boxDbEntity.toBox(),
                                isActive = settingAndBoxTuple.accountBoxSettingsDbView.settings.isActive
                            )
                        }
                    )
                }
            }
    }

    private suspend fun findAccountIdByEmailAndPassword(email: String, password: CharArray): Long {
        val tuple = accountsDao.findByEmail(email) ?: throw AuthException()

        val saltBytes = securityUtils.stringToBytes(tuple.salt)
        val hashBytes = securityUtils.passwordToHash(password, saltBytes)
        val hashString = securityUtils.bytesToString(hashBytes)
        password.fill('*')
        if (tuple.hash != hashString) throw AuthException()
        return tuple.id
    }

    private suspend fun createAccount(signUpData: SignUpData) {
        try {
            val entity = AccountDbEntity.fromSignUpData(signUpData, securityUtils)
            accountsDao.createNewAccount(entity)
        } catch (ex: SQLiteConstraintException) {
            val appException = AccountAlreadyExistsException()
            appException.initCause(ex)
            throw appException
        }
    }

    private fun getAccountById(accountId: Long): Flow<Account?> {
        return accountsDao.getById(accountId)
            .map { accountDbEntity -> accountDbEntity?.toAccount() }
    }

    private suspend fun updateUsernameForAccountId(accountId: Long, newUsername: String) {
        accountsDao.updateUsername(AccountUpdateUsernameTuple(accountId, newUsername))
    }

    private class AccountId(val value: Long)

}