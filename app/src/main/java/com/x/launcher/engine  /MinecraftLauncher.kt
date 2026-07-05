package com.x.launcher.engine

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class MinecraftLauncher(private val context: Context) {

    /**
     * Constructs the official JVM argument stream parameters and hooks dynamic 
     * compiled native SDK components directly into the active runtime processes channel.
     */
    suspend fun bootGame(config: LaunchConfig): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val gameJar = File(config.gameDirectory, "client-${config.gameVersion}.jar")
                val nativesFolder = File(config.gameDirectory, "natives")
                
                if (!gameJar.exists() || !nativesFolder.exists()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error: Game assets or SDK Natives missing! Aborting boot.", Toast.LENGTH_LONG).show()
                    }
                    return@withContext false
                }

                // Construct complete system configuration command argument blocks for JVM execution
                val commandList = ArrayList<String>()
                
                // Points execution pipeline safely into the local JRE binary path
                commandList.add("${config.gameDirectory.absolutePath}/jre/bin/java") 
                commandList.add("-Xmx${config.maxRamMb}M") 
                
                // FORCED SDK BINDING: Injects the desktop graphics compilation native pathways
                commandList.add("-Djava.library.path=${nativesFolder.absolutePath}")
                commandList.add("-cp")
                commandList.add(gameJar.absolutePath)
                
                commandList.add("net.minecraft.client.main.Main") 
                commandList.add("--username")
                commandList.add(config.playerUsername)
                commandList.add("--version")
                commandList.add(config.gameVersion)
                commandList.add("--gameDir")
                commandList.add(config.gameDirectory.absolutePath)
                commandList.add("--assetsDir")
                commandList.add(config.assetsDirectory.absolutePath)
                commandList.add("--uuid")
                commandList.add(config.playerUuid)
                commandList.add("--accessToken")
                commandList.add(config.accessToken)

                // Instantiate runtime environment controller streams
                val processBuilder = ProcessBuilder(commandList)
                processBuilder.directory(config.gameDirectory)
                processBuilder.redirectErrorStream(true)
                
                // Deploy process execution thread
                val process = processBuilder.start()
                
                true
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "SDK Boot Execution Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
                false
            }
        }
    }
}
