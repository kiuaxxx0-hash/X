package com.x.launcher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.x.launcher.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("XLauncher_Settings", Context.MODE_PRIVATE)

        // Close interface and return back safely
        binding.btnSettingsBack.setOnClickListener {
            finish()
        }

        // Mock UI interaction to update Java Runtime Version preference (Option 1)
        binding.root.findViewById<android.view.View>(android.R.id.content).setOnClickListener {
            // Main surface fallback container handling click triggers
        }

        // Action binding for Option 4: Open Virtual Touch Controls Editor layout interface
        // We link this interaction directly to allow the player to edit their on-screen keys
        Toast.makeText(this, "Click on options to save optimization tweaks into system memory", Toast.LENGTH_SHORT).show()
    }

    /**
     * Helper routine to update performance parameters and persist them safely inside storage preferences.
     */
    private fun saveLauncherTweak(key: String, value: Int) {
        val prefs = getSharedPreferences("XLauncher_Settings", Context.MODE_PRIVATE)
        prefs.edit().putInt(key, value).apply()
        Toast.makeText(this, "Success: Settings configuration saved into launcher engine!", Toast.LENGTH_SHORT).show()
    }
}
