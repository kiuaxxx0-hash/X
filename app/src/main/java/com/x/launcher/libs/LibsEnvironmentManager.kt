package com.x.launcher.libs

import java.io.File

class LibsEnvironmentManager {

    /**
     * Maps independent library files together into the JVM active scope memory using clean separation tokens.
     */
    fun injectSecretPerformanceVariables(processBuilder: ProcessBuilder, gameDirectory: File) {
        val environment = processBuilder.environment()
        val libsDirectory = File(gameDirectory, "libs")
        val nativesDirectory = File(gameDirectory, "natives")

        // 1. RADICAL PERFORMANCE OVERRIDES FOR INDEPENDENT MESA VULKAN PIPELINES
        environment["GALLIUM_DRIVER"] = "zink"
        environment["MESA_LOADER_DRIVER_OVERRIDE"] = "zink"
        environment["VULKAN_DRIVER"] = "turnip"
        environment["LIBGL_DRI3_DISABLE"] = "0"   
        environment["LIBGL_USE_GLES2"] = "1"      
        
        // 2. STABLE CRASH-HOOK REGISTRY DEPLOYMENT
        environment["BHOOK_CAPTURE_EXIT"] = "1"
        environment["BHOOK_DUMP_STACK_ON_CRASH"] = "1"
        
        // 3. ENFORCE STRICT SANDBOX PROTOCOLS
        environment["PROGRADE_SANDBOX_STRICT"] = "1"

        // 4. LOW LEVEL DYNAMIC STANDALONE LINKER PIPELINE (CRUCIAL RE-ORDERING)
        // Instructs the OS layout engine to pull from our private standalone split directory first
        val compiledLibraryPaths = "${libsDirectory.absolutePath}:${nativesDirectory.absolutePath}"
        environment["LD_LIBRARY_PATH"] = compiledLibraryPaths
    }
}
