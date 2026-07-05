package com.x.launcher.auth

enum class AccountType {
    OFFLINE,
    MICROSOFT,
    GITHUB
}

data class PlayerAccount(
    val username: String,
    val uuid: String,
    val accessToken: String,
    val accountType: AccountType,
    val skinUrl: String? = null
)

