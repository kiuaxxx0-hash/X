package com.x.launcher.engine

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

interface MojangManifestEngine {

    // Fetch the official central mojang master directory manifest file structure
    @GET("https://mojang.com")
    suspend fun getOfficialVersionManifest(): MojangManifestResponse
}

data class MojangManifestResponse(
    @SerializedName("latest") val latestVersions: LatestMetadata,
    @SerializedName("versions") val versionList: List<MojangVersionItem>
)

data class LatestMetadata(
    @SerializedName("release") val latestRelease: String,
    @SerializedName("snapshot") val latestSnapshot: String
)

data class MojangVersionItem(
    @SerializedName("id") val versionId: String,
    @SerializedName("type") val type: String, // release or snapshot
    @SerializedName("url") val versionMetadataUrl: String
)

