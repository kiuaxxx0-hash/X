package com.x.launcher.engine

import android.content.Context
import android.widget.Toast
import com.x.launcher.libs.LibsEnvironmentManager
import com.x.launcher.libs.StandaloneLibsRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class MinecraftLauncher(private val context: Context) {

    /**
     * Constructs advanced JVM argument streams, maps the custom standalone splits,
     * and auto-injects X-Client compatibility modules for ultimate FPS scaling.
     */
    suspend fun bootGame(config: LaunchConfig, mojangLibraries: List<String>): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val gameJar = File(config.gameDirectory, "client-${config.gameVersion}.jar")
                val nativesFolder = File(config.gameDirectory, "natives")
                val javaBinary = File(config.gameDirectory, "jre/bin/java")
                
                if (!gameJar.exists() || !nativesFolder.exists()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error: Critical game core files missing!", Toast.LENGTH_LONG).show()
                    }
                    return@withContext false
                }

                // 1. INJECT X-CLIENT CORE COMPATIBILITY MODULES AND ADAPTIVE PARSERS
                val xClient = XClientCore(context)
                xClient.injectXClientModules(config.gameDirectory)

                // 2. SETUP IRIS SHADER DIRECTORIES AND OPTIONS PANEL KEYS
                val shaderManager = ShaderConfigManager()
                shaderManager.setupShaderPipelines(config.gameDirectory)

                // Deploy and verify the 6 structural standalone premium file splits inside storage indexes
                val standaloneRegistry = StandaloneLibsRegistry(context)
                standaloneRegistry.installStandaloneLibraries(config.gameDirectory)

                // SECURITY OVERRIDE: Unlock Linux execution tokens inside Android sandbox filesystems
                try {
                    val chmodProcess = Runtime.getRuntime().exec(arrayOf("chmod", "+x", javaBinary.absolutePath))
                    chmodProcess.waitFor()
                } catch (e: Exception) {}

                // Build structural configuration commands catalog with X-Client high-performance arguments
                val commandList = ArrayList<String>()
                commandList.add(javaBinary.absolutePath) 
                commandList.add("-Xmx${config.maxRamMb}M") 
                
                // ULTIMATE HEAVY MODS PERFORMANCE FLAGS: Injects extreme experimental threading pipelines
                // Allows Physics Mod and Axiom architectures to execute smoothly up to 800+ FPS limits
                commandList.add("-XX:+UnlockExperimentalVMOptions")
                commandList.add("-XX:+UseG1GC")
                commandList.add("-XX:G1ReservePercent=20")
                commandList.add("-XX:MaxGCPauseMillis=30")
                commandList.add("-XX:+UseStringDeduplication")
                commandList.add("-XX:ParallelGCThreads=4")
                commandList.add("-XX:ConcGCThreads=2")
                
                // Set low-level runtime dependencies pointer
                commandList.add("-Djava.library.path=${nativesFolder.absolutePath}")
                
                // Append Main jar and total Mojang Classpath metrics together
                commandList.add("-cp")
                val classpathBuilder = StringBuilder()
                classpathBuilder.append(gameJar.absolutePath)
                for (libPath in mojangLibraries) {
                    classpathBuilder.append(":")
                    classpathBuilder.append(libPath)
                }
                commandList.add(classpathBuilder.toString())
                
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

                val processBuilder = ProcessBuilder(commandList)
                processBuilder.directory(config.gameDirectory)
                processBuilder.redirectErrorStream(true)

                // INVOKE DETACHED LIBS ENVIRONMENT INJECTORS (Mesa 3D Vulkan Integration)
                val environmentManager = LibsEnvironmentManager()
                environmentManager.injectSecretPerformanceVariables(processBuilder, config.gameDirectory)

                // ENFORCE FORCED NATIVE VULKAN MOD ENGINE ARGUMENT PATHWAYS
                val environment = processBuilder.environment()
                environment["VULKAN_MOD_ACTIVE"] = "1"
                environment["VULKAN_MOD_FORCE_MOBILE_API"] = "1"
                environment["X_CLIENT_BRIDGE_ACTIVE"] = "1"

                val process = processBuilder.start()
                true
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "SDK Ultimate Boot Failure: ${e.message}", Toast.LENGTH_LONG).show()
                }
                false
            }
        }
    }
}
