package com.x.launcher.engine

import java.io.File

data class LaunchConfig(
    val gameVersion: String,
    val maxRamMb: Int,
    val gameDirectory: File,
    val assetsDirectory: File,
    val playerUsername: String,
    val playerUuid: String,
    val accessToken: String
)

