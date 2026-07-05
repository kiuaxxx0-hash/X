package com.x.launcher

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.x.launcher.databinding.ActivityMainBinding
import com.x.launcher.engine.GameDownloader
import com.x.launcher.engine.LaunchConfig
import com.x.launcher.engine.MinecraftLauncher
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val currentSelectedVersion = "1.20.1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bind the current client game version text to the visual dashboard view
        binding.txtSelectedVersion.text = currentSelectedVersion

        // 1. Top Left Toolbar: Open and switch between different player profiles
        binding.btnAccountManager.setOnClickListener {
            Toast.makeText(this, "Opening Accounts Manager Screen...", Toast.LENGTH_SHORT).show()
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
     * Checks storage indexes, auto-downloads missing binary resources, and invokes the JVM boot thread.
     */
    private fun executeGameLaunchPipeline() {
        // Instantiate operational file-system pathways inside the sandbox storage environment
        val internalStorageDir = File(filesDir, "X-Launcher")
        val gameDirectory = File(internalStorageDir, ".minecraft")
        val assetsDirectory = File(gameDirectory, "assets")

        val downloader = GameDownloader(this)
        val launcher = MinecraftLauncher(this)

        Toast.makeText(this, "X-Engine verifying package integrity...", Toast.LENGTH_SHORT).show()

        lifecycleScope.launch {
            // Step 1: Ensure localized game jar exists, auto-download if missing
            val isReady = downloader.downloadGameVersion(currentSelectedVersion, gameDirectory)
            
            if (isReady) {
                // Step 2: Assemble runtime application parameter blocks 
                val config = LaunchConfig(
                    gameVersion = currentSelectedVersion,
                    maxRamMb = 2048, // Default 2GB heap boundary allocation
                    gameDirectory = gameDirectory,
                    assetsDirectory = assetsDirectory,
                    playerUsername = "kiua",
                    playerUuid = "00000000-0000-0000-0000-000000000000",
                    accessToken = "00000000000000000000000000000000"
                )

                // Step 3: Trigger low-level binary execution threads
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
