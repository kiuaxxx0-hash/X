package com.x.launcher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.x.launcher.auth.AccountManager
import com.x.launcher.auth.AccountType
import com.x.launcher.databinding.ActivityMainBinding
import com.x.launcher.engine.GameDownloader
import com.x.launcher.engine.LaunchConfig
import com.x.launcher.engine.MinecraftLauncher
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var accountManager: AccountManager
    private val currentSelectedVersion = "1.20.1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the account subsystem context
        accountManager = AccountManager(this)

        // Setup a default account for "kiua" if the storage is completely empty
        var activeAccount = accountManager.getActiveAccount()
        if (activeAccount.username == "Guest_X") {
            activeAccount = accountManager.createOfflineAccount("kiua")
        }

        // Dynamically update UI state to match the loaded account data properties
        binding.txtAccountName.text = activeAccount.username
        binding.txtPanelAccountName.text = activeAccount.username
        binding.txtAccountType.text = "${activeAccount.accountType.name} account"
        binding.txtSelectedVersion.text = currentSelectedVersion

        // 1. Top Left Toolbar: Open and switch between different player profiles
        binding.btnAccountManager.setOnClickListener {
            val intent = Intent(this, AccountManagerActivity::class.java)
            startActivity(intent)
        }

        // 2. Top Right Toolbar: Modern Sync and download queue status monitor
        binding.btnDownloadProgress.setOnClickListener {
            Toast.makeText(this, "Sync Engine: Checking for game updates and server assets...", Toast.LENGTH_SHORT).show()
        }

        // 3. Top Right Toolbar: Launch the internal game storage directory explorer
        binding.btnInternalFileManager.setOnClickListener {
            val intent = Intent(this, FileManagerActivity::class.java)
            startActivity(intent)
        }

        // 4. Top Right Toolbar: Open Central Mods Hub with local game version tracking
        binding.btnModsStore.setOnClickListener {
            val intent = Intent(this, ModsStoreActivity::class.java).apply {
                putExtra("GAME_VERSION", currentSelectedVersion)
            }
            startActivity(intent)
        }

        // 5. Top Right Toolbar: Navigate to advanced rendering and JVM tweak controls
        binding.btnAdvancedSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // 6. Right Control Board: Edit skins, offline nicknames, or premium assets
        binding.btnEditProfile.setOnClickListener {
            Toast.makeText(this, "Profile customize interface is under active development!", Toast.LENGTH_SHORT).show()
        }

        // 7. Right Control Board: Execute core game engine download and launch routines
        binding.btnPlay.setOnClickListener {
            executeGameLaunchPipeline()
        }
    }

    /**
     * Orchestrates the active background execution timeline for X Launcher core game services.
     * Reads saved RAM allocation matrices dynamically from local system preferences.
     */
    private fun executeGameLaunchPipeline() {
        val internalStorageDir = File(filesDir, "X-Launcher")
        val gameDirectory = File(internalStorageDir, ".minecraft")
        val assetsDirectory = File(gameDirectory, "assets")

        val downloader = GameDownloader(this)
        val launcher = MinecraftLauncher(this)
        val currentAccount = accountManager.getActiveAccount()

        // Read dynamically configured RAM profiles from custom user tweaks settings
        val prefs = getSharedPreferences("XLauncher_Settings", Context.MODE_PRIVATE)
        val allocatedRam = prefs.getInt("KEY_MAX_RAM", 2048) // Dynamically defaults to 2GB if empty

        Toast.makeText(this, "X-Engine verifying package integrity...", Toast.LENGTH_SHORT).show()

        lifecycleScope.launch {
            val isReady = downloader.downloadGameVersion(currentSelectedVersion, gameDirectory)
            
            if (isReady) {
                // Assemble runtime application parameter blocks dynamically from stored profile data
                val config = LaunchConfig(
                    gameVersion = currentSelectedVersion,
                    maxRamMb = allocatedRam, // Dynamic allocated user RAM injected here cleanly
                    gameDirectory = gameDirectory,
                    assetsDirectory = assetsDirectory,
                    playerUsername = currentAccount.username,
                    playerUuid = currentAccount.uuid,
                    accessToken = currentAccount.accessToken
                )

                val bootSuccess = launcher.bootGame(config)
                if (bootSuccess) {
                    Toast.makeText(this@MainActivity, "Game process deployed successfully!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@MainActivity, "Launch pipeline aborted due to asset transfer failure.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
