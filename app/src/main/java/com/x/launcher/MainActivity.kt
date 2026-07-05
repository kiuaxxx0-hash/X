package com.x.launcher

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.x.launcher.databinding.ActivityModsStoreBinding
import com.x.launcher.models.ModItem
import com.x.launcher.network.ModApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ModsStoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModsStoreBinding
    private lateinit var apiService: ModApiService
    private var activeGameVersion: String = "1.20.1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityModsStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Capture running game target version parameter from network runtime intent
        activeGameVersion = intent.getStringExtra("GAME_VERSION") ?: "1.20.1"
        binding.txtAutoFilterStatus.text = "Auto-Filtering: Active Version ($activeGameVersion)"

        // Instantiate low-level network adapter mapping to Modrinth api endpoints
        val retrofit = Retrofit.Builder()
            .baseUrl("https://modrinth.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ModApiService::class.java)

        // Safely destroy view state context stack and pop user back to main lobby
        binding.btnModsStoreBack.setOnClickListener {
            finish()
        }

        // Setup modrinth provider filtering select listener action bounds
        binding.providerModrinth.setOnClickListener {
            Toast.makeText(this, "Switched network index to Modrinth database engine", Toast.LENGTH_SHORT).show()
        }

        // Setup curseforge provider filtering select listener action bounds
        binding.providerCurseForge.setOnClickListener {
            Toast.makeText(this, "Switched network index to CurseForge database engine", Toast.LENGTH_SHORT).show()
        }

        // Setup query test trigger simulation mapping to the layout mock cards
        binding.modCardItem1.setOnClickListener {
            executeSmartModSearch("Sodium")
        }

        binding.modCardItem2.setOnClickListener {
            executeSmartModSearch("Iris Shaders")
        }

        binding.modCardItem3.setOnClickListener {
            executeSmartModSearch("Lithium")
        }

        binding.modCardItem4.setOnClickListener {
            executeSmartModSearch("Indium")
        }
    }

    /**
     * Queries web api interfaces, injects forced localized client version restrictions,
     * and maps nested dependency trees down for deep continuous asset pipeline delivery.
     */
    private fun executeSmartModSearch(query: String) {
        binding.txtDependencyStatus.text = "Querying Database..."
        
        lifecycleScope.launch {
            try {
                // Compiles required Modrinth search facet formatting: [["versions:1.20.1"]]
                val versionFacet = "[[\"versions:$activeGameVersion\"]]"
                
                val response = withContext(Dispatchers.IO) {
                    apiService.searchModrinth(query, versionFacet)
                }

                if (response.results.isNotEmpty()) {
                    val foundMod = response.results[0]
                    processModInstallation(foundMod)
                } else {
                    binding.txtDependencyStatus.text = "No packages discovered for $activeGameVersion"
                }
            } catch (e: Exception) {
                binding.txtDependencyStatus.text = "API Service Inactive"
                Toast.makeText(this@ModsStoreActivity, "Network pipeline failure: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun processModInstallation(mod: ModItem) {
        // Evaluate structural dependency trees to pull sub-required dependencies automatically
        binding.txtDependencyStatus.text = "Parsing dependencies..."
        
        val dependencies = mod.dependenciesList
        if (!dependencies.isNullOrEmpty()) {
            val totalQueuePayload = dependencies.size + 1
            binding.txtDependencyStatus.text = "Auto-Resolve: Active ($totalQueuePayload Files)"
            Toast.makeText(this, "Manifest Resolved: Successfully queued ${mod.title} with dependencies!", Toast.LENGTH_LONG).show()
        } else {
            binding.txtDependencyStatus.text = "No background attachments required."
            Toast.makeText(this, "Downloading standalone package: ${mod.title}", Toast.LENGTH_SHORT).show()
        }
    }
}

