package com.x.launcher

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.x.launcher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize ViewBinding to link XML elements cleanly
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up a click listener for the main PLAY button as a test
        binding.btnPlay.setOnClickListener {
            Toast.makeText(this, "X Launcher is ready to launch Minecraft!", Toast.LENGTH_SHORT).show()
        }
    }
}
