package com.x.launcher.engine

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ModInstallerEngine(private val context: Context) {

    /**
     * Takes a freshly downloaded mod file from internal caches or API streams,
     * validates its signature, and injects it directly into the targeted profile mods folder.
     */
    suspend fun installModToClient(downloadedFile: File, gameDirectory: File): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Construct target destination pathway dynamic parameters safely
                val modsFolder = File(gameDirectory, "mods")
                if (!modsFolder.exists()) {
                    modsFolder.mkdirs() // Create missing structural mods folder cleanly
                }

                // Enforce binary resource type constraint validations
                if (!downloadedFile.name.endsWith(".jar")) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Installation Aborted: Invalid mod file format type!", Toast.LENGTH_LONG).show()
                    }
                    return@withContext false
                }

                val destinationFile = File(modsFolder, downloadedFile.name)

                // Execute background atomic filesystem data transfers
                FileInputStream(downloadedFile).use { inputStream ->
                    FileOutputStream(destinationFile).use { outputStream ->
                        val buffer = ByteArray(4096)
                        var length: Int
                        while (inputStream.read(buffer).also { length = it } > 0) {
                            outputStream.write(buffer, 0, length)
                        }
                    }
                }

                // Safely purge temporary network delivery cache payloads
                if (downloadedFile.exists()) {
                    downloadedFile.delete()
                }

                true
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Mod Deployment Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
                false
            }
        }
    }
}
