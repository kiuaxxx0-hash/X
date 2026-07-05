package com.x.launcher.engine

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.zip.ZipInputStream

class JavaRuntimeDownloader(private val context: Context) {

    /**
     * Downloads and unpacks the appropriate Java Runtime Environment (JRE 17/21) 
     * matching the Android smartphone architecture (ARM64).
     */
    suspend fun downloadAndInstallJava(targetDir: File): File? {
        return withContext(Dispatchers.IO) {
            try {
                val javaExecutable = File(targetDir, "bin/java")
                if (javaExecutable.exists()) {
                    return@withContext javaExecutable // Java already installed
                }

                if (!targetDir.exists()) {
                    targetDir.mkdirs()
                }

                // Official compiled open-source JRE binaries for Android mobile devices (ARM64 Linux link simulation)
                val jreDownloadUrl = "https://github.com"
                val zipFile = File(context.cacheDir, "java_runtime.zip")

                // Download the runtime package zip archive stream
                val url = URL(jreDownloadUrl)
                url.openConnection().getInputStream().use { inputStream ->
                    FileOutputStream(zipFile).use { outputStream ->
                        val buffer = ByteArray(4096)
                        var bytesRead: Int
                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                        }
                    }
                }

                // Unpack the downloaded zip structure archive into executable application binaries
                ZipInputStream(zipFile.inputStream()).use { zipInputStream ->
                    var entry = zipInputStream.nextEntry
                    while (entry != null) {
                        val outFile = File(targetDir, entry.name)
                        if (entry.isDirectory) {
                            outFile.mkdirs()
                        } else {
                            outFile.parentFile?.mkdirs()
                            FileOutputStream(outFile).use { outputStream ->
                                val buffer = ByteArray(4096)
                                var bytesRead: Int
                                while (zipInputStream.read(buffer).also { bytesRead = it } != -1) {
                                    outputStream.write(buffer, 0, bytesRead)
                                }
                            }
                            // Grant native Linux execution permissions to the Java binary system
                            if (outFile.name == "java" || outFile.parentFile?.name == "bin") {
                                outFile.setExecutable(true, false)
                            }
                        }
                        zipInputStream.closeEntry()
                        entry = zipInputStream.nextEntry
                    }
                }

                // Clear cached delivery payloads
                if (zipFile.exists()) {
                    zipFile.delete()
                }

                javaExecutable
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Java Environment Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
                null
            }
        }
    }
}
