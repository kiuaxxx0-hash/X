package com.x.launcher.engine

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class GameDownloader(private val context: Context) {

    /**
     * Downloads the official Minecraft server/client JSON manifest 
     * and triggers structural file synchronization.
     */
    suspend fun downloadGameVersion(version: String, targetDir: File): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Ensure target system directories exist safely
                if (!targetDir.exists()) {
                    targetDir.mkdirs()
                }

                // Temporary Mock URL mirroring Mojang launcher storage buckets for manifest files
                val clientJarUrl = "https://mojang.com{version}/client.jar"
                val destinationFile = File(targetDir, "client-$version.jar")

                if (!destinationFile.exists()) {
                    val url = URL(clientJarUrl)
                    val connection = url.openConnection()
                    connection.connect()

                    val inputStream = connection.getInputStream()
                    val outputStream = FileOutputStream(destinationFile)
                    
                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }

                    outputStream.close()
                    inputStream.close()
                }
                true
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Engine Download Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
                false
            }
        }
    }
}

