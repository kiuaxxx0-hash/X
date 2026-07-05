package com.x.launcher.models

import com.google.gson.annotations.SerializedName

data class MinecraftVersionMetadata(
    @SerializedName("id") val id: String,
    @SerializedName("libraries") val librariesList: List<MinecraftLibraryItem>,
    @SerializedName("mainClass") val mainClass: String
)

data class MinecraftLibraryItem(
    @SerializedName("name") val name: String,
    @SerializedName("downloads") val downloads: LibraryDownloads?
)

data class LibraryDownloads(
    @SerializedName("artifact") val artifact: LibraryArtifact?
)

data class LibraryArtifact(
    @SerializedName("url") val url: String,
    @SerializedName("path") val path: String,
    @SerializedName("size") val size: Long
)

