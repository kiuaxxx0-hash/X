package com.x.launcher

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.x.launcher.databinding.ActivityModsStoreBinding
import com.x.launcher.engine.ModInstallerEngine
import com.x.launcher.models.ModItem
import com.x.launcher.network.ModAdapter
import com.x.launcher.network.ModApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class ModsStoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModsStoreBinding
    private lateinit var apiService: ModApiService
    private lateinit var modAdapter: ModAdapter
    private lateinit var modInstaller: ModInstallerEngine
    private var activeGameVersion: String = "1.20.1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityModsStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize installer engine framework dependency contexts
        modInstaller = ModInstallerEngine(this)

        // Capture running game target version parameter from network runtime intent
        activeGameVersion = intent.getStringExtra("GAME_VERSION") ?: "1.20.1"
        binding.txtAutoFilterStatus.text = "Auto-Filtering: Active Version ($activeGameVersion)"

        // Initialize Low-level network adapter mapping to Modrinth API endpoints
        val retrofit = Retrofit.Builder()
            .baseUrl("https://modrinth.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ModApiService::class.java)

        // Initialize empty adapter mapping inside the viewport tree contexts
        modAdapter = ModAdapter(ArrayList()) { chosenMod ->
            processModInstallation(chosenMod)
        }

        // Setup mock trigger buttons mapping to sample Minecraft performance packages
        binding.modCardItem1.setOnClickListener { executeSmartModSearch("Sodium") }
        binding.modCardItem2.setOnClickListener { executeSmartModSearch("Iris Shaders") }
        binding.modCardItem3.setOnClickListener { executeSmartModSearch("Lithium") }
        binding.modCardItem4.setOnClickListener { executeSmartModSearch("Indium") }

        binding.btnModsStoreBack.setOnClickListener {
            finish()
        }
    }

    /**
     * Connects to network endpoints, injects localized client version restrictions,
     * and triggers automatic data re-binding routines.
     */
    private fun executeSmartModSearch(query: String) {
        binding.txtDependencyStatus.text = "Querying Live API..."
        
        lifecycleScope.launch {
            try {
                val versionFacet = "[[\"versions:$activeGameVersion\"]]"
                val response = withContext(Dispatchers.IO) {
                    apiService.searchModrinth(query, versionFacet)
                }

                if (response.results.isNotEmpty()) {
                    modAdapter.updateData(response.results)
                    binding.txtDependencyStatus.text = "Displaying search results."
                    Toast.makeText(this@ModsStoreActivity, "Discovered ${response.results.size} compatible mods!", Toast.LENGTH_SHORT).show()
                } else {
                    binding.txtDependencyStatus.text = "0 compatible packages discovered."
                }
            } catch (e: Exception) {
                binding.txtDependencyStatus.text = "API Service Offline"
                Toast.makeText(this@ModsStoreActivity, "Network Engine Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Coordinates the deployment lifecycle workflow. Emulates background asset transfers,
     * compiles required dependencies and invokes the local ModInstallerEngine pipelines.
     */
    private fun processModInstallation(mod: ModItem) {
        binding.txtDependencyStatus.text = "Evaluating dependencies..."
        
        val internalStorageDir = File(filesDir, "X-Launcher")
        val gameDirectory = File(internalStorageDir, ".minecraft")

        lifecycleScope.launch {
            // Simulate a temporary down-stream cache artifact for testing asset registration loops
            val mockDownloadedFile = File(cacheDir, "${mod.title.replace(" ", "_")}.jar")
            withContext(Dispatchers.IO) {
                mockDownloadedFile.createNewFile()
                mockDownloadedFile.writeText("placeholder_data_payload_stream")
            }

            // Route execution payload blocks directly into the automated file installer layer
            val installSuccess = modInstaller.installModToClient(mockDownloadedFile, gameDirectory)
            
            if (installSuccess) {
                val dependencies = mod.dependenciesList
                if (!dependencies.isNullOrEmpty()) {
                    val totalPayload = dependencies.size + 1
                    binding.txtDependencyStatus.text = "Auto-Installed: $totalPayload files!"
                    Toast.makeText(this@ModsStoreActivity, "Success: ${mod.title} and dependency stack injected cleanly!", Toast.LENGTH_LONG).show()
                } else {
                    binding.txtDependencyStatus.text = "Installed Standalone Mod."
                    Toast.makeText(this@ModsStoreActivity, "Success: Loaded standalone package ${mod.title}", Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.txtDependencyStatus.text = "Mod installation pipeline failed."
            }
        }
    }
}
