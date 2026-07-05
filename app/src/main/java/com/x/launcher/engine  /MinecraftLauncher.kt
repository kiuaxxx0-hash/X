package com.x.launcher.engine

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class MinecraftLauncher(private val context: Context) {

    /**
     * Constructs the official JVM argument stream parameters, binds custom native 
     * graphics configurations, and expands the complete loaded Mojang Classpath stream.
     */
    suspend fun bootGame(config: LaunchConfig, mojangLibraries: List<String>): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val gameJar = File(config.gameDirectory, "client-${config.gameVersion}.jar")
                val nativesFolder = File(config.gameDirectory, "natives")
                
                if (!gameJar.exists() || !nativesFolder.exists()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error: Critical game core files or natives missing! Aborting boot.", Toast.LENGTH_LONG).show()
                    }
                    return@withContext false
                }

                // Construct complete structural command parameter array blocks for execution
                val commandList = ArrayList<String>()
                
                // Points process builders directly to the target environment executable path
                commandList.add("${config.gameDirectory.absolutePath}/jre/bin/java") 
                commandList.add("-Xmx${config.maxRamMb}M") 
                
                // Hooks desktop graphics display native engines compilation bindings
                commandList.add("-Djava.library.path=${nativesFolder.absolutePath}")
                
                // BUILD COMPREHENSIVE CLASSPATH: Append main game jar and all downloaded library parameters together
                commandList.add("-cp")
                val classpathBuilder = StringBuilder()
                classpathBuilder.append(gameJar.absolutePath)
                
                for (libPath in mojangLibraries) {
                    // Inject appropriate Linux filesystem array separation tokens cleanly
                    classpathBuilder.append(":")
                    classpathBuilder.append(libPath)
                }
                commandList.add(classpathBuilder.toString())
                
                // Main execute identifier entrypoint mapping
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

                // Execute process builders inside localized target directory frameworks
                val processBuilder = ProcessBuilder(commandList)
                processBuilder.directory(config.gameDirectory)
                processBuilder.redirectErrorStream(true)
                
                // Deploy active execution threads
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
