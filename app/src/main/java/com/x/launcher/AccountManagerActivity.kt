package com.x.launcher

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.x.launcher.auth.AccountManager
import com.x.launcher.databinding.ActivityAccountManagerBinding

class AccountManagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountManagerBinding
    private lateinit var accountManager: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityAccountManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize local profile tracking database
        accountManager = AccountManager(this)

        // Close identity manager interface and return back to central launcher hub
        binding.btnAccountBack.setOnClickListener {
            finish()
        }

        // Action trigger listener to register new player profiles into application memory
        binding.btnCreateOfflineProfile.setOnClickListener {
            // Simulated validation handler matching the mockup layout design context
            val generatedNickname = "Player_" + (1000..9999).random()
            
            val newSession = accountManager.createOfflineAccount(generatedNickname)
            
            Toast.makeText(
                this, 
                "Successfully registered identity: ${newSession.username}", 
                Toast.LENGTH_SHORT
            ).show()
            
            // Pop stack channel back to update core interface metrics dynamically
            finish()
        }

        // Attach listeners to mock account rows for interactive layout feedback
        binding.accountRowItem1.setOnClickListener {
            Toast.makeText(this, "Profile session 1 selected.", Toast.LENGTH_SHORT).show()
        }
        binding.accountRowItem2.setOnClickListener {
            Toast.makeText(this, "Profile session 2 selected.", Toast.LENGTH_SHORT).show()
        }
        binding.accountRowItem3.setOnClickListener {
            Toast.makeText(this, "Profile session 3 selected.", Toast.LENGTH_SHORT).show()
        }
    }
}

