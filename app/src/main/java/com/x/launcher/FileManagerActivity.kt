package com.x.launcher

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.x.launcher.databinding.ActivityFileManagerBinding

class FileManagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFileManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityFileManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Pop the backstack navigation array safely back to launcher interface screen
        binding.btnFileManagerBack.setOnClickListener {
            finish()
        }

        // Setup local storage asset indices navigation hooks testing listeners
        binding.shortcutMods.setOnClickListener {
            Toast.makeText(this, "Navigating directly to inner virtual mods subdirectory", Toast.LENGTH_SHORT).show()
        }

        binding.shortcutResourcePacks.setOnClickListener {
            Toast.makeText(this, "Navigating directly to inner virtual resourcepacks subdirectory", Toast.LENGTH_SHORT).show()
        }

        binding.shortcutSaves.setOnClickListener {
            Toast.makeText(this, "Navigating directly to inner virtual saves worlds subdirectory", Toast.LENGTH_SHORT).show()
        }

        binding.shortcutConfigs.setOnClickListener {
            Toast.makeText(this, "Navigating directly to inner client configuration properties filesystem", Toast.LENGTH_SHORT).show()
        }
    }
}
