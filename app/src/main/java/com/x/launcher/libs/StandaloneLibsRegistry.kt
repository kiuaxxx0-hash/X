package com.x.launcher.libs

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.zip.ZipInputStream

class StandaloneLibsRegistry(private val context: Context) {

    /**
     * Downloads and unpacks each individual dynamic C++ optimization engine 
     * as a distinct standalone file into the dedicated local filesystem space.
     */
    suspend fun installStandaloneLibraries(gameDirectory: File): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val libsDirectory = File(gameDirectory, "libs")
                if (!libsDirectory.exists()) {
                    libsDirectory.mkdirs()
                }

                // Verify structural standalone array maps to prevent double download loops
                val crucialFileCheck = File(libsDirectory, "libGL.so")
                if (crucialFileCheck.exists()) {
                    return@withContext true
                }

                // Dedicated high-speed streaming bucket contains the standalone dynamic binary splits
                val bucketUrl = "https://github.com"
                val temporaryZip = File(context.cacheDir, "standalone_cache.zip")

                val url = URL(bucketUrl)
                url.openConnection().getInputStream().use { inputStream ->
                    FileOutputStream(temporaryZip).use { outputStream ->
                        val buffer = ByteArray(4096)
                        var readLen: Int
                        while (inputStream.read(buffer).also { readLen = it } != -1) {
                            outputStream.write(buffer, 0, readLen)
                        }
                    }
                }

                // Unpack phase: Extracts every compiled package as an independent atomic file layout
                ZipInputStream(temporaryZip.inputStream()).use { zipInputStream ->
                    var entry = zipInputStream.nextEntry
                    while (entry != null) {
                        val targetedStandaloneFile = File(libsDirectory, entry.name)
                        
                        if (!entry.isDirectory) {
                            targetedStandaloneFile.parentFile?.mkdirs()
                            FileOutputStream(targetedStandaloneFile).use { outStream ->
                                val buffer = ByteArray(4096)
                                var len: Int
                                while (zipInputStream.read(buffer).also { len = it } != -1) {
                                    outStream.write(buffer, 0, len)
                                }
                            }
                            // ENFORCE INDEPENDENT LINUX PERMISSIONS: Grant raw binary access keys separately
                            targetedStandaloneFile.setExecutable(true, false)
                            targetedStandaloneFile.setReadable(true, false)
                        }
                        zipInputStream.closeEntry()
                        entry = zipInputStream.nextEntry
                    }
                }

                if (temporaryZip.exists()) {
                    temporaryZip.delete()
                }
                true
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Standalone Registry Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
                false
            }
        }
    }
}

