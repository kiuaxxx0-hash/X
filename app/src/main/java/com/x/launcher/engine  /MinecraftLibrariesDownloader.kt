package com.x.launcher.engine

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.x.launcher.models.MinecraftVersionMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class MinecraftLibrariesDownloader(private val context: Context) {

    /**
     * Parses the official Mojang version JSON metadata document, filters internal 
     * client libraries and dependencies, and auto-downloads the entire runtime classpath layer.
     */
    suspend fun downloadRequiredLibraries(versionMetadataUrl: String, gameDirectory: File): List<String> {
        val classpathList = ArrayList<String>()
        
        return withContext(Dispatchers.IO) {
            try {
                val librariesFolder = File(gameDirectory, "libraries")
                if (!librariesFolder.exists()) {
                    librariesFolder.mkdirs()
                }

                // Step 1: Connect to Mojang servers and fetch the full version metadata JSON stream
                val url = URL(versionMetadataUrl)
                val jsonString = url.readText()
                val metadata = Gson().fromJson(jsonString, MinecraftVersionMetadata::class.class.java)

                // Step 2: Loop across all identified dependency items within the manifest payload
                for (library in metadata.librariesList) {
                    val artifact = library.downloads?.artifact ?: continue
                    
                    val targetLocalFile = File(librariesFolder, artifact.path)
                    if (!targetLocalFile.parentFile?.exists()!!) {
                        targetLocalFile.parentFile?.mkdirs()
                    }

                    // Step 3: Trigger network stream transfers if the local asset does not exist or size matches corrupt states
                    if (!targetLocalFile.exists() || targetLocalFile.length() != artifact.size) {
                        val downloadUrl = URL(artifact.url)
                        downloadUrl.openConnection().getInputStream().use { inputStream ->
                            FileOutputStream(targetLocalFile).use { outputStream ->
                                val buffer = ByteArray(4096)
                                var readBytes: Int
                                while (inputStream.read(buffer).also { readBytes = it } != -1) {
                                    outputStream.write(buffer, 0, readBytes)
                                }
                            }
                        }
                    }
                    
                    // Accumulate verified absolute path layouts to load into dynamic JVM loaders later
                    classpathList.add(targetLocalFile.absolutePath)
                }
                
                classpathList
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Libraries Engine Failure: ${e.message}", Toast.LENGTH_LONG).show()
                }
                ArrayList()
            }
        }
    }
}
