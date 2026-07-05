package com.x.launcher.auth

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID

class AccountManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("XLauncher_Auth", Context.MODE_PRIVATE)

    /**
     * Saves the current active player session securely into storage indexes.
     */
    fun saveAccount(account: PlayerAccount) {
        prefs.edit().apply {
            putString("KEY_USERNAME", account.username)
            putString("KEY_UUID", account.uuid)
            putString("KEY_TOKEN", account.accessToken)
            putString("KEY_TYPE", account.accountType.name)
            putString("KEY_SKIN", account.skinUrl)
            apply()
        }
    }

    /**
     * Retrieves the current logged-in account, or creates a default offline profile if none exists.
     */
    fun getActiveAccount(): PlayerAccount {
        val username = prefs.getString("KEY_USERNAME", "Guest_X") ?: "Guest_X"
        val uuid = prefs.getString("KEY_UUID", UUID.nameUUIDFromBytes(username.toByteArray()).toString()) ?: ""
        val token = prefs.getString("KEY_TOKEN", "00000000000000000000000000000000") ?: ""
        val typeStr = prefs.getString("KEY_TYPE", AccountType.OFFLINE.name) ?: AccountType.OFFLINE.name
        val skinUrl = prefs.getString("KEY_SKIN", null)

        return PlayerAccount(username, uuid, token, AccountType.valueOf(typeStr), skinUrl)
    }

    /**
     * Generates a completely new offline player profile structure cleanly.
     */
    fun createOfflineAccount(username: String): PlayerAccount {
        val calculatedUuid = UUID.nameUUIDFromBytes(username.toByteArray()).toString()
        val offlineAccount = PlayerAccount(
            username = username,
            uuid = calculatedUuid,
            accessToken = "offline_token_placeholder",
            accountType = AccountType.OFFLINE
        )
        saveAccount(offlineAccount)
        return offlineAccount
    }
}

