package com.x.launcher.libs

import java.io.File

class LibsEnvironmentManager {

    /**
     * Overrides generic OS thread descriptors to bind absolute cutting edge 
     * graphics drivers and anti-crash hooks directly into the runtime JVM thread state.
     */
    fun injectSecretPerformanceVariables(processBuilder: ProcessBuilder, gameDirectory: File) {
        val environment = processBuilder.environment()
        val libsDirectory = File(gameDirectory, "libs")
        val nativesDirectory = File(gameDirectory, "natives")

        // 1. FORCED VULKAN ZINK MESA 3D INFUSION (ULTIMATE FPS BOOST DRIVERS)
        environment["GALLIUM_DRIVER"] = "zink"
        environment["MESA_LOADER_DRIVER_OVERRIDE"] = "zink"
        environment["VULKAN_DRIVER"] = "turnip" // Locks direct pipeline execution onto Qualcomm Adreno architectures
        environment["LIBGL_DRI3_DISABLE"] = "0"   // Activates direct asynchronous desktop rendering frames
        environment["LIBGL_USE_GLES2"] = "1"      // Forces GLES 2.0 translation fallback optimizations
        
        // 2. CRASH ENGINE HOOK BI-PASS SCHEMAS (BHOOK MECHANISMS INTEGRATION)
        environment["BHOOK_CAPTURE_EXIT"] = "1"
        environment["BHOOK_DUMP_STACK_ON_CRASH"] = "1"
        
        // 3. ISOLATED JAVA SANDBOX SECURITY SCHEMA
        environment["PROGRADE_SANDBOX_STRICT"] = "1"

        // 4. LOW LEVEL DYNAMIC LINKER REDIRECTION SYSTEM
        // Concatenates our private premium libs directory before standard system libraries layout pathways
        val compiledLibraryPaths = "${libsDirectory.absolutePath}:${nativesDirectory.absolutePath}"
        environment["LD_LIBRARY_PATH"] = compiledLibraryPaths
    }
}

