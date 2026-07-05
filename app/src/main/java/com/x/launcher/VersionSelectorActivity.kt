package com.x.launcher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.x.launcher.databinding.ActivityVersionSelectorBinding

class VersionSelectorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVersionSelectorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityVersionSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Terminate active environment stack context and return to the home screen
        binding.btnVersionSelectorBack.setOnClickListener {
            finish()
        }

        // Action trigger bindings to register chosen game client metadata metrics into shared memory
        binding.btnSelectVersion1_21.setOnClickListener {
            commitSelectedVersion("1.21")
        }

        binding.btnSelectVersion1_20_1.setOnClickListener {
            commitSelectedVersion("1.20.1")
        }

        binding.btnSelectVersion1_16_5.setOnClickListener {
            commitSelectedVersion("1.16.5")
        }
    }

    /**
     * Caches chosen version parameters inside metadata properties and restarts intent streams safely.
     */
    private fun commitSelectedVersion(versionName: String) {
        val prefs = getSharedPreferences("XLauncher_Settings", Context.MODE_PRIVATE)
        prefs.edit().putString("KEY_SELECTED_VERSION", versionName).apply()
        
        Toast.makeText(this, "Client version locked to: $versionName", Toast.LENGTH_SHORT).show()
        
        // Return result status flags seamlessly into layout interface contexts
        val resultIntent = Intent()
        resultIntent.putExtra("UPDATED_VERSION", versionName)
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}
